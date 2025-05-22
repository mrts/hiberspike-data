package ee.hiberspike.test;

import ee.hiberspike.data.EntityRepository;
import org.hibernate.annotations.processing.Find;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends EntityRepository<Author, String> {

    @Find
    Optional<Author> findBySsn(String ssn);

    @Find
    List<Author> findByName(String name);
}
