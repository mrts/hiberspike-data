package ee.hiberspike.test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class TaskRepositoryExtendedTest {

    @Inject
    TaskRepositoryExtended taskRepository;

    @Test
    void find_returnsNull_whenIdDoesNotExist() {
        Task task = taskRepository.findBy(12345L);
        assertNull(task);
    }

    @Test
    void findOptional_returnsEmptyOptional_whenIdDoesNotExist() {
        Optional<Task> task = taskRepository.findOptionalBy(12345L);
        assertTrue(task.isEmpty());
    }

    @Test
    void find_returnsTask_whenIdExists() {
        Task task = taskRepository.findBy(1L);
        assertNotNull(task);
        assertEquals("Example task", task.description);
    }

    @Test
    void findOptional_returnsTask_whenIdExists() {
        Optional<Task> task = taskRepository.findOptionalBy(1L);
        assertFalse(task.isEmpty());
        assertEquals("Example task", task.get().description);
    }
}