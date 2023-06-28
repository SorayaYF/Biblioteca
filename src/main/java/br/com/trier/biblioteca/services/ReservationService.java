package br.com.trier.biblioteca.services;

import java.util.List;

import br.com.trier.biblioteca.domain.Book;
import br.com.trier.biblioteca.domain.Reservation;
import br.com.trier.biblioteca.domain.User;

public interface ReservationService {
	
	Reservation findById(Integer id);
	
	Reservation insert(Reservation reservation);
	
	List<Reservation> listAll();
	
	Reservation update(Reservation reservation);
	
	void delete(Integer id);
	
	List<Reservation> findByDate(String date);
	
	List<Reservation> findByDateBetween(String dateIn, String dateFin);
	
	List<Reservation> findByBook(Book book);
	
	List<Reservation> findByUser(User user);
}