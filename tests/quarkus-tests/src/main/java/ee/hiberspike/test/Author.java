package ee.hiberspike.test;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.envers.Audited;

@Audited
@Entity
public class Author {
    @Id
    public String ssn;

    public String name;
}