package br.com.trier.biblioteca.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.biblioteca.domain.Author;
import br.com.trier.biblioteca.domain.Book;
import br.com.trier.biblioteca.domain.PublishingCompany;
import br.com.trier.biblioteca.enuns.Genre;
import br.com.trier.biblioteca.repositories.BookRepository;
import br.com.trier.biblioteca.services.BookService;
import br.com.trier.biblioteca.services.exceptions.ObjectNotFound;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository repository;

	@Override
	public Book findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("O livro %s não existe".formatted(id)));
	}

	@Override
	public Book insert(Book book) {
		return repository.save(book);
	}

	@Override
	public List<Book> listAll() {
		List<Book> lista = repository.findAll();
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum livro cadastrado");
		}
		return lista;
	}

	@Override
	public Book update(Book book) {
		findById(book.getId());
		return repository.save(book);
	}

	@Override
	public void delete(Integer id) {
		Book book = findById(id);
		repository.delete(book);
	}

	@Override
	public List<Book> findByTitleStartsWithIgnoreCase(String name) {
		List<Book> lista = repository.findByTitleStartsWithIgnoreCase(name);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum livro encontrado com título iniciando por %s".formatted(name));
		}
		return lista;
	}

	@Override
	public List<Book> findByGenre(Genre genre) {
		List<Book> lista = repository.findByGenre(genre);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum livro encontrado com o gênero %s".formatted(genre));
		}
		return lista;
	}

	@Override
	public List<Book> findByAuthor(Author author) {
		List<Book> lista = repository.findByAuthor(author);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum livro encontrado do autor %s".formatted(author.getName()));
		}
		return lista;
	}

	@Override
	public List<Book> findByPublishingCompany(PublishingCompany publishingCompany) {
		List<Book> lista = repository.findByPublishingCompany(publishingCompany);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum livro encontrado da editora %s".formatted(publishingCompany.getName()));
		}
		return lista;
	}
}

