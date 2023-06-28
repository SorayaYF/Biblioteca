package br.com.trier.biblioteca.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.biblioteca.domain.Book;
import br.com.trier.biblioteca.domain.Loan;
import br.com.trier.biblioteca.domain.User;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
	
	List<Loan> findByLoanDate(ZonedDateTime loanDate);
	
	List<Loan> findByReturnDate(ZonedDateTime returnDate);

	List<Loan> findByLoanDateBetween(ZonedDateTime loanDateIn, ZonedDateTime loanDateFin);

	List<Loan> findByBook(Book book);

	List<Loan> findByUser(User user);

}
