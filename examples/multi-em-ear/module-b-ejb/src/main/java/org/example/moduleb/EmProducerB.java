package org.example.moduleb;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataSourceDefinition(
        name = "java:/jdbc/DSB",
        className = "org.h2.jdbcx.JdbcDataSource",
        url = "jdbc:h2:mem:dsb;DB_CLOSE_DELAY=-1",
        user = "sa",
        password = "sa"
)
@Dependent
public class EmProducerB {
    @Produces
    @Dependent
    @FooBQualifier
    @PersistenceContext(unitName = "pu-b")
    EntityManager em;
}
