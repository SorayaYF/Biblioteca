package br.com.trier.biblioteca.domain;

import java.time.ZonedDateTime;

import br.com.trier.biblioteca.domain.dto.LoanDTO;
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

@Entity(name = "emprestimo")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Loan {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_emprestimo")
    private Integer id;

    @Column(name = "data_emprestimo")
    private ZonedDateTime loanDate;

    @Column(name = "data_devolucao")
    private ZonedDateTime returnDate;

    @ManyToOne
    private Book book;

    @ManyToOne
    private User user;

    public Loan(LoanDTO dto, Book book, User user) {
        this(dto.getId(), DateUtils.strToZonedDateTime(dto.getLoanDate()), DateUtils.strToZonedDateTime(dto.getReturnDate()), book, user);
    }

    public LoanDTO toDTO() {
        return new LoanDTO(id, DateUtils.zonedDateTimeToStr(loanDate), DateUtils.zonedDateTimeToStr(returnDate), book.getId(), book.getTitle(),
                user.getId(), user.getName());
    }

}
