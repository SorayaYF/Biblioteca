package br.com.trier.biblioteca.services.impl;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.biblioteca.domain.Book;
import br.com.trier.biblioteca.domain.Reservation;
import br.com.trier.biblioteca.domain.User;
import br.com.trier.biblioteca.repositories.ReservationRepository;
import br.com.trier.biblioteca.services.ReservationService;
import br.com.trier.biblioteca.services.exceptions.ObjectNotFound;
import br.com.trier.biblioteca.utils.DateUtils;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private ReservationRepository repository;

	@Override
	public Reservation findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("A reserva %s não existe".formatted(id)));
	}

	@Override
	public Reservation insert(Reservation reservation) {
		return repository.save(reservation);
	}

	@Override
	public List<Reservation> listAll() {
		List<Reservation> lista = repository.findAll();
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma reserva cadastrada");
		}
		return lista;
	}

	@Override
	public Reservation update(Reservation reservation) {
		findById(reservation.getId());
		return repository.save(reservation);
	}

	@Override
	public void delete(Integer id) {
		repository.delete(findById(id));
	}

	@Override
	public List<Reservation> findByDate(String date) {
		ZonedDateTime zonedDate = DateUtils.strToZonedDateTime(date);

		List<Reservation> lista = repository.findByDate(zonedDate);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma reserva cadastrada na data %s".formatted(date));
		}
		return lista;
	}

	@Override
	public List<Reservation> findByDateBetween(String dateIn, String dateFin) {
		ZonedDateTime zonedDateIn = DateUtils.strToZonedDateTime(dateIn);
		ZonedDateTime zonedDateFin = DateUtils.strToZonedDateTime(dateFin);

		List<Reservation> lista = repository.findByDateBetween(zonedDateIn, zonedDateFin);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma reserva cadastrada entre as datas %s e %s".formatted(dateIn, dateFin));
		}
		return lista;
	}

	@Override
	public List<Reservation> findByBook(Book book) {
		List<Reservation> lista = repository.findByBook(book);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma reserva cadastrada para o livro com ID %s".formatted(book.getId()));
		}
		return lista;
	}

	@Override
	public List<Reservation> findByUser(User user) {
		List<Reservation> lista = repository.findByUser(user);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma reserva cadastrada para o usuário com ID %s".formatted(user.getId()));
		}
		return lista;
	}
}
