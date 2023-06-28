package br.com.trier.biblioteca.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.trier.biblioteca.domain.Book;
import br.com.trier.biblioteca.domain.Reservation;
import br.com.trier.biblioteca.domain.User;
import br.com.trier.biblioteca.services.exceptions.ObjectNotFound;

@SpringBootTest
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/limpa_tabelas.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/livro.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/usuario.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/reserva.sql")
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("Buscar por ID")
    void findById() {
        Reservation reservation = reservationService.findById(1);
        assertNotNull(reservation);
        assertEquals(1, reservation.getId());
        assertEquals("2023-01-01T00:00:00Z", reservation.getDate().toString());
    }

    @Test
    @DisplayName("Buscar por ID inexistente")
    void findByIdInvalid() {
        assertThrows(ObjectNotFound.class, () -> reservationService.findById(10));
    }

    @Test
    @DisplayName("Listar Todos")
    void listAll() {
        List<Reservation> reservations = reservationService.listAll();
        assertEquals(2, reservations.size());
    }

    @Test
    @DisplayName("Listar Todos sem cadastro")
    @Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
    void listAllEmpty() {
        assertThrows(ObjectNotFound.class, () -> reservationService.listAll());
    }

    @Test
    @DisplayName("Inserir reserva")
    @Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
    @Sql({ "classpath:/resources/sqls/livro.sql" })
    @Sql({ "classpath:/resources/sqls/usuario.sql" })
    void insert() {
        Book book = bookService.findById(1);
        User user = userService.findById(1);
        ZonedDateTime date = ZonedDateTime.parse("2023-07-01T00:00:00Z");
        Reservation reservation = new Reservation(null, date, book, user);
        reservation = reservationService.insert(reservation);
        assertNotNull(reservation);
        assertNotNull(reservation.getId());
        assertEquals("2023-07-01T00:00:00Z", reservation.getDate().toString());
    }

    @Test
    @DisplayName("Atualizar reserva")
    void update() {
        Book book = bookService.findById(1);
        User user = userService.findById(1);
        ZonedDateTime date = ZonedDateTime.parse("2023-07-01T00:00:00Z");
        Reservation reservation = new Reservation(1, date, book, user);
        reservation = reservationService.update(reservation);
        assertEquals("2023-07-01T00:00:00Z", reservation.getDate().toString());
    }

    @Test
    @DisplayName("Atualizar reserva inexistente")
    void updateInvalid() {
        Book book = bookService.findById(1);
        User user = userService.findById(1);
        ZonedDateTime date = ZonedDateTime.parse("2023-07-01T00:00:00Z");
        Reservation reservation = new Reservation(10, date, book, user);
        assertThrows(ObjectNotFound.class, () -> reservationService.update(reservation));
    }

    @Test
    @DisplayName("Excluir reserva")
    void delete() {
        reservationService.delete(1);
        assertThrows(ObjectNotFound.class, () -> reservationService.findById(1));
    }

    @Test
    @DisplayName("Excluir reserva inexistente")
    void deleteNonexistent() {
        assertThrows(ObjectNotFound.class, () -> reservationService.delete(10));
    }

    @Test
    @DisplayName("Procurar reserva por data")
    void findByDate() {
        List<Reservation> reservations = reservationService.findByDate("2023-01-01T00:00:00Z");
        assertEquals(1, reservations.size());
    }

    @Test
    @DisplayName("Procurar reserva por período de datas")
    void findByDateBetween() {
        List<Reservation> reservations = reservationService.findByDateBetween("2023-01-01T00:00:00Z", "2023-01-31T23:59:59Z");
        assertEquals(1, reservations.size());
    }

    @Test
    @DisplayName("Procurar reserva por livro")
    void findByBook() {
        Book book = bookService.findById(1);
        List<Reservation> reservations = reservationService.findByBook(book);
        assertEquals(1, reservations.size());
    }

    @Test
    @DisplayName("Procurar reserva por usuário")
    void findByUser() {
        User user = userService.findById(1);
        List<Reservation> reservations = reservationService.findByUser(user);
        assertEquals(1, reservations.size());
    }
}

