package br.com.trier.biblioteca.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.biblioteca.domain.Book;
import br.com.trier.biblioteca.domain.Reservation;
import br.com.trier.biblioteca.domain.User;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	
	List<Reservation> findByDate(ZonedDateTime date);

	List<Reservation> findByDateBetween(ZonedDateTime dateIn, ZonedDateTime dateFin);

	List<Reservation> findByBook(Book book);

	List<Reservation> findByUser(User user);


}
