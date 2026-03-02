# SBOM CVE Scanner - Enhanced Edition

A comprehensive Python tool for Software Composition Analysis (SCA) that reads CycloneDX SBOM files, fetches CVEs, performs reachability analysis, detects dependency exclusions, and generates prioritized vulnerability reports in multiple formats.

## Quick Start

```bash
# Clone and setup
cd SCA-automation
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt

# Run with sample SBOM (included)
python sbom_cve_scanner_enhanced.py sample-sbom.json

# Run with your own SBOM and source code
python sbom_cve_scanner_enhanced.py your-sbom.json --source /path/to/source -f all
```

## Features

### Core Capabilities
- **CycloneDX Support**: Parses both JSON and XML CycloneDX SBOM formats (v1.4, v1.5, v1.6)
- **Multi-Ecosystem**: Supports npm, PyPI, Maven, NuGet, RubyGems, Go, Cargo, and more
- **Vulnerability Database**: Uses OSV (Open Source Vulnerabilities) API - no API key required
- **CVE ID Display**: Shows both CVE IDs (e.g., CVE-2021-44228) and GHSA IDs with links

### Advanced Analysis (False Positive Reduction)
- **Reachability Analysis**: Static code analysis to detect if vulnerable dependencies are actually imported/used in your codebase
- **Exclusion Detection**: Parses Maven `pom.xml` files to identify excluded dependencies
- **Exploit Condition Analysis**: Pattern-based analysis of CVE exploit prerequisites (see below)
- **Risk Prioritization**: Combines severity + reachability + exploitability + exclusion status for effective prioritization

## Exploit Condition Analysis - NO API KEY REQUIRED BY DEFAULT

The tool analyzes CVE exploit conditions to determine if a vulnerability is actually exploitable. There are **four analysis options**:

### 1. Heuristic Analysis (DEFAULT - No API Key Needed)
```bash
# This is the default - just run normally
python sbom_cve_scanner_enhanced.py sample-sbom.json
```
Uses **pattern matching and rules** to analyze exploitability. It checks:
- Vulnerability type keywords (deserialization, SQL injection, RCE, etc.)
- Severity levels and attack vectors
- Known exploit patterns

**This is NOT AI - it's rule-based analysis that works offline with no API calls.**

### 2. OpenAI Analysis (Optional - Requires API Key)
```bash
python sbom_cve_scanner_enhanced.py sbom.json --ai openai --api-key YOUR_API_KEY
```
Uses GPT models (gpt-4o-mini by default) for intelligent analysis. Requires an OpenAI API key.

