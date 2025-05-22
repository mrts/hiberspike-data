package ee.hiberspike.test;

import ee.hiberspike.data.EntityCountRepository;
import org.hibernate.annotations.processing.Find;

import java.util.Optional;

public interface AuthorCountRepository extends EntityCountRepository<Author, String> {

    @Find
    Optional<Author> findBySsn(String ssn);

    @Override
    default Class<Author> getEntityClass() {
        return Author.class;
    }
}
