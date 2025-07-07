# Multi-Module EAR Example

This example shows how two EJB modules in the same EAR can use different data
sources by providing a separately qualified `EntityManager` producer to avoid
ambiguous injections.

## Usage

From the HiberSpike Data project root directory (where `hiberspike-data/pom.xml` is located), run:

```sh
mvn install
```

Then run in this directory:

```sh
mvn clean verify
```

Deploy `target/multi-em-ear-assembly-1.0.0-SNAPSHOT.ear` to a Jakarta EE 10
application server.
