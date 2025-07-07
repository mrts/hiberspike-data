package org.example.modulea;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataSourceDefinition(
        name = "java:/jdbc/DSA",
        className = "org.h2.jdbcx.JdbcDataSource",
        url = "jdbc:h2:mem:dsa;DB_CLOSE_DELAY=-1",
        user = "sa",
        password = "sa"
)
@Dependent
public class EmProducerA {
    @Produces
    @Dependent
    @FooAQualifier
    @PersistenceContext(unitName = "pu-a")
    EntityManager em;
}
