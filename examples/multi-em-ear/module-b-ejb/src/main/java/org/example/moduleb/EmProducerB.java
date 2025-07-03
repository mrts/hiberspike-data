package org.example.moduleb;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.annotation.sql.DataSourceDefinition;
import org.hibernate.Session;

@DataSourceDefinition(
        name = "java:/jdbc/DSB",
        className = "org.h2.jdbcx.JdbcDataSource",
        url = "jdbc:h2:mem:dsb;DB_CLOSE_DELAY=-1",
        user = "sa",
        password = "sa"
)
@ApplicationScoped
public class EmProducerB {

    @PersistenceContext(unitName = "pu-b", type = PersistenceContextType.EXTENDED)
    EntityManager em;

    @Produces
    @FooBQualifier
    Session fooBSession() {
        return em.unwrap(Session.class);
    }
}
