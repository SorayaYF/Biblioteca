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

import br.com.trier.biblioteca.domain.PublishingCompany;
import br.com.trier.biblioteca.services.PublishingCompanyService;

@RestController
@RequestMapping("/publishingCompanys")
public class PublishingCompanyResource {

	@Autowired
	private PublishingCompanyService service;

	@Secured({ "ROLE_USER" })
	@GetMapping("/{id}")
	public ResponseEntity<PublishingCompany> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@Secured({ "ROLE_ADMIN" })
	@PostMapping
	public ResponseEntity<PublishingCompany> insert(@RequestBody PublishingCompany publishingCompany) {
		return ResponseEntity.ok(service.insert(publishingCompany));
	}

	@Secured({ "ROLE_USER" })
	@GetMapping
	public ResponseEntity<List<PublishingCompany>> listAll() {
		return ResponseEntity.ok(service.listAll());
	}

	@Secured({ "ROLE_ADMIN" })
	@PutMapping("/{id}")
	public ResponseEntity<PublishingCompany> update(@PathVariable Integer id,
			@RequestBody PublishingCompany publishingCompany) {
		publishingCompany.setId(id);
		return ResponseEntity.ok(service.update(publishingCompany));
	}

	@Secured({ "ROLE_ADMIN" })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@Secured({ "ROLE_USER" })
	@GetMapping("/name/{name}")
	public ResponseEntity<List<PublishingCompany>> findByNameStartsWithIgnoreCase(@PathVariable String name) {
		return ResponseEntity.ok(service.findByNameStartsWithIgnoreCase(name));
	}

}
