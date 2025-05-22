package ee.hiberspike.test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class AuthorCountRepositoryTest {

    @Inject
    AuthorCountRepository authorCountRepository;

    @Test
    void count_returnsCorrectNumberOfAuthors() {
        Long authorsCount = authorCountRepository.count();
        assertEquals(2, authorsCount);
    }

}