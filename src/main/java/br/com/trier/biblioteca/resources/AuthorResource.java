package br.com.trier.biblioteca.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.biblioteca.domain.Author;
import br.com.trier.biblioteca.services.AuthorService;

@RestController
@RequestMapping("/authors")
public class AuthorResource {
	
	@Autowired
	private AuthorService service;

	@Secured({"ROLE_USER"})
	@GetMapping("/{id}")
	public ResponseEntity<Author> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@Secured({"ROLE_ADMIN"})
	@PostMapping
	public ResponseEntity<Author> insert(@RequestBody Author author) {
		return ResponseEntity.ok(service.insert(author));
	}
	
	@Secured({"ROLE_USER"})
	@GetMapping
	public ResponseEntity<List<Author>> listAll() {
		return ResponseEntity.ok(service.listAll());
	}

	@Secured({"ROLE_ADMIN"})
	@PutMapping("/{id}")
	public ResponseEntity<Author> update(@PathVariable Integer id, @RequestBody Author author) {
		author.setId(id);
		return ResponseEntity.ok(service.update(author));
	}

	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Author>> findByNameStartsWithIgnoreCase(@PathVariable String name) {
		return ResponseEntity.ok(service.findByNameStartsWithIgnoreCase(name));
	}
}
