package br.com.trier.biblioteca.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.trier.biblioteca.BaseTests;
import br.com.trier.biblioteca.domain.Book;
import br.com.trier.biblioteca.domain.Loan;
import br.com.trier.biblioteca.domain.User;
import br.com.trier.biblioteca.services.exceptions.ObjectNotFound;
import br.com.trier.biblioteca.utils.DateUtils;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/livro.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/usuario.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/emprestimo.sql")
class LoanServiceTest extends BaseTests {

	@Autowired
	LoanService service;

	@Autowired
	BookService bookService;

	@Autowired
	UserService userService;

	@Test
	@DisplayName("Buscar por id")
	void findByIdValid() {
		Loan loan = service.findById(1);
		assertNotNull(loan);
		assertEquals(1, loan.getId());
		assertEquals("28/06/2023", loan.getLoanDate());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	void findByIdInvalid() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.findById(10));
		assertEquals("O empréstimo 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Listar todos")
	void listAll() {
		assertEquals(2, service.listAll().size());
	}

	@Test
	@DisplayName("Listar todos sem cadastro")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	void listAllEmpty() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.listAll());
		assertEquals("Nenhum empréstimo cadastrado", ex.getMessage());
	}

	@Test
	@DisplayName("Cadastrar empréstimo")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/livro.sql" })
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void insert() {
		Book book = bookService.findById(1);
		User user = userService.findById(1);

		Loan loan = new Loan(null, DateUtils.strToZonedDateTime("01/07/2023"), DateUtils.strToZonedDateTime("05/07/2023"), book, user);
		service.insert(loan);

		assertEquals(1, service.listAll().size());
		assertEquals(1, loan.getId());
		assertEquals("01/07/2023", loan.getLoanDate());
		assertEquals(null, loan.getReturnDate());
		assertEquals(book, loan.getBook());
		assertEquals(user, loan.getUser());
	}

	@Test
	@DisplayName("Alterar empréstimo")
	void update() {
		Book book = bookService.findById(1);
		User user = userService.findById(1);

		Loan loan = new Loan(1, DateUtils.strToZonedDateTime("01/07/2023"), DateUtils.strToZonedDateTime("05/07/2023"), book, user);
		service.update(loan);

		assertEquals(2, service.listAll().size());
		assertEquals(1, loan.getId());
		assertEquals("01/07/2023", loan.getLoanDate());
		assertEquals("05/07/2023", loan.getReturnDate());
		assertEquals(book, loan.getBook());
		assertEquals(user, loan.getUser());
	}

	@Test
	@DisplayName("Alterar empréstimo inexistente")
	void updateInvalid() {
		Book book = bookService.findById(1);
		User user = userService.findById(1);

		Loan loan = new Loan(10, DateUtils.strToZonedDateTime("01/07/2023"), DateUtils.strToZonedDateTime("05/07/2023"), book, user);

		var ex = assertThrows(ObjectNotFound.class, () -> service.update(loan));
		assertEquals("O empréstimo 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir empréstimo")
	void delete() {
		Loan loan = service.findById(1);
		assertNotNull(loan);
		assertEquals(1, loan.getId());
		assertEquals("28/06/2023", loan.getLoanDate());

		service.delete(1);

		var ex = assertThrows(ObjectNotFound.class, () -> service.findById(1));
		assertEquals("O empréstimo 1 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir empréstimo inexistente")
	void deleteInvalid() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.delete(10));
		assertEquals("O empréstimo 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Encontrar por data de empréstimo")
	void findByLoanDate() {
		List<Loan> loans = service.findByLoanDate("28/06/2023");
		assertEquals(1, loans.size());
	}

	@Test
	@DisplayName("Encontrar por data de empréstimo inexistente")
	void findByLoanDateNonExist() {
		List<Loan> loans = service.findByLoanDate("28/06/2023");
		assertEquals(1, loans.size());

		var ex = assertThrows(ObjectNotFound.class, () -> service.findByLoanDate("30/06/2023"));
		assertEquals("Nenhum empréstimo cadastrado na data de empréstimo 30/06/2023", ex.getMessage());
	}

	@Test
	@DisplayName("Encontrar por data de devolução")
	void findByReturnDate() {
		List<Loan> loans = service.findByReturnDate("05/07/2023");
		assertEquals(1, loans.size());
	}

	@Test
	@DisplayName("Encontrar por data de devolução inexistente")
	void findByReturnDateNonExist() {
		List<Loan> loans = service.findByReturnDate("05/07/2023");
		assertEquals(1, loans.size());

		var ex = assertThrows(ObjectNotFound.class, () -> service.findByReturnDate("10/07/2023"));
		assertEquals("Nenhum empréstimo cadastrado na data de devolução 10/07/2023", ex.getMessage());
	}

	@Test
	@DisplayName("Encontrar por período de data de empréstimo")
	void findByLoanDateBetween() {
		List<Loan> loans = service.findByLoanDateBetween("28/06/2023", "02/07/2023");
		assertEquals(2, loans.size());
	}

	@Test
	@DisplayName("Encontrar por período de data de empréstimo inexistente")
	void findByLoanDateBetweenNonExist() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.findByLoanDateBetween("01/07/2023", "10/07/2023"));
		assertEquals("Nenhum empréstimo cadastrado no período de empréstimo de 01/07/2023 até 10/07/2023", ex.getMessage());
	}

	@Test
	@DisplayName("Encontrar por livro")
	void findByBook() {
		Book book = bookService.findById(1);
		List<Loan> loans = service.findByBook(book);
		assertEquals(1, loans.size());
	}

	@Test
	@DisplayName("Encontrar por livro inexistente")
	void findByBookNonExist() {
		Book book = new Book();
		book.setId(10);
		List<Loan> loans = service.findByBook(book);
		assertEquals(0, loans.size());
	}

	@Test
	@DisplayName("Encontrar por usuário")
	void findByUser() {
		User user = userService.findById(1);
		List<Loan> loans = service.findByUser(user);
		assertEquals(2, loans.size());
	}

	@Test
	@DisplayName("Encontrar por usuário inexistente")
	void findByUserNonExist() {
		User user = new User();
		user.setId(10);
		List<Loan> loans = service.findByUser(user);
		assertEquals(0, loans.size());
	}
}
