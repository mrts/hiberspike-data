package ee.hiberspike.test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@QuarkusTest
class TaskRepositoryTest {

    @Inject
    TaskRepository taskRepository;

    @Transactional
    @Test
    public void getPrimaryKey_ReturnsNumberZero_ForNewEntityWithPrimitivePrimaryKey() {
        var task = new Task();
        assertEquals(0L, task.id);
        assertNotNull(taskRepository.getPrimaryKey(task));
        assertEquals(0L, taskRepository.getPrimaryKey(task).longValue());

        task = taskRepository.saveAndFlush(task);

        assertTrue(taskRepository.getPrimaryKey(task) > 0);
    }

}