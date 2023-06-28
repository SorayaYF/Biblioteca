package br.com.trier.biblioteca.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.biblioteca.BaseTests;
import br.com.trier.biblioteca.domain.PublishingCompany;
import br.com.trier.biblioteca.services.exceptions.IntegrityViolation;
import br.com.trier.biblioteca.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
class PublishingCompanyServiceTest extends BaseTests{


	@Autowired
	PublishingCompanyService publishingCompanyService;

	@Test
	@DisplayName("Buscar por id")
	@Sql({ "classpath:/resources/sqls/editora.sql" })
	void findIdValid() {
		PublishingCompany editora = publishingCompanyService.findById(1);
		assertNotNull(editora);
		assertEquals(1, editora.getId());
		assertEquals("Galera", editora.getName());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	@Sql({ "classpath:/resources/sqls/editora.sql" })
	void findIdInvalid() {
		var ex = assertThrows(ObjectNotFound.class, () -> publishingCompanyService.findById(10));
		assertEquals("A editora 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Listar Todos")
	@Sql({ "classpath:/resources/sqls/editora.sql" })
	void listAll() {
		assertEquals(2, publishingCompanyService.listAll().size());
	}

	@Test
	@DisplayName("Listar Todos sem cadastro")
	void listAllEmpty() {
		var ex = assertThrows(ObjectNotFound.class, () -> publishingCompanyService.listAll());
		assertEquals("Nenhuma editora cadastrada", ex.getMessage());
	}

	@Test
	@DisplayName("Cadastrar editora")
	void insert() {
		PublishingCompany editora = new PublishingCompany(null, "Intrínseca");
		publishingCompanyService.insert(editora);
		assertEquals(1, publishingCompanyService.listAll().size());
		assertEquals(1, editora.getId());
		assertEquals("Intrínseca", editora.getName());
	}

	@Test
	@DisplayName("Cadastrar editora com nome duplicado")
	@Sql({ "classpath:/resources/sqls/editora.sql" })
	void insertWithSameName() {
		PublishingCompany editora = new PublishingCompany(null, "Galera");
		var ex = assertThrows(IntegrityViolation.class, () -> publishingCompanyService.insert(editora));
		assertEquals("Nome já cadastrado: Brasil", ex.getMessage());
	}

	@Test
	@DisplayName("Alterar editora")
	@Sql({ "classpath:/resources/sqls/editora.sql" })
	void update() {
		PublishingCompany editora = publishingCompanyService.findById(1);
		assertNotNull(editora);
		assertEquals(1, editora.getId());
		assertEquals("Galera", editora.getName());
		editora = new PublishingCompany(1, "Intrínseca");
		publishingCompanyService.update(editora);
		assertEquals(2, publishingCompanyService.listAll().size());
		assertEquals(1, editora.getId());
		assertEquals("Intrínseca", editora.getName());
	}

	@Test
	@DisplayName("Alterar editora inexistente")
	@Sql({ "classpath:/resources/sqls/editora.sql" })
	void updateInvalid() {
		PublishingCompany editora = new PublishingCompany(10, "Intrínseca");
		var ex = assertThrows(ObjectNotFound.class, () -> publishingCompanyService.update(editora));
		assertEquals("A editora 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir editora")
	@Sql({ "classpath:/resources/sqls/editora.sql" })
	void delete() {
		assertEquals(2, publishingCompanyService.listAll().size());
		publishingCompanyService.delete(1);
		assertEquals(1, publishingCompanyService.listAll().size());
		assertEquals(2, publishingCompanyService.listAll().get(0).getId());
	}

	@Test
	@DisplayName("Excluir editora inexistente")
	@Sql({ "classpath:/resources/sqls/editora.sql" })
	void deleteNonexistent() {
		assertEquals(2, publishingCompanyService.listAll().size());
		var ex = assertThrows(ObjectNotFound.class, () -> publishingCompanyService.delete(10));
		assertEquals("A editora 10 não existe", ex.getMessage());
		assertEquals(2, publishingCompanyService.listAll().size());
		assertEquals(1, publishingCompanyService.listAll().get(0).getId());
	}

	@Test
	@DisplayName("Procurar por nome")
	@Sql({ "classpath:/resources/sqls/editora.sql" })
	void findByName() {
		assertEquals(1, publishingCompanyService.findByNameStartsWithIgnoreCase("g").size());
		var ex = assertThrows(ObjectNotFound.class, () -> publishingCompanyService.findByNameStartsWithIgnoreCase("x"));
		assertEquals("Nenhuma editora inicia com x", ex.getMessage());

	}

}
