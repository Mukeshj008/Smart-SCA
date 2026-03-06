# Sample Vulnerable Java Application

A sample Java project utilizing 10 vulnerable dependencies for SCA (Software Composition Analysis) testing.

## Dependencies (from sample_sbom_10_vulns.json)

1. **commons-beanutils** 1.9.3 - Bean property utilities
2. **jackson-databind** 2.8.5 - JSON processing
3. **log4j-core** 2.7 - Logging
4. **log4j-api** 2.7 - Logging API
5. **spring-core** 4.3.7.RELEASE - Spring Framework
6. **gson** 2.8.0 - JSON library
7. **hibernate-validator** 5.3.4.Final - Bean validation
8. **elasticsearch** 5.2.2 - Search engine
9. **commons-lang3** 3.6 - String utilities
10. **liquibase-core** 3.1.1 - Database migrations

## Project Structure

```
sample-java-project/
├── pom.xml
├── src/main/java/com/example/app/
│   ├── Application.java
│   ├── AppConfig.java
│   ├── config/          # Jackson, Elasticsearch, Liquibase config
│   ├── controller/      # REST controllers (User, Product, Order)
│   ├── model/           # User, Order, Product entities
│   ├── service/         # JsonService, GsonService, BeanCopyService, etc.
│   ├── repository/      # UserRepository, ProductRepository
│   ├── util/            # StringUtil, ReflectionUtil
│   ├── facade/          # ApplicationFacade
│   └── batch/           # ReportGenerator
└── src/main/resources/
    ├── log4j2.xml
    └── db/changelog/    # Liquibase migrations
```

## Build

```bash
mvn clean compile
```

## Generate SBOM

Use Syft or similar tool to generate an SBOM from this project:

```bash
syft . -o cyclonedx-json=sbom.json
```

## Scan with SCA Tool

```bash
python smart_sca_scanner.py sample_sbom_10_vulns.json --source sample-java-project -f all
```
