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

import br.com.trier.biblioteca.domain.Book;
import br.com.trier.biblioteca.domain.dto.BookDTO;
import br.com.trier.biblioteca.enuns.Genre;
import br.com.trier.biblioteca.services.AuthorService;
import br.com.trier.biblioteca.services.BookService;
import br.com.trier.biblioteca.services.PublishingCompanyService;

@RestController
@RequestMapping("/books")
public class BookResource {

	@Autowired
	private BookService service;

	@Autowired
	private AuthorService authorService;

	@Autowired
	private PublishingCompanyService publishingCompanyService;

	@Secured({"ROLE_USER"})
	@GetMapping("/{id}")
	public ResponseEntity<BookDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id).toDTO());
	}

	@PostMapping
	public ResponseEntity<BookDTO> insert(@RequestBody BookDTO bookDTO) {
		return ResponseEntity.ok(service.insert(new Book(bookDTO, authorService.findById(bookDTO.getAuthorId()),
				publishingCompanyService.findById(bookDTO.getPublishingCompanyId()))).toDTO());
	}

	@Secured({"ROLE_USER"})
	@GetMapping
	public ResponseEntity<List<BookDTO>> listAll() {
		return ResponseEntity.ok(service.listAll().stream().map(book -> book.toDTO()).toList());
	}

	@PostMapping("/{id}")
	public ResponseEntity<BookDTO> update(@RequestBody BookDTO bookDTO, @PathVariable Integer id) {
		Book book = new Book(bookDTO, authorService.findById(bookDTO.getAuthorId()),
				publishingCompanyService.findById(bookDTO.getPublishingCompanyId()));
		book.setId(id);
		return ResponseEntity.ok(service.update(book).toDTO());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/title/{name}")
	public ResponseEntity<List<BookDTO>> findByTitleStartsWithIgnoreCase(@PathVariable String name) {
		return ResponseEntity
				.ok(service.findByTitleStartsWithIgnoreCase(name).stream().map(book -> book.toDTO()).toList());
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/genre/{genre}")
	public ResponseEntity<List<BookDTO>> findByGenre(@PathVariable Genre genre) {
		return ResponseEntity
				.ok(service.findByGenre(genre).stream().map(book -> book.toDTO()).toList());
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/author/{authorId}")
	public ResponseEntity<List<BookDTO>> findByAuthor(@PathVariable Integer authorId) {
		return ResponseEntity.ok(service.findByAuthor(authorService.findById(authorId)).stream()
				.map(book -> book.toDTO()).toList());
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/publishing-company/{companyId}")
	public ResponseEntity<List<BookDTO>> findByPublishingCompany(@PathVariable Integer companyId) {
		return ResponseEntity.ok(service.findByPublishingCompany(publishingCompanyService.findById(companyId)).stream()
				.map(book -> book.toDTO()).toList());
	}

}
