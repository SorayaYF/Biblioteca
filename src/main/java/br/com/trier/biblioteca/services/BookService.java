package br.com.trier.biblioteca.services;

import java.util.List;

import br.com.trier.biblioteca.domain.Author;
import br.com.trier.biblioteca.domain.Book;
import br.com.trier.biblioteca.domain.PublishingCompany;
import br.com.trier.biblioteca.enuns.Genre;

public interface BookService {

	Book findById(Integer id);

	Book insert(Book book);

	List<Book> listAll();

	Book update(Book book);

	void delete(Integer id);

	List<Book> findByTitleStartsWithIgnoreCase(String name);

	List<Book> findByGenre(Genre genre);

	List<Book> findByAuthor(Author author);

	List<Book> findByPublishingCompany(PublishingCompany publishingCompany);
}
