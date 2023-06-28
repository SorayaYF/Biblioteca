package br.com.trier.biblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.biblioteca.domain.Author;
import br.com.trier.biblioteca.domain.Book;
import br.com.trier.biblioteca.domain.PublishingCompany;
import br.com.trier.biblioteca.enuns.Genre;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{

	List<Book> findByTitleStartsWithIgnoreCase(String name);
	
	List<Book> findByGenre(Genre genre);

	List<Book> findByAuthor(Author author);

	List<Book> findByPublishingCompany(PublishingCompany publishingCompany);
}
