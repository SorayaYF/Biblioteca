package br.com.trier.biblioteca.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
	
	private Integer id;
	private String date;
	private Integer bookId;
	private String bookTitle;
	private Integer userId;
	private String userName;

}
