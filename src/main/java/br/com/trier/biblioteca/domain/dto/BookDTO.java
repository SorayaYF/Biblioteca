package br.com.trier.biblioteca.domain.dto;

import br.com.trier.biblioteca.enuns.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

	private Integer id;
	private String title;
	private String ISBN;
	private Genre genre;
	private Integer authorId;
	private String authorName;
	private Integer publishingCompanyId;
	private String publishingCompanyName;

}
