package br.com.trier.biblioteca.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.biblioteca.BaseTests;
import br.com.trier.biblioteca.domain.Author;
import br.com.trier.biblioteca.services.exceptions.IntegrityViolation;
import br.com.trier.biblioteca.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
class AuthorServiceTest extends BaseTests{


	@Autowired
	AuthorService authorService;

	@Test
	@DisplayName("Buscar por id")
	@Sql({ "classpath:/resources/sqls/autor.sql" })
	void findIdValid() {
		Author autor = authorService.findById(1);
		assertNotNull(autor);
		assertEquals(1, autor.getId());
		assertEquals("Colleen Hoover", autor.getName());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	@Sql({ "classpath:/resources/sqls/autor.sql" })
	void findIdInvalid() {
		var ex = assertThrows(ObjectNotFound.class, () -> authorService.findById(10));
		assertEquals("O autor 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Listar Todos")
	@Sql({ "classpath:/resources/sqls/autor.sql" })
	void listAll() {
		assertEquals(3, authorService.listAll().size());
	}

	@Test
	@DisplayName("Listar Todos sem cadastro")
	void listAllEmpty() {
		var ex = assertThrows(ObjectNotFound.class, () -> authorService.listAll());
		assertEquals("Nenhum autor cadastrado", ex.getMessage());
	}

	@Test
	@DisplayName("Cadastrar autor")
	void insert() {
		Author autor = new Author(null, "Elayne Baeta");
		authorService.insert(autor);
		assertEquals(1, authorService.listAll().size());
		assertEquals(1, autor.getId());
		assertEquals("Elayne Baeta", autor.getName());
	}

	@Test
	@DisplayName("Cadastrar autor com nome duplicado")
	@Sql({ "classpath:/resources/sqls/autor.sql" })
	void insertWithSameName() {
		Author autor = new Author(null, "Colleen Hoover");
		var ex = assertThrows(IntegrityViolation.class, () -> authorService.insert(autor));
		assertEquals("Nome já cadastrado: Colleen Hoover", ex.getMessage());
	}

	@Test
	@DisplayName("Alterar autor")
	@Sql({ "classpath:/resources/sqls/autor.sql" })
	void update() {
		Author autor = authorService.findById(1);
		assertNotNull(autor);
		assertEquals(1, autor.getId());
		assertEquals("Colleen Hoover", autor.getName());
		autor = new Author(1, "Elayne Baeta");
		authorService.update(autor);
		assertEquals(3, authorService.listAll().size());
		assertEquals(1, autor.getId());
		assertEquals("Elayne Baeta", autor.getName());
	}

	@Test
	@DisplayName("Alterar autor inexistente")
	@Sql({ "classpath:/resources/sqls/autor.sql" })
	void updateInvalid() {
		Author autor = new Author(10, "Elayne Baeta");
		var ex = assertThrows(ObjectNotFound.class, () -> authorService.update(autor));
		assertEquals("O autor 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir autor")
	@Sql({ "classpath:/resources/sqls/autor.sql" })
	void delete() {
		assertEquals(3, authorService.listAll().size());
		authorService.delete(1);
		assertEquals(2, authorService.listAll().size());
		assertEquals(3, authorService.listAll().get(0).getId());
	}

	@Test
	@DisplayName("Excluir autor inexistente")
	@Sql({ "classpath:/resources/sqls/autor.sql" })
	void deleteNonexistent() {
		assertEquals(3, authorService.listAll().size());
		var ex = assertThrows(ObjectNotFound.class, () -> authorService.delete(10));
		assertEquals("O autor 10 não existe", ex.getMessage());
		assertEquals(3, authorService.listAll().size());
		assertEquals(1, authorService.listAll().get(0).getId());
	}

	@Test
	@DisplayName("Procurar por nome")
	@Sql({ "classpath:/resources/sqls/autor.sql" })
	void findByName() {
		assertEquals(1, authorService.findByNameStartsWithIgnoreCase("c").size());
		var ex = assertThrows(ObjectNotFound.class, () -> authorService.findByNameStartsWithIgnoreCase("x"));
		assertEquals("Nenhum autor inicia com x", ex.getMessage());

	}
}
