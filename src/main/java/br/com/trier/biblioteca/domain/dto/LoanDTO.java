package br.com.trier.biblioteca.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoanDTO {

	private Integer id;
	private String loanDate;
	private String returnDate;
	private Integer bookId;
	private String bookTitle;
	private Integer userId;
	private String userName;

}
