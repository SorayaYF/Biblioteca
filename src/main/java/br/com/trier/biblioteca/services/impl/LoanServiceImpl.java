package br.com.trier.biblioteca.services.impl;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.biblioteca.domain.Book;
import br.com.trier.biblioteca.domain.Loan;
import br.com.trier.biblioteca.domain.User;
import br.com.trier.biblioteca.repositories.LoanRepository;
import br.com.trier.biblioteca.services.LoanService;
import br.com.trier.biblioteca.services.exceptions.ObjectNotFound;
import br.com.trier.biblioteca.utils.DateUtils;

@Service
public class LoanServiceImpl implements LoanService {

	@Autowired
	private LoanRepository repository;

	@Override
	public Loan findById(Integer id) {
		return repository.findById(id)
				.orElseThrow(() -> new ObjectNotFound("O empréstimo %s não existe".formatted(id)));
	}

	@Override
	public Loan insert(Loan loan) {
		return repository.save(loan);
	}

	@Override
	public List<Loan> listAll() {
		List<Loan> lista = repository.findAll();
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum empréstimo cadastrado");
		}
		return lista;
	}

	@Override
	public Loan update(Loan loan) {
		findById(loan.getId());
		return repository.save(loan);
	}

	@Override
	public void delete(Integer id) {
		repository.delete(findById(id));
	}

	@Override
	public List<Loan> findByLoanDate(String loanDate) {
		ZonedDateTime zonedLoanDate = DateUtils.strToZonedDateTime(loanDate);

		List<Loan> lista = repository.findByLoanDate(zonedLoanDate);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum empréstimo cadastrado na data %s".formatted(loanDate));
		}
		return lista;
	}

	@Override
	public List<Loan> findByReturnDate(String returnDate) {
		ZonedDateTime zonedReturnDate = DateUtils.strToZonedDateTime(returnDate);

		List<Loan> lista = repository.findByReturnDate(zonedReturnDate);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum empréstimo cadastrado com data de devolução %s".formatted(returnDate));
		}
		return lista;
	}

	@Override
	public List<Loan> findByLoanDateBetween(String loanDateIn, String loanDateFin) {
		ZonedDateTime zonedLoanDateIn = DateUtils.strToZonedDateTime(loanDateIn);
		ZonedDateTime zonedLoanDateFin = DateUtils.strToZonedDateTime(loanDateFin);

		List<Loan> lista = repository.findByLoanDateBetween(zonedLoanDateIn, zonedLoanDateFin);
		if (lista.isEmpty()) {
			throw new ObjectNotFound(
					"Nenhum empréstimo cadastrado entre as datas %s e %s".formatted(loanDateIn, loanDateFin));
		}
		return lista;
	}

	@Override
	public List<Loan> findByBook(Book book) {
		List<Loan> lista = repository.findByBook(book);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum empréstimo cadastrado para o livro com ID %s".formatted(book.getId()));
		}
		return lista;
	}

	@Override
	public List<Loan> findByUser(User user) {
		List<Loan> lista = repository.findByUser(user);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum empréstimo cadastrado para o usuário com ID %s".formatted(user.getId()));
		}
		return lista;
	}
}
