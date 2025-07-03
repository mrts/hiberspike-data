package org.example.modulea;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.annotation.sql.DataSourceDefinition;
import org.hibernate.Session;

@DataSourceDefinition(
        name = "java:/jdbc/DSA",
        className = "org.h2.jdbcx.JdbcDataSource",
        url = "jdbc:h2:mem:dsa;DB_CLOSE_DELAY=-1",
        user = "sa",
        password = "sa"
)
@ApplicationScoped
public class EmProducerA {

    @PersistenceContext(unitName = "pu-a", type = PersistenceContextType.EXTENDED)
    EntityManager em;

    @Produces
    @FooAQualifier
    Session fooASession() {
        return em.unwrap(Session.class);
    }
}
