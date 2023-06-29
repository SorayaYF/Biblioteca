package br.com.trier.biblioteca.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.biblioteca.domain.Loan;
import br.com.trier.biblioteca.domain.dto.LoanDTO;
import br.com.trier.biblioteca.services.LoanService;
import br.com.trier.biblioteca.services.BookService;
import br.com.trier.biblioteca.services.UserService;

@RestController
@RequestMapping("/loans")
public class LoanResource {

	@Autowired
	private LoanService service;

	@Autowired
	private BookService bookService;

	@Autowired
	private UserService userService;

	@Secured({ "ROLE_USER" })
	@GetMapping("/{id}")
	public ResponseEntity<LoanDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id).toDTO());
	}

	@Secured({ "ROLE_USER" })
	@PostMapping
	public ResponseEntity<LoanDTO> insert(@RequestBody LoanDTO loanDTO) {
		Loan loan = new Loan(loanDTO, bookService.findById(loanDTO.getBookId()),
				userService.findById(loanDTO.getUserId()));
		return ResponseEntity.ok(service.insert(loan).toDTO());
	}

	@Secured({ "ROLE_USER" })
	@GetMapping
	public ResponseEntity<List<LoanDTO>> listAll() {
		return ResponseEntity.ok(service.listAll().stream().map(loan -> loan.toDTO()).toList());
	}

	@Secured({ "ROLE_USER" })
	@PostMapping("/{id}")
	public ResponseEntity<LoanDTO> update(@RequestBody LoanDTO loanDTO, @PathVariable Integer id) {
		Loan loan = new Loan(loanDTO, bookService.findById(loanDTO.getBookId()),
				userService.findById(loanDTO.getUserId()));
		loan.setId(id);
		return ResponseEntity.ok(service.update(loan).toDTO());
	}

	@Secured({ "ROLE_USER" })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@Secured({ "ROLE_USER" })
	@GetMapping("/loanDate/{loanDate}")
	public ResponseEntity<List<LoanDTO>> findByLoanDate(@PathVariable String loanDate) {
		return ResponseEntity.ok(service.findByLoanDate(loanDate).stream().map(loan -> loan.toDTO()).toList());
	}

	@Secured({ "ROLE_USER" })
	@GetMapping("/returnDate/{returnDate}")
	public ResponseEntity<List<LoanDTO>> findByReturnDate(@PathVariable String returnDate) {
		return ResponseEntity.ok(service.findByReturnDate(returnDate).stream().map(loan -> loan.toDTO()).toList());
	}

	@Secured({ "ROLE_USER" })
	@GetMapping("/loanDate/{loanDateIn}/{loanDateFin}")
	public ResponseEntity<List<LoanDTO>> findByLoanDateBetween(@PathVariable String loanDateIn,
			@PathVariable String loanDateFin) {
		return ResponseEntity
				.ok(service.findByLoanDateBetween(loanDateIn, loanDateFin).stream().map(loan -> loan.toDTO()).toList());
	}

	@Secured({ "ROLE_USER" })
	@GetMapping("/book/{bookId}")
	public ResponseEntity<List<LoanDTO>> findByBook(@PathVariable Integer bookId) {
		return ResponseEntity
				.ok(service.findByBook(bookService.findById(bookId)).stream().map(loan -> loan.toDTO()).toList());
	}

	@Secured({ "ROLE_USER" })
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<LoanDTO>> findByUser(@PathVariable Integer userId) {
		return ResponseEntity
				.ok(service.findByUser(userService.findById(userId)).stream().map(loan -> loan.toDTO()).toList());
	}
}
