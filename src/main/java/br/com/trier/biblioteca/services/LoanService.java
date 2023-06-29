package br.com.trier.biblioteca.services;

import java.util.List;

import br.com.trier.biblioteca.domain.Book;
import br.com.trier.biblioteca.domain.Loan;
import br.com.trier.biblioteca.domain.User;

public interface LoanService {

	Loan findById(Integer id);

	Loan insert(Loan loan);

	List<Loan> listAll();

	Loan update(Loan loan);

	void delete(Integer id);

	List<Loan> findByLoanDate(String loanDate);

	List<Loan> findByReturnDate(String returnDate);

	List<Loan> findByLoanDateBetween(String loanDateIn, String loanDateFin);

	List<Loan> findByBook(Book book);

	List<Loan> findByUser(User user);
}
