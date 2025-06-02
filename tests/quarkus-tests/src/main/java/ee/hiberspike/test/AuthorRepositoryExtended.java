package ee.hiberspike.test;

import ee.hiberspike.data.ExtendedEntityRepository;
import org.hibernate.annotations.processing.Find;

import java.util.Optional;

public interface AuthorRepositoryExtended extends ExtendedEntityRepository<Author, String> {

    @Find
    Optional<Author> findBySsn(String ssn);

    @Override
    default Class<Author> getEntityClass() {
        return Author.class;
    }
}
