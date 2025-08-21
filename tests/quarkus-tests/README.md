# quarkus-tests

A Quarkus project for integration testing the HiberSpike Data library.

It uses Quarkus’s `@QuarkusTest` extension, the embedded H2 database and
illustrates how to inject and work with repositories in a typical Quarkus
application.

Loosely based on <https://github.com/gavinking/data-demo-quarkus-mvn> and
[DeltaSpike tests](https://github.com/apache/deltaspike/blob/master/deltaspike/modules/data/impl/src/test/java/org/apache/deltaspike/data/impl/handler/EntityRepositoryHandlerTest.java).

## Usage

From the project root directory (where `hiberspike-data/pom.xml` is located), run:

```sh
mvn install
```

Then change to the current `quarkus-tests` directory and run:

```sh
cd tests/quarkus-tests
mvn test
```

Quarkus default README follows.

---

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-tests-${VERSION}-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.
