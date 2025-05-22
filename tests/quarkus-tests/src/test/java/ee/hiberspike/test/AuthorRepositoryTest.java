package ee.hiberspike.test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class AuthorRepositoryTest {

    @Inject
    AuthorRepository authorRepository;

    @Test
    void findByName_returnsAuthor_whenNameExists() {
        List<Author> authors = authorRepository.findByName("Gavin King");
        assertEquals(1, authors.size());
        assertEquals("Gavin King", authors.get(0).name);
    }

}