# HiberSpike Data

**HiberSpike Data** is a small library that provides a modern reimplementation
of the [DeltaSpike Data `EntityRepository`](https://deltaspike.apache.org/documentation/data.html#_the_entityrepository_interface) interface,
built using [Hibernate Data Repositories](https://docs.jboss.org/hibernate/orm/6.6/repositories/html_single/Hibernate_Data_Repositories.html)
and designed for Jakarta EE 10+ applications.


## Why HiberSpike Data?

DeltaSpike Data is no longer actively maintained and doesn’t officially support the `jakarta` namespace and Jakarta EE 9+. HiberSpike Data provides a near drop-in replacement for DeltaSpike Data `EntityRepository` interface for Jakarta EE 10+ applications. It uses stateful sessions and is compatible with Hibernate Envers, lazy loading etc.

## Why not Jakarta Data 1.0?

[Jakarta Data 1.0](https://jakarta.ee/specifications/data/1.0/) uses stateless sessions, which breaks compatibility
with Hibernate Envers and lazy loading, and lacks support for Jakarta Persistance annotations.

These issues will be addressed in the upcoming [Jakarta Data version 1.1](https://jakarta.ee/specifications/data/1.1/), but the 1.1 
specification is still under development.

## What is Hibernate Data Repositories?

[Hibernate Data Repositories](https://docs.jboss.org/hibernate/orm/current/repositories/html_single/Hibernate_Data_Repositories.html)
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
        <groupId>ee.hiberspike</groupId>
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

### 3. Create a `EntityManager` producer

The generated repository implementations use CDI to inject `EntityManager`, so a
`EntityManager` producer method is required.

```java
@RequestScoped
public class EntityManagerProducer {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Produces
    public EntityManager entityManager() {
        return em;
    }
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

### 1. Replace DeltaSpike dependency with HiberSpike Data in pom.xml

Remove `<dependency> ... deltaspike-data-* ... </dependency>` and add
HiberSpike and Hibernate dependencies to `pom.xml` as described in *How to use
HiberSpike Data* above.

### 2. Add `hibernate-jpamodelgen` annotation processor to Maven compiler plugin

Add the `hibernate-jpamodelgen` plugin to `pom.xml` under
`maven-compiler-plugin` configuration as described in *How to use HiberSpike
Data* above.

### 3. Create a `EntityManager` producer

Create a `EntityManager` producer method as described in *How to use HiberSpike
Data* above.

### 4. Remove DeltaSpike properties file

Delete `src/main/resources/META-INF/apache-deltaspike.properties`.

### 5. Update repository imports

Replace `import org.apache.deltaspike.data.api.EntityRepository;` with `import
ee.hiberspike.data.EntityRepository;`.

### 6. Remove all DeltaSpike `@Repository` annotations

HiberSpike Data uses plain repository intefaces, remove the DeltaSpike
`@Repository` annotation from all your repository interfaces.

### 7. Replace `@Query` with `@HSQL`

Replace DeltaSpike’s `@Query` annotation with Hibernate Data Repositories’
`@HSQL` annotation from the `org.hibernate.annotations.processing` package,
which serves a similar purpose:

```java
// Old
@Query("SELECT b FROM Book b WHERE b.title = ?1")
Book findByTitle(String title);

// New
@HSQL("SELECT b FROM Book b WHERE b.title = ?1")
Book findByTitle(String title);
```

### 8. Annotate all DeltaSpike naming-based queries with `@Find`

Annotate DeltaSpike’s naming-based query methods with Hibernate Data
Repositories’ `@Find` annotation from the
`org.hibernate.annotations.processing` package, which serves a similar purpose:

```java
// Old
Book findByTitle(String title);

// New
@Find
Book findByTitle(String title);
```

At least one method with either `@HQL`, `@SQL` or `@Find` annotation is
required in a repository interface to trigger annotation processing.

### 9. Pagination: replace `@FirstResult` and `@MaxResults` with `Page`

Hibernate Data Repositories use `org.hibernate.query.Page` for paging, replace
`@FirstResult` and `@MaxResults` with `Page`:

```java
// Old
Book findByAuthor(String author, @FirstResult int firstResult, @MaxResults int maxResults);

// New
@Find
Book findByAuthor(String author, Page page);
```

## Why doesn't HiberSpike `EntityRepository` include `count()` and `findBy()`?

HiberSpike Data `EntityRepository` does not include `count()`, `findBy()` or
`findOptionalBy()` that are present in DeltaSpike's `EntityRepository`. This is
intentional and avoids imposing certain implementation constraints on all
repositories. Instead, HiberSpike Data provides two separate repository
interfaces, `EntityCountRepository` and `EntityWithIdRepository`, that provide
these methods at the expense of specific limitations. By separating these
methods into distinct interfaces, HiberSpike Data allows you to opt in to
additional features, accepting their extra complexity, only when needed.

A generic `count()` method requires access to the entity’s class name at
runtime, but due to Java’s generic type erasure, this information is not easily
available. To work around this, `EntityCountRepository` requires you to
override the `getEntityClass()` method, which should simply return the entity
class:

```java
public interface BookRepository extends EntityCountRepository<Book, Long> {

    @Find
    Book findByTitle(String title);


    @Override
    protected Class<Book> getEntityClass() {
        return Book.class;
    }

}
```

The `EntityWithIdRepository` interface provides `E findBy(PK id)` and
`Optional<E> findOptionalBy(PK id)`, but these methods assume your entity’s
primary key field is named `id`. This limitation comes from how
`hibernate-jpamodelgen` assembles queries based on method parameter names.

If your entity uses a different field name for the primary key, code generation
fails.  By not including `findBy()` and `findOptionalBy()` in the base
`EntityRepository`, HiberSpike Data avoids making assumptions about your entity
structure.

## Example-based query API is not yet implemented

HiberSpike Data does not currently support DeltaSpike Data example-based query
methods such as `findBy(E example, SingularAttribute<E, ?>... attributes)`.
Implementing example-based queries would require additional effort and since
they are not used in our current projects, we have omitted them.

We welcome contributions from the community to implement this functionality. If
you're interested in contributing, please submit a pull request.