### 3. Google Gemini Analysis (Optional - Requires API Key)
```bash
python sbom_cve_scanner_enhanced.py sbom.json --ai gemini --gemini-key YOUR_API_KEY
```
Uses Google's Gemini AI models for analysis. Get your API key from:
- [Google AI Studio](https://aistudio.google.com/app/apikey) (free tier available)

Supported models:
- `gemini-2.0-flash` (default, fast and cost-effective)
- `gemini-2.5-flash` (latest, more capable)
- `gemini-2.5-pro` (most capable, higher cost)

**Note:** Free tier has rate limits (~15 requests/minute). The tool automatically handles rate limiting with delays and retries.

```bash
# Use a specific Gemini model
python sbom_cve_scanner_enhanced.py sbom.json --ai gemini --gemini-key YOUR_KEY --model gemini-2.5-flash
```

### 4. Ollama Analysis (Optional - Requires Local Server)
```bash
# First, install and start Ollama: https://ollama.ai
ollama pull llama3.2
ollama serve

# Then run with Ollama
python sbom_cve_scanner_enhanced.py sbom.json --ai ollama --model llama3.2
```
Uses locally-running LLM for analysis. No API key, but requires Ollama installed.

### Skip Analysis Entirely
```bash
python sbom_cve_scanner_enhanced.py sbom.json --skip-ai
```

### 5. Exploit-DB Intelligence (OPTIONAL)

Optionally enhance analysis with real-world exploit data from **Exploit-DB** (exploit-db.com):

| Source | What it Provides | Why it Matters |
|--------|------------------|----------------|
| **Exploit-DB** (exploit-db.com) | Public exploits, PoCs, Metasploit modules | Shows if working exploit code exists |

**Use `--exploit-db` flag to enable:**

```bash
# Basic scan (heuristic only - fast)
python sbom_cve_scanner_enhanced.py sbom.json

# With Exploit-DB intelligence (searches exploit-db.com)
python sbom_cve_scanner_enhanced.py sbom.json --exploit-db

# Combined with AI providers + Exploit-DB
python sbom_cve_scanner_enhanced.py sbom.json --ai openai --api-key YOUR_KEY --exploit-db
python sbom_cve_scanner_enhanced.py sbom.json --ai gemini --gemini-key YOUR_KEY --exploit-db
```

#### How Exploit-DB Affects Exploitability

| Indicator | Impact on Exploitability |
|-----------|-------------------------|
| Public exploit found | → **HIGH** (working exploit exists) |
| Verified exploit | → **CRITICAL** (tested and confirmed) |
| Metasploit module | → **HIGH** (easy to weaponize) |
| No exploits found | → Uses heuristic patterns only |

## How Exploit Condition Analysis Works

### The Analysis Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         EXPLOIT ANALYSIS FLOW                               │
└─────────────────────────────────────────────────────────────────────────────┘

1. Scanner finds a CVE (e.g., CVE-2021-44228 in log4j)
                    │
                    ▼
2. Gathers context:
   • CVE description from OSV database
   • Component name and version
   • Code context: Import locations found in your codebase
                    │
                    ▼
3. Analyzer (Heuristic or AI) determines:
   • What conditions are needed to exploit the CVE?
   • Are those conditions met in this codebase?
   • What's the exploitability level: HIGH/MEDIUM/LOW?
                    │
                    ▼
4. Result used to calculate EFFECTIVE RISK
```

### Example: Analyzing Log4Shell (CVE-2021-44228)

**Input Data:**
```
CVE: CVE-2021-44228
Component: log4j-core v2.7
Description: "Apache Log4j2 JNDI features do not protect against attacker 
              controlled LDAP. An attacker who can control log messages can 
              execute arbitrary code when message lookup substitution is enabled."

Code Context Found:
  - /src/Service.java:15 → import org.apache.logging.log4j.Logger
  - /src/Controller.java:42 → logger.info("User: " + userInput)
```

**Analyzer Extracts Conditions:**

| From CVE Description | Extracted Condition |
|---------------------|---------------------|
| "attacker who can control log messages" | User input must reach log statements |
| "JNDI features" | JNDI lookup must be enabled |
| "execute arbitrary code from LDAP" | Outbound network access needed |

**Analyzer Checks Code Context:**

| Condition | Evidence Found | Met? |
|-----------|---------------|------|
| User input in logs | `logger.info("User: " + userInput)` | ✅ YES |
| Log4j imported | `import org.apache.logging.log4j.Logger` | ✅ YES |
| Vulnerable version | log4j-core v2.7 | ✅ YES |

**Output:**
```json
{
  "conditions": [
    "User-controlled input reaches log statements",
    "JNDI lookup feature enabled (default in v2.7)",
    "Outbound network access to attacker LDAP server"
  ],
  "conditions_met": [
    "User input logged directly",
    "Log4j v2.7 is vulnerable"
  ],
  "exploitability": "HIGH",
  "reasoning": "Code shows user input being logged. Version 2.7 has JNDI enabled by default."
}
```

### How Heuristic Analysis Works (Default - No AI)

The heuristic analyzer uses **keyword pattern matching**:

```python
EXPLOIT_PATTERNS = {
    'deserialization': {
        'keywords': ['deserializ', 'unmarshall', 'readObject', 'fromJson'],
        'condition': 'Deserializes untrusted data'
    },
    'sql_injection': {
        'keywords': ['sql', 'query', 'execute', 'prepareStatement'],
        'condition': 'Executes SQL with user input'
    },
    'remote_code_execution': {
        'keywords': ['exec', 'eval', 'Runtime', 'ProcessBuilder', 'shell'],
        'condition': 'Executes commands or code'
    },
    'log_injection': {
        'keywords': ['log4j', 'jndi', 'lookup', 'log injection'],
        'condition': 'Log injection via lookup'
    },
    # ... more patterns for XXE, SSRF, Path Traversal, etc.
}
```

**Process:**
1. Scans CVE description for keywords
2. Matches against known vulnerability patterns
3. Assigns exploitability based on severity + pattern type
4. Fast and works completely offline

### How AI Analysis Works (Ollama/Gemini/OpenAI)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         AI ANALYSIS FLOW                                    │
└─────────────────────────────────────────────────────────────────────────────┘

Your Scanner                              AI Provider
     │                                         │
     │  "Analyze this CVE:                     │
     │   CVE-2021-44228                        │
     │   Description: ...JNDI...LDAP...        │
     │   Code shows: logger.info(userInput)    │
     │                                         │
     │   What conditions are needed?           │
     │   Are they met in this code?"           │
     │ ────────────────────────────────────────>
     │                                         │
     │         AI processes and understands    │
     │         the vulnerability context       │
     │                                         │
     │  Response:                              │
     │  {                                      │
     │    "conditions": [...],                 │
     │    "conditions_met": [...],             │
     │    "exploitability": "HIGH",            │
     │    "reasoning": "..."                   │
     │  }                                      │
     │ <────────────────────────────────────────
```

**Ollama runs locally** on your machine (uses your CPU/GPU), while **Gemini/OpenAI** call cloud APIs.

### Comparison: Heuristic vs AI

| Aspect | Heuristic (Default) | AI (Ollama/Gemini/OpenAI) |
|--------|---------------------|---------------------------|
| **How it works** | Keyword pattern matching | Natural language understanding |
| **Conditions extracted** | Generic patterns | Specific to each CVE |
| **Speed** | Instant (~1 second total) | 2-60 seconds per CVE |
| **Accuracy** | Good (catches common patterns) | Better (understands context) |
| **Cost** | Free | Free (Ollama) / API costs |
| **Requires** | Nothing | API key or local Ollama |
| **Best for** | Quick scans, CI/CD pipelines | Deep security analysis |

### When to Use Which?

| Scenario | Recommended |
|----------|-------------|
| Daily CI/CD scans | Heuristic (fast, reliable) |
| Quick vulnerability check | Heuristic |
| Deep security audit | AI (Ollama or Gemini) |
| Offline environment | Heuristic or Ollama |
| Best accuracy needed | OpenAI or Gemini |

### Report Formats
- **HTML**: Interactive report with clickable evidence panels, color-coded severity, and filterable tables
- **Excel**: Multi-sheet workbook with Summary, Vulnerabilities, Components, and Exclusions tabs
- **PDF**: Executive summary with risk distribution and top prioritized vulnerabilities

## Installation

```bash
# Clone or download this repository
cd SCA-automation

# Create virtual environment (recommended)
python3 -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate

# Install dependencies
pip install -r requirements.txt
```

### Dependencies
```
requests>=2.28.0
openpyxl>=3.1.0      # For Excel reports
reportlab>=4.0.0     # For PDF reports
```

## Usage

### Basic Usage

```bash
# Activate virtual environment
source venv/bin/activate

# Basic scan (HTML output)
python sbom_cve_scanner_enhanced.py sbom.json

# With source code for reachability analysis
python sbom_cve_scanner_enhanced.py sbom.json --source /path/to/source

# Generate all formats (HTML + PDF + Excel)
python sbom_cve_scanner_enhanced.py sbom.json --source /path/to/source -f all -o report
```

### Full Command Reference

```bash
python sbom_cve_scanner_enhanced.py <sbom_file> [options]
```

#### Required Arguments
| Argument | Description |
|----------|-------------|
| `sbom_file` | Path to CycloneDX SBOM file (JSON or XML) |

#### Optional Arguments

**Report Options:**
| Option | Description | Default |
|--------|-------------|---------|
| `-o, --output` | Output report base path (without extension) | Auto-generated: `{sbom_name}_{date}_{time}_{analyzer}` |
| `-f, --format` | Output format: `html`, `pdf`, `excel`, or `all` | `html` |

**Analysis Options:**
| Option | Description | Default |
|--------|-------------|---------|
| `--source` | Source code path(s) for reachability analysis (can specify multiple) | None |
| `--skip-ai` | Skip exploit condition analysis entirely | False |
| `--exploit-db` | Enable Exploit-DB intelligence (searches exploit-db.com for public exploits) | False |

**Exploit Analysis Provider (all optional - heuristic used by default):**
| Option | Description | Default |
|--------|-------------|---------|
| `--ai` | Analysis method: `heuristic`, `openai`, `gemini`, or `ollama` | `heuristic` |
| `--api-key` | API key for OpenAI (only if using `--ai openai`) | None |
| `--gemini-key` | API key for Google Gemini (only if using `--ai gemini`) | None |
| `--model` | Model name for AI providers | gpt-4o-mini / gemini-2.0-flash / llama3.2 |
| `--ollama-url` | Ollama server URL (only if using `--ai ollama`) | http://localhost:11434 |

**Network Options:**
| Option | Description | Default |
|--------|-------------|---------|
| `--delay` | Delay between API requests to OSV (seconds) | 0.3 |
| `--no-verify-ssl` | Disable SSL verification (use if SSL certificate errors) | False |

**Other:**
| Option | Description |
|--------|-------------|
| `-h, --help` | Show detailed help message with examples |
| `--version` | Show version number |

### Examples

```bash
# Basic scan - NO API KEY NEEDED (uses heuristic analysis)
# Output: sbom_20260302_120000_heuristic.html (auto-generated filename)
python sbom_cve_scanner_enhanced.py sbom.json

# Full analysis with source code and all report formats
python sbom_cve_scanner_enhanced.py sbom.json --source ./src -f all

# With Exploit-DB intelligence (searches exploit-db.com)
python sbom_cve_scanner_enhanced.py sbom.json --source ./src --exploit-db -f all

# Custom output name
python sbom_cve_scanner_enhanced.py sbom.json -f html -o my_report

# Fix SSL certificate errors (common on macOS)
python sbom_cve_scanner_enhanced.py sbom.json --source ./src -f all --no-verify-ssl

# --- OPTIONAL: Using AI providers (requires API key or local server) ---

# With OpenAI + Exploit-DB (requires API key)
python sbom_cve_scanner_enhanced.py sbom.json --ai openai --api-key sk-your-key-here --exploit-db

# With Google Gemini + Exploit-DB (requires API key - free tier available)
python sbom_cve_scanner_enhanced.py sbom.json --ai gemini --gemini-key your-gemini-key --exploit-db

# With local Ollama (requires Ollama server running)
python sbom_cve_scanner_enhanced.py sbom.json --ai ollama --model llama3.2

# Skip exploit analysis entirely
python sbom_cve_scanner_enhanced.py sbom.json --skip-ai
```

## Understanding the Reports

### Risk Prioritization

The tool calculates an **Effective Risk** score that combines multiple factors:

| Factor | Description |
|--------|-------------|
| **Severity** | CVE severity from vulnerability database (CRITICAL, HIGH, MEDIUM, LOW) |
| **Reachability** | Is the vulnerable code actually imported/used? |
| **Exclusion** | Is the dependency excluded in pom.xml? |
| **Exploitability** | Are the conditions for exploitation met? |

A CRITICAL severity CVE may have **LOW effective risk** if:
- The dependency is NOT REACHABLE (not imported in code)
- The dependency is EXCLUDED (excluded in pom.xml)
- Exploit conditions are not met

### Interactive HTML Features

Click on badges to see evidence:

| Badge | Click to See |
|-------|--------------|
| **REACHABLE** (Orange) | File paths where imports were found, usage patterns |
| **NOT REACHABLE** (Green) | Analysis details, possible reasons, impact assessment |
| **EXCLUDED** (Grey) | Which dependencies exclude it, pom.xml file paths |
| **INCLUDED** (Orange) | Confirmation no exclusion found, risk warning |

### Excel Sheets

| Sheet | Contents |
|-------|----------|
| **Summary** | Report metadata, total counts, severity breakdown |
| **Vulnerabilities** | Full CVE list with all details (filterable) |
| **Components** | All dependencies with vulnerability counts |
| **Exclusions** | Excluded dependencies with evidence |

## Reachability Analysis

The tool scans your source code for import statements to determine if vulnerable dependencies are actually used:

### Supported Languages
- Java (`.java`, `.kt`, `.scala`)
- Python (`.py`)
- JavaScript/TypeScript (`.js`, `.ts`, `.tsx`, `.jsx`)
- Go (`.go`)

### How It Works
1. Builds an index of all imports in your codebase
2. For each vulnerable component, searches for matching imports
3. Reports file paths and line numbers where imports are found
4. Marks dependencies as REACHABLE or NOT REACHABLE

## Exclusion Detection

The tool parses Maven `pom.xml` files to detect excluded dependencies:

### What It Detects
- `<exclusions>` tags in dependency declarations
- Wildcard exclusions (e.g., `<artifactId>*</artifactId>`)
- Dependencies with `provided`, `test`, or `system` scope

### Example pom.xml Exclusion
```xml
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-commons</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

## Sample SBOM Included

A sample SBOM file (`sample-sbom.json`) is included with common vulnerable dependencies for testing:

| Component | Version | Ecosystem | Known Vulnerabilities |
|-----------|---------|-----------|----------------------|
| jackson-databind | 2.9.8 | Maven | Multiple deserialization CVEs |
| log4j-core | 2.14.1 | Maven | Log4Shell (CVE-2021-44228) |
| spring-core | 5.2.0.RELEASE | Maven | Spring4Shell and others |
| struts2-core | 2.5.20 | Maven | RCE vulnerabilities |
| lodash | 4.17.15 | npm | Prototype pollution |
| django | 3.1.0 | PyPI | Multiple security fixes |

```bash
# Test with the sample SBOM
python sbom_cve_scanner_enhanced.py sample-sbom.json -f all
```

## Generating CycloneDX SBOMs

### For Maven Projects
```bash
mvn org.cyclonedx:cyclonedx-maven-plugin:makeAggregateBom
# Output: target/bom.json
```

### For npm Projects
```bash
npx @cyclonedx/cyclonedx-npm --output-file sbom.json
```

### For Python Projects
```bash
pip install cyclonedx-bom
cyclonedx-py -o sbom.json
```

### For Gradle Projects
```bash
# Add plugin to build.gradle, then run:
gradle cyclonedxBom
```

## Troubleshooting

### SSL Certificate Errors
Common on macOS with fresh Python installations:
```bash
python sbom_cve_scanner_enhanced.py sbom.json --no-verify-ssl
```

### Rate Limiting
If you encounter API errors, increase the delay:
```bash
python sbom_cve_scanner_enhanced.py sbom.json --delay 1.0
```

### No Vulnerabilities Found
- Ensure your SBOM contains valid Package URLs (purl)
- Verify the component versions are correctly specified
- Check network connectivity to api.osv.dev

### Virtual Environment Issues
```bash
# Recreate virtual environment
rm -rf venv
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

## Output Examples

### Console Output
```
Parsing SBOM: sample-sbom.json
Found 15 components

Building reachability index from: /path/to/source
Indexed 500 unique imports

Scanning for vulnerabilities...
  [1/15] Scanning jackson-databind@2.9.8... 48 vulnerabilities
  [2/15] Scanning log4j-core@2.14.1... 5 vulnerabilities
  [3/15] Scanning spring-core@5.2.0.RELEASE... 4 vulnerabilities
  ...

Analyzing dependency exclusions...
Found 12 exclusion patterns

Performing reachability analysis...
Performing exploit condition analysis using Heuristic Analysis...
  Analyzed 42 high-priority vulnerabilities

Generating report(s)...
Report generated: sample-sbom_20260302_120000_heuristic.html
PDF report generated: sample-sbom_20260302_120000_heuristic.pdf
Excel report generated: sample-sbom_20260302_120000_heuristic.xlsx

Scan complete!
  Components scanned: 15
  Total vulnerabilities: 72
  Reports generated: sample-sbom_20260302_120000_heuristic.html, ...
```

## How It Works (Architecture)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         SBOM CVE Scanner Pipeline                           │
└─────────────────────────────────────────────────────────────────────────────┘

1. PARSE SBOM          2. FETCH CVEs           3. ANALYZE              4. REPORT
   ┌─────────┐           ┌─────────┐           ┌─────────────┐         ┌──────┐
   │ sbom.   │──────────>│   OSV   │──────────>│ Reachability│────────>│ HTML │
   │ json/xml│           │   API   │           │ Exclusions  │         │ PDF  │
   └─────────┘           │ (free)  │           │ Exploit     │         │ Excel│
                         └─────────┘           │ Analysis    │         └──────┘
                                               └─────────────┘
```

### Step-by-Step Process:

1. **Parse SBOM**: Reads CycloneDX file (JSON/XML) to extract dependencies
2. **Fetch CVEs**: Queries OSV API for each dependency (free, no API key)
3. **Analyze**:
   - **Reachability**: Scans source code for imports to detect used dependencies
   - **Exclusions**: Parses pom.xml files to find excluded dependencies
   - **Exploit Conditions**: Uses heuristic rules (default) or AI to assess exploitability
4. **Calculate Risk**: Combines severity + reachability + exclusions + exploitability
5. **Generate Reports**: Creates HTML, PDF, and/or Excel reports

## API Information

This tool uses the [OSV (Open Source Vulnerabilities)](https://osv.dev/) API:
- Free to use, no API key required
- Aggregates vulnerabilities from GitHub Advisory, NVD, and other sources
- Supports all major package ecosystems

## Severity Ratings

| Severity | CVSS Score | Color |
|----------|------------|-------|
| Critical | 9.0 - 10.0 | Red |
| High | 7.0 - 8.9 | Orange |
| Medium | 4.0 - 6.9 | Yellow |
| Low | 0.1 - 3.9 | Green |

## License

MIT License
