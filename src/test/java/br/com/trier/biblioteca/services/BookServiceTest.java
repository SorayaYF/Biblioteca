package br.com.trier.biblioteca.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.trier.biblioteca.domain.Author;
import br.com.trier.biblioteca.domain.Book;
import br.com.trier.biblioteca.domain.PublishingCompany;
import br.com.trier.biblioteca.enuns.Genre;
import br.com.trier.biblioteca.services.exceptions.ObjectNotFound;

@SpringBootTest
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/limpa_tabelas.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/autor.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/editora.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/livro.sql")
class BookServiceTest {

    @Autowired
    BookService bookService;

    @Autowired
    AuthorService authorService;

    @Autowired
    PublishingCompanyService publishingCompanyService;

    @Test
    @DisplayName("Buscar por ID")
    void findById() {
        Book book = bookService.findById(1);
        assertNotNull(book);
        assertEquals(1, book.getId());
        assertEquals("Livro 1", book.getTitle());
    }

    @Test
    @DisplayName("Buscar por ID inexistente")
    void findByIdInvalid() {
        assertThrows(ObjectNotFound.class, () -> bookService.findById(10));
    }

    @Test
    @DisplayName("Listar Todos")
    void listAll() {
        List<Book> books = bookService.listAll();
        assertEquals(3, books.size());
    }

    @Test
    @DisplayName("Listar Todos sem cadastro")
    @Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
    void listAllEmpty() {
        assertThrows(ObjectNotFound.class, () -> bookService.listAll());
    }

    @Test
    @DisplayName("Inserir livro")
    @Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
    @Sql({ "classpath:/resources/sqls/autor.sql" })
    @Sql({ "classpath:/resources/sqls/editora.sql" })
    void insert() {
        Author author = authorService.findById(1);
        PublishingCompany publishingCompany = publishingCompanyService.findById(1);
        Book book = new Book(null, "Livro 3", "ISBN123456", Genre.Romance, author, publishingCompany);
        book = bookService.insert(book);
        assertNotNull(book);
        assertNotNull(book.getId());
        assertEquals("Livro 3", book.getTitle());
    }

    @Test
    @DisplayName("Atualizar livro")
    void update() {
        Author author = authorService.findById(1);
        PublishingCompany publishingCompany = publishingCompanyService.findById(1);
        Book book = new Book(1, "Livro Atualizado", "ISBN123456", Genre.Romance, author, publishingCompany);
        book = bookService.update(book);
        assertEquals("Livro Atualizado", book.getTitle());
    }

    @Test
    @DisplayName("Atualizar livro inexistente")
    void updateInvalid() {
        Author author = authorService.findById(1);
        PublishingCompany publishingCompany = publishingCompanyService.findById(1);
        Book book = new Book(10, "Livro 10", "ISBN123456", Genre.Acao, author, publishingCompany);
        assertThrows(ObjectNotFound.class, () -> bookService.update(book));
    }

    @Test
    @DisplayName("Excluir livro")
    void delete() {
        bookService.delete(1);
        assertThrows(ObjectNotFound.class, () -> bookService.findById(1));
    }

    @Test
    @DisplayName("Excluir livro inexistente")
    void deleteNonexistent() {
        assertThrows(ObjectNotFound.class, () -> bookService.delete(10));
    }

    @Test
    @DisplayName("Procurar livro por título")
    void findByTitle() {
		assertEquals(3, bookService.findByTitleStartsWithIgnoreCase("l").size());
		var ex = assertThrows(ObjectNotFound.class, () -> bookService.findByTitleStartsWithIgnoreCase("x"));
		assertEquals("Nenhum livro encontrado com título iniciando por x", ex.getMessage());

    }

    @Test
    @DisplayName("Procurar livro por gênero")
    void findByGenre() {
    	assertEquals(1, bookService.findByGenre(Genre.Romance).size());
    	var ex = assertThrows(ObjectNotFound.class, () -> bookService.findByGenre(Genre.Acao));
    	assertEquals("Nenhum livro encontrado com o gênero Acao", ex.getMessage());
    }

    @Test
    @DisplayName("Procurar livro por autor")
    void findByAuthor() {
        Author author = authorService.findById(1);
    	assertEquals(2, bookService.findByAuthor(author).size());
    	var ex = assertThrows(ObjectNotFound.class, () -> bookService.findByAuthor(authorService.findById(3)));
    	assertEquals("Nenhum livro encontrado do autor Sergio", ex.getMessage());
    }

    @Test
    @DisplayName("Procurar livro por editora")
    void findByPublishingCompany() {
        PublishingCompany publishingCompany = publishingCompanyService.findById(1);
    	assertEquals(1, bookService.findByPublishingCompany(publishingCompany).size());
    	var ex = assertThrows(ObjectNotFound.class, () -> bookService.findByPublishingCompany(publishingCompanyService.findById(3)));
    	assertEquals("Nenhum livro encontrado da editora QueroTudoQueESeu", ex.getMessage());
    }
}
