package br.com.trier.biblioteca.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.biblioteca.domain.Author;
import br.com.trier.biblioteca.repositories.AuthorRepository;
import br.com.trier.biblioteca.services.AuthorService;
import br.com.trier.biblioteca.services.exceptions.IntegrityViolation;
import br.com.trier.biblioteca.services.exceptions.ObjectNotFound;

@Service
public class AuthorServiceImpl implements AuthorService{

	@Autowired
	private AuthorRepository repository;
	
	private void findByName(Author author) {
		Author busca = repository.findByName(author.getName());
		if (busca != null && busca.getId() != author.getId()) {
			throw new IntegrityViolation("Nome já cadastrado: %s".formatted(author.getName()));
		}
	}

	@Override
	public Author findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("O autor %s não existe".formatted(id)));
	}

	@Override
	public Author insert(Author author) {
		findByName(author);
		return repository.save(author);
	}

	@Override
	public List<Author> listAll() {
		List<Author> lista = repository.findAll();
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum autor cadastrado");
		}
		return lista;
	}

	@Override
	public Author update(Author author) {
		findById(author.getId());
		findByName(author);
		return repository.save(author);
	}

	@Override
	public void delete(Integer id) {
		Author author = findById(id);
		repository.delete(author);
	}

	@Override
	public List<Author> findByNameStartsWithIgnoreCase(String name) {
		List<Author> lista = repository.findByNameStartsWithIgnoreCase(name);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum autor inicia com %s".formatted(name));
		}
		return lista;
	}
	
}
