package ee.hiberspike.test;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.envers.Audited;

@Audited
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String ssn;

    public String name;
}