package br.com.trier.biblioteca.services;

import java.util.List;

import br.com.trier.biblioteca.domain.Author;

public interface AuthorService {
	
	Author findById(Integer id);

	Author insert(Author author);

	List<Author> listAll();

	Author update(Author author);

	void delete(Integer id);

	List<Author> findByNameStartsWithIgnoreCase(String name);

}
