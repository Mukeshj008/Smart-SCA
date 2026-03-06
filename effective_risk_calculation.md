# Effective Risk Calculation Reference

Single table showing how **Effective Risk** is derived from Base Severity, Reachability, Exclusion, and Exploitability.

---

## Effective Risk Calculation Table

| Base Severity | Reachable | Excluded | Exploitability | Effective Risk |
| :------------ | :------- | :------- | :------------- | :------------- |
| CRITICAL      | No       | Yes      | N/A            | INFORMATIONAL  |
| HIGH          | No       | Yes      | N/A            | INFORMATIONAL  |
| MEDIUM        | No       | Yes      | N/A            | INFORMATIONAL  |
| LOW           | No       | Yes      | N/A            | INFORMATIONAL  |
| CRITICAL      | Yes      | Yes      | CRITICAL       | CRITICAL       |
| CRITICAL      | Yes      | Yes      | HIGH           | CRITICAL       |
| CRITICAL      | Yes      | Yes      | MEDIUM         | HIGH           |
| CRITICAL      | Yes      | Yes      | LOW            | MEDIUM         |
| HIGH          | Yes      | Yes      | CRITICAL       | HIGH           |
| HIGH          | Yes      | Yes      | HIGH           | HIGH           |
| HIGH          | Yes      | Yes      | MEDIUM         | MEDIUM         |
| HIGH          | Yes      | Yes      | LOW            | LOW            |
| MEDIUM        | Yes      | Yes      | CRITICAL       | MEDIUM         |
| MEDIUM        | Yes      | Yes      | HIGH           | MEDIUM         |
| MEDIUM        | Yes      | Yes      | MEDIUM         | MEDIUM         |
| MEDIUM        | Yes      | Yes      | LOW            | LOW            |
| LOW           | Yes      | Yes      | CRITICAL       | LOW            |
| LOW           | Yes      | Yes      | HIGH           | LOW            |
| LOW           | Yes      | Yes      | MEDIUM         | LOW            |
| LOW           | Yes      | Yes      | LOW            | LOW            |
| CRITICAL      | Yes      | No       | CRITICAL       | CRITICAL       |
| CRITICAL      | Yes      | No       | HIGH           | CRITICAL       |
| CRITICAL      | Yes      | No       | MEDIUM         | HIGH           |
| CRITICAL      | Yes      | No       | LOW            | MEDIUM         |
| HIGH          | Yes      | No       | CRITICAL       | HIGH           |
| HIGH          | Yes      | No       | HIGH           | HIGH           |
| HIGH          | Yes      | No       | MEDIUM         | MEDIUM         |
| HIGH          | Yes      | No       | LOW            | LOW            |
| MEDIUM        | Yes      | No       | CRITICAL       | MEDIUM         |
| MEDIUM        | Yes      | No       | HIGH           | MEDIUM         |
| MEDIUM        | Yes      | No       | MEDIUM         | MEDIUM         |
| MEDIUM        | Yes      | No       | LOW            | LOW            |
| LOW           | Yes      | No       | CRITICAL       | LOW            |
| LOW           | Yes      | No       | HIGH           | LOW            |
| LOW           | Yes      | No       | MEDIUM         | LOW            |
| LOW           | Yes      | No       | LOW            | LOW            |
| CRITICAL      | No       | No       | CRITICAL       | CRITICAL       |
| CRITICAL      | No       | No       | HIGH           | HIGH           |
| CRITICAL      | No       | No       | MEDIUM         | MEDIUM         |
| CRITICAL      | No       | No       | LOW            | LOW            |
| HIGH          | No       | No       | CRITICAL       | HIGH           |
| HIGH          | No       | No       | HIGH           | MEDIUM         |
| HIGH          | No       | No       | MEDIUM         | LOW            |
| HIGH          | No       | No       | LOW            | LOW            |
| MEDIUM        | No       | No       | CRITICAL       | MEDIUM         |
| MEDIUM        | No       | No       | HIGH           | MEDIUM         |
| MEDIUM        | No       | No       | MEDIUM         | LOW            |
| MEDIUM        | No       | No       | LOW            | LOW            |
| LOW           | No       | No       | CRITICAL       | LOW            |
| LOW           | No       | No       | HIGH           | LOW            |
| LOW           | No       | No       | MEDIUM         | LOW            |
| LOW           | No       | No       | LOW            | LOW            |

---

### Exploitability from Conditions

When conditions are verified in source code:

| Conditions Met | Exploitability |
| :------------- | :------------- |
| All conditions met | **CRITICAL** |
| Some conditions met | **HIGH** |
| None met | **LOW** |

---

### Notes

- **Excluded = Yes, Not Reachable:** Exploitability is not used → **INFORMATIONAL**.
- **Excluded = Yes, Reachable:** Exploitability is checked; effective risk = Base Severity × Exploitability (capped at base severity).
- **Excluded = No:** Effective risk = Base Severity × Exploitability (capped at base severity). When **Not Reachable**, exploitability is reduced by 1 level before calculation.
