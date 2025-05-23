# wildfly-arquillian-tests

A Jakarta EE 10 application project for integration testing the HiberSpike Data library.

The integration tests use H2, WildFly and Arquillian with JUnit 5 and are
running inside a managed WildFly container.

Based on <https://github.com/wildfly-extras/guides/tree/main/arquillian-junit5>.

## Usage

From the project root directory (where `hiberspike-data/pom.xml` is located), run:

```sh
mvn install
```

Then change to the current `wildfly-arquillian-tests` directory and run tests:

```sh
cd tests/wildfly-arquillian-tests
mvn test
```
