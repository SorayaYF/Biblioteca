package br.com.trier.biblioteca.domain;

import br.com.trier.biblioteca.domain.dto.BookDTO;
import br.com.trier.biblioteca.enuns.Genre;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "livro")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Book {

	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_livro")
	private Integer id;

	@Column(name = "titulo_livro")
	private String title;

	@Column(name = "ISBN_livro")
	private String ISBN;

	@Column(name = "genero_livro")
	private Genre genre;

	@ManyToOne
	private Author author;

	@ManyToOne
	private PublishingCompany publishingCompany;

	public Book(BookDTO dto, Author author, PublishingCompany publishingCompany) {
		this(dto.getId(), dto.getTitle(), dto.getISBN(), dto.getGenre(), author, publishingCompany);
	}

	public BookDTO toDTO() {
		return new BookDTO(id, title, ISBN, genre, author.getId(), author.getName(), publishingCompany.getId(),
				publishingCompany.getName());
	}
}
