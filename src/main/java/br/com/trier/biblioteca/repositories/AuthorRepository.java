package br.com.trier.biblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.biblioteca.domain.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
	
	Author findByName(String name);

	List<Author> findByNameStartsWithIgnoreCase(String name);

}
