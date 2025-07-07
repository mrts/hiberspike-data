# Multi-Module EAR Example

This example shows how two EJB modules in the same EAR can use different data
sources by providing a separately qualified `EntityManager` producer to avoid
ambiguous injections. A small Vaadin web application demonstrates that both
repositories work at runtime.

## Usage

From the HiberSpike Data project root directory (where `hiberspike-data/pom.xml` is located), run:

```sh
mvn install
```

Then run in this directory:

```sh
mvn clean package -Pproduction
./scripts/download-wildfly.sh # downloads WildFly
cp ear-assembly/target/multi-em-ear-assembly-1.0.0-SNAPSHOT.ear wildfly/wildfly-36.0.1.Final/standalone/deployments/
./wildfly/wildfly-36.0.1.Final/bin/standalone.bat # runs WildFly
```

Finally, open <http://localhost:8080/vaadin-web/> in browser and click _Add FooA and FooB objects_ to test both data sources.
