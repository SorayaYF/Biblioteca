package br.com.trier.biblioteca.domain;

import java.time.ZonedDateTime;

import br.com.trier.biblioteca.domain.dto.ReservationDTO;
import br.com.trier.biblioteca.utils.DateUtils;
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

@Entity(name = "reserva")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Reservation {
	
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reserva")
	private Integer id;

	@Column(name = "data_reserva")
	private ZonedDateTime date;

	@ManyToOne
	private Book book;

	@ManyToOne
	private User user;
	
	public Reservation(ReservationDTO dto, Book book, User user) {
		this(dto.getId(), DateUtils.strToZonedDateTime(dto.getDate()), book, user);
	}

	public ReservationDTO toDTO() {
		return new ReservationDTO(id, DateUtils.zonedDateTimeToStr(date), book.getId(), book.getTitle(),
				user.getId(), user.getName());
	}

}
