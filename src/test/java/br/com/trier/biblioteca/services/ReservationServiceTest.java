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
import br.com.trier.biblioteca.domain.Reservation;
import br.com.trier.biblioteca.domain.User;
import br.com.trier.biblioteca.services.exceptions.ObjectNotFound;
import br.com.trier.biblioteca.utils.DateUtils;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/autor.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/editora.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/livro.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/usuario.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/reserva.sql")
class ReservationServiceTest extends BaseTests {

	@Autowired
	ReservationService service;

	@Autowired
	BookService bookService;

	@Autowired
	UserService userService;

	@Test
	@DisplayName("Buscar por id")
	void findByIdValid() {
		Reservation reservation = service.findById(1);
		assertNotNull(reservation);
		assertEquals(1, reservation.getId());
		assertEquals(2023, reservation.getDate().getYear());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	void findByIdInvalid() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.findById(10));
		assertEquals("A reserva 10 não existe", ex.getMessage());
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
		assertEquals("Nenhuma reserva cadastrada", ex.getMessage());
	}

	@Test
	@DisplayName("Cadastrar reserva")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/livro.sql" })
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void insert() {
		Book book = bookService.findById(1);
		User user = userService.findById(1);
		Reservation reservation = new Reservation(null, DateUtils.strToZonedDateTime("01/07/2023"), book, user);
		service.insert(reservation);

		assertEquals(1, service.listAll().size());
		assertEquals(1, reservation.getId());
		assertEquals("01/07/2023", reservation.getDate());
		assertEquals(book, reservation.getBook());
		assertEquals(user, reservation.getUser());
	}

	@Test
	@DisplayName("Alterar reserva")
	void update() {
		Book book = bookService.findById(1);
		User user = userService.findById(1);

		Reservation reservation = new Reservation(1, DateUtils.strToZonedDateTime("01/07/2023"), book, user);
		service.update(reservation);

		assertEquals(2, service.listAll().size());
		assertEquals(1, reservation.getId());
		assertEquals(01, reservation.getDate().getDayOfMonth());
		assertEquals(book, reservation.getBook());
		assertEquals(user, reservation.getUser());
	}

	@Test
	@DisplayName("Alterar reserva inexistente")
	void updateInvalid() {
		Book book = bookService.findById(1);
		User user = userService.findById(1);

		Reservation reservation = new Reservation(10, DateUtils.strToZonedDateTime("01/07/2023"), book, user);

		var ex = assertThrows(ObjectNotFound.class, () -> service.update(reservation));
		assertEquals("A reserva 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir reserva")
	void delete() {
		Reservation reservation = service.findById(1);
		assertNotNull(reservation);
		assertEquals(1, reservation.getId());
		assertEquals(28, reservation.getDate().getDayOfMonth());

		service.delete(1);

		var ex = assertThrows(ObjectNotFound.class, () -> service.findById(1));
		assertEquals("A reserva 1 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir reserva inexistente")
	void deleteInvalid() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.delete(10));
		assertEquals("A reserva 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Encontrar por data")
	void findByDate() {
		List<Reservation> reservations = service.findByDate("28/06/2023");
		assertEquals(1, reservations.size());
	}

	@Test
	@DisplayName("Encontrar por data inexistente")
	void findByDateNonExist() {
		List<Reservation> reservations = service.findByDate("28/06/2023");
		assertEquals(1, reservations.size());

		var ex = assertThrows(ObjectNotFound.class, () -> service.findByDate("30/06/2023"));
		assertEquals("Nenhuma reserva cadastrada na data 30/06/2023", ex.getMessage());
	}

	@Test
	@DisplayName("Encontrar por período de data")
	void findByDateBetween() {
		List<Reservation> reservations = service.findByDateBetween("28/06/2023", "02/07/2023");
		assertEquals(2, reservations.size());
	}

	@Test
	@DisplayName("Encontrar por período de data inexistente")
	void findByDateBetweenNonExist() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.findByDateBetween("01/07/2023", "10/07/2023"));
		assertEquals("Nenhuma reserva cadastrada entre as datas 01/07/2023 e 10/07/2023", ex.getMessage());
	}

	@Test
	@DisplayName("Encontrar por livro")
	void findByBook() {
		Book book = bookService.findById(1);
		List<Reservation> reservations = service.findByBook(book);
		assertEquals(1, reservations.size());
		var ex = assertThrows(ObjectNotFound.class, () -> service.findByBook(bookService.findById(3)));
		assertEquals("Nenhuma reserva cadastrada para o livro com ID 3", ex.getMessage());
	}

	@Test
	@DisplayName("Encontrar por usuário")
	void findByUser() {
		User user = userService.findById(1);
		List<Reservation> reservations = service.findByUser(user);
		assertEquals(2, reservations.size());
		var ex = assertThrows(ObjectNotFound.class, () -> service.findByUser(userService.findById(2)));
		assertEquals("Nenhuma reserva cadastrada para o usuário com ID 2", ex.getMessage());
	}
}
