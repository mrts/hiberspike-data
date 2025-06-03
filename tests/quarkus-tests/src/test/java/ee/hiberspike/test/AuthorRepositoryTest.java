package ee.hiberspike.test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Transactional
    @Test
    public void getPrimaryKey_ReturnsNull_ForNewEntityWithObjectPrimaryKey() {
        var author = new Author();
        assertNull(author.ssn);
        assertNull(authorRepository.getPrimaryKey(author));

        author = authorRepository.saveAndFlush(author);

        assertNotNull(authorRepository.getPrimaryKey(author));
    }

}