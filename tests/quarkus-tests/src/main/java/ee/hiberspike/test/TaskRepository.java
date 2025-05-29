package ee.hiberspike.test;

import ee.hiberspike.data.EntityRepository;
import org.hibernate.annotations.processing.Find;

import java.util.Optional;

public interface TaskRepository extends EntityRepository<Task, Long> {
    @Find
    Optional<Task> findByDescription(String description);
}
