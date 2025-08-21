# HiberSpike Data

**HiberSpike Data** is a small library that provides a modern reimplementation
of the [DeltaSpike Data `EntityRepository`](https://deltaspike.apache.org/documentation/data.html#_the_entityrepository_interface)
interface for Jakarta EE 10+ applications, built using [Hibernate Data Repositories](https://docs.jboss.org/hibernate/orm/6.6/repositories/html_single/Hibernate_Data_Repositories.html).

## Why HiberSpike Data?

DeltaSpike Data is no longer actively maintained and doesn’t officially support
the `jakarta` namespace and Jakarta EE 9+. HiberSpike Data provides a near
drop-in replacement for DeltaSpike Data `EntityRepository` interface for
Jakarta EE 10+ applications. It uses [Hibernate’s stateful sessions](https://docs.jboss.org/hibernate/orm/7.0/introduction/html_single/Hibernate_Introduction.html#stateful-and-stateless-sessions)
and is compatible with Hibernate Envers, lazy loading etc.

> **Note:** Hibernate’s stateful and stateless sessions
> are unrelated to EJB stateful or stateless session beans.

## Why not Jakarta Data 1.0?

[Jakarta Data 1.0](https://jakarta.ee/specifications/data/1.0/) uses stateless
sessions, which breaks compatibility with Hibernate Envers and lazy loading,
and lacks support for Jakarta Persistance annotations like `@Persist`, `@Merge`,
`@Remove` etc that would give access to the corresponding operations of
`EntityManager`.

These issues will be addressed in the upcoming [Jakarta Data version 1.1](https://jakarta.ee/specifications/data/1.1/),
but the 1.1 specification is still under development.

Neither is it an API-compatible drop-in replacement for projects migrating away
from DeltaSpike Data out of the box.

## What is Hibernate Data Repositories?

[Hibernate Data Repositories](https://docs.jboss.org/hibernate/orm/6.6/repositories/html_single/Hibernate_Data_Repositories.html)
is a lightweight compile-time query generation system built into Hibernate ORM
6+. It supports `@HQL`, `@SQL` and `@Find` method annotations and `Page` for pagination.

The implementation is generated via annotation processing by `hibernate-jpamodelgen`.

## How to use HiberSpike Data

### 1. Add HiberSpike and Hibernate dependencies

Add the following lines to `pom.xml` under `<dependencies>`:

```xml
<dependencies>
    ...
    <dependency>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>${hibernate.version}</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>io.github.mrts</groupId>
        <artifactId>hiberspike-data</artifactId>
        <version>${hiberspike-data.version}</version>
    </dependency>
</dependencies>
```

### 2. Add `hibernate-jpamodelgen` annotation processor to Maven compiler plugin

Add the `hibernate-jpamodelgen` plugin to `pom.xml` under `maven-compiler-plugin` configuration:

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
                <configuration>
                    <annotationProcessorPaths>
                        <!-- Hibernate Jpamodelgen processes Hibernate Data Repository annotations
                        and generates the repository implementations. -->
                        <path>
                            <groupId>org.hibernate.orm</groupId>
                            <artifactId>hibernate-jpamodelgen</artifactId>
                            <version>${hibernate.version}</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

Maven compiler plugin version 3.13 or higher is required.

### 3. Create an `EntityManager` producer

The generated repository implementations use CDI to inject `EntityManager`, so
an `EntityManager` producer is required. You can either use
[a producer field or a producer method](https://jakarta.ee/learn/docs/jakartaee-tutorial/9.1/cdi/cdi-adv/cdi-adv.html#_using_producer_methods_producer_fields_and_disposer_methods_in_cdi_applications).

```java
@Dependent
public class EntityManagerProducer {

    @Produces // producer field
    @Dependent
    @PersistenceContext
    private EntityManager em;
}
```

### 4. Create your repository interface

```java
public interface BookRepository extends EntityRepository<Book, Long> {

    @Find
    Book findByTitle(String title);

}
```

Add at least one method with either `@HQL`, `@SQL` or `@Find` annotation from
the `org.hibernate.annotations.processing` package to trigger annotation
processing, then `hibernate-jpamodelgen` (see `maven-compiler-plugin`
configuration below) will generate a `BookRepository_` implementation at
compile time.

To learn more about HQL and JPQL, see the 
[_Guide to Hibernate Query Language_](https://docs.jboss.org/hibernate/orm/6.6/querylanguage/html_single/Hibernate_Query_Language.html).
For practical examples of how to use `@HQL` for advanced cases such as deletes,
updates, counting, case-insensitive searches, top-N queries etc, see _Porting
DeltaSpike convention-based methods to HQL_ below.

### 5. Inject and use the repository

```java
@Inject
BookRepository bookRepository;

...

public void saveBook() {
    bookRepository.save(new Book("Hibernate 101"));
}
```

### 6. Pagination

Use `org.hibernate.query.Page` for pagination:

```java
List<Book> books = bookRepository.findAll(Page.page(10, 0)); // first 10 results
```

## How to migrate from DeltaSpike Data to HiberSpike Data

HiberSpike Data is designed to be a near drop-in replacement for most
DeltaSpike Data use cases, but some migration steps are required. Here’s how
to migrate your project:

### 1. Replace DeltaSpike Data dependency with HiberSpike Data

Remove `<dependency> ... deltaspike-data-* ... </dependency>` and add
HiberSpike and Hibernate dependencies to `pom.xml` as described in *How to use
HiberSpike Data* above.

### 2. Add `hibernate-jpamodelgen` annotation processor to Maven compiler plugin

Add the `hibernate-jpamodelgen` plugin to `pom.xml` under
`maven-compiler-plugin` configuration as described in *How to use HiberSpike
Data* above.

### 3. Create an `EntityManager` producer

Create an `EntityManager` producer field or method as described in *How to use
HiberSpike Data* above or use the producer that was created for DeltaSpike
Data.

### 4. Remove DeltaSpike properties file

Delete `src/main/resources/META-INF/apache-deltaspike.properties`.

### 5. Update repository imports

Replace `import org.apache.deltaspike.data.api.EntityRepository;` with `import
ee.hiberspike.data.EntityRepository;`.

Also, see *Why doesn't HiberSpike `EntityRepository` include `count()` and
`findBy()`?* below.

### 6. Remove DeltaSpike `@Repository` annotations

HiberSpike Data uses plain repository intefaces, remove the DeltaSpike
`@Repository` annotation from all your repository interfaces.

### 7. Replace `@Query` with `@HQL`

Replace DeltaSpike’s `@Query` annotation with Hibernate Data Repositories’
`@HQL` annotation from the `org.hibernate.annotations.processing` package,
which serves a similar purpose:

```java
// Old
@Query("SELECT b FROM Book b WHERE b.title = ?1")
Book findByTitle(String title);

// New
@HQL("SELECT b FROM Book b WHERE b.title = ?1")
Book findByTitle(String title);
```

### 8. Annotate all DeltaSpike naming convention-based methods with `@Find` or `@HQL`

DeltaSpike Data supports generating queries based on method naming conventions,
but Hibernate Data Repositories require queries to be defined explicitly with
annotations:

```java
// Old
Book findByTitle(String title);

// New
@Find
Book findByTitle(String title);
```

To port DeltaSpike convention-based methods:

- for simple `findBy{fieldname}` queries, add the `@Find` annotation; note
  that `hibernate-jpamodelgen` assembles queries based on method parameter
  names that have to match entity field names (`String title` has
  to be a field in the entity class `Book` in the example above),
- use `@OrderBy` and `@Param` as needed,
- for more complex and modifying queries, use `@HQL` and write the filtering
  logic in the HQL expression, see _Porting DeltaSpike convention-based methods
  to HQL_ below,
- at least one method with either `@HQL`, `@SQL` or `@Find` annotation is
  required in a repository interface to trigger annotation processing.

#### Porting DeltaSpike convention-based methods to HQL

`@HQL` gives you full control over your queries with compile-time checking and
none of the limitations of naming conventions. It is intended for advanced cases
such as deletes, updates, counting, case-insensitive searches, top-N queries
etc.

To learn more about HQL and JPQL, see the 
[_Guide to Hibernate Query Language_](https://docs.jboss.org/hibernate/orm/6.6/querylanguage/html_single/Hibernate_Query_Language.html).

A few examples:

```java
// DeltaSpike
Book findByTitleLikeIgnoreCase(String title);

// Hibernate Data
@HQL("where title ilike :title")
Book findByTitleLikeIgnoreCase(String title);
```

```java
// DeltaSpike
List<Book> findFirst2ByTitle(String title);

// Hibernate Data
@HQL("where title = :title order by id limit 2")
List<Book> findFirst2ByTitle(String title);
```

```java
// DeltaSpike
long countByTitle(String title);

// Hibernate Data
@HQL("select count(b) from Book b where title = :title")
long countByTitle(String title);
```

```java
// DeltaSpike
void deleteByTitleAndAvailable(String title, boolean available);

// Hibernate Data
@HQL("delete from Book where title = :title and available = :available")
void deleteByTitleAndAvailable(String title, boolean available);
```

For a complete method-by-method migration example, see the following files in the test project:

| DeltaSpike | HiberSpike |
| ---------- | ---------- |
| [DeltaSpike SimpleRepository](https://github.com/apache/deltaspike/blob/master/deltaspike/modules/data/impl/src/test/java/org/apache/deltaspike/data/test/service/SimpleRepository.java) | [HiberSpike SimpleRepository](https://github.com/mrts/hiberspike-data/blob/main/tests/quarkus-tests/src/main/java/org/apache/deltaspike/data/test/service/SimpleRepository.java) |
| [DeltaSpike QueryHandlerTest](https://github.com/apache/deltaspike/blob/master/deltaspike/modules/data/impl/src/test/java/org/apache/deltaspike/data/impl/handler/QueryHandlerTest.java) | [HiberSpike QueryHandlerTest](https://github.com/mrts/hiberspike-data/blob/main/tests/quarkus-tests/src/test/java/org/apache/deltaspike/data/impl/handler/QueryHandlerTest.java) |

### 9. Pagination: replace `@FirstResult` and `@MaxResults` with `Page`

Hibernate Data Repositories use `org.hibernate.query.Page` for paging, replace
`@FirstResult` and `@MaxResults` with `Page`:

```java
// Old
List<Book> findByAuthor(String author, @FirstResult int firstResult, @MaxResults int maxResults);

// New
@Find
List<Book> findByAuthor(String author, Page page);
```
## Test projects and usage examples

There are two example test projects in the [tests/](tests/) directory which
verify the library’s functionality but also serve as practical examples for
using HiberSpike Data in modern Jakarta EE 10+ applications.

The test projects also demonstrate how to write integration tests that use a
real database in either Quarkus or WildFly environments.

Before running the tests, make sure to build and install HiberSpike Data to
your local Maven repository by running `mvn install` at the root of the
repository.

### Quarkus integration tests ([tests/quarkus-tests](tests/quarkus-tests))

This example project shows how to use HiberSpike Data repositories with
Quarkus. It uses Quarkus’s `@QuarkusTest` extension, the embedded H2 database
and illustrates how to inject and work with repositories in a typical Quarkus
application.

Running the tests with Maven:

```sh
mvn install
cd tests/quarkus-tests
mvn test
```

### WildFly + Arquillian integration tests ([tests/wildfly-arquillian-tests](tests/wildfly-arquillian-tests)):

This example project demonstrates how to use HiberSpike Data repositories in a
full Jakarta EE application. The integration tests use H2, WildFly and
Arquillian with JUnit 5 and are running inside a managed WildFly container.

Running the tests with Maven:

```sh
mvn install
cd tests/wildfly-arquillian-tests
mvn test
```

## Why doesn't HiberSpike `EntityRepository` include `count()` and `findBy()`?

HiberSpike Data base `EntityRepository` intentionally does not include
`count()`, `findBy()` or `findOptionalBy()` that are present in DeltaSpike's
`EntityRepository`.

The implementation of these methods requires access to the entity’s class at
runtime, but due to Java’s generic type erasure, this information isn't easily
available.

HiberSpike Data provides a separate interface `ExtendedEntityRepository` that
includes these methods, but requires you to override the `getEntityClass()`
method to give access to the entity class:

```java
public interface BookRepository extends ExtendedEntityRepository<Book, Long> {

    @Find
    Book findByTitle(String title);


    @Override
    protected Class<Book> getEntityClass() {
        return Book.class;
    }

}
```

By leaving these methods out of the base `EntityRepository`, you're not forced to
override `getEntityClass()` in every repository and can opt in to these
additional features only when needed.

## Example-based query API is not yet implemented

HiberSpike Data does not currently support DeltaSpike Data example-based query
methods such as `findBy(E example, SingularAttribute<E, ?>... attributes)`.
Implementing example-based queries would require additional effort and since
they are not used in our current projects, we have omitted them.

We welcome contributions from the community to implement this functionality. If
you're interested in contributing, please submit a pull request.
