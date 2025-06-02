package ee.hiberspike.test;

import ee.hiberspike.data.ExtendedEntityRepository;
import org.hibernate.annotations.processing.Find;

import java.util.Optional;

public interface TaskRepositoryExtended extends ExtendedEntityRepository<Task, Long> {

    @Find
    Optional<Task> findByDescription(String description);

    @Override
    default Class<Task> getEntityClass() {
        return Task.class;
    }
}
