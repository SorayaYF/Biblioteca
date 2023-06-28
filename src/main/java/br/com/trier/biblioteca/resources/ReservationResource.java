package br.com.trier.biblioteca.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.biblioteca.domain.Reservation;
import br.com.trier.biblioteca.domain.dto.ReservationDTO;
import br.com.trier.biblioteca.services.ReservationService;
import br.com.trier.biblioteca.services.BookService;
import br.com.trier.biblioteca.services.UserService;

@RestController
@RequestMapping("/reservations")
public class ReservationResource {

    @Autowired
    private ReservationService service;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Secured({"ROLE_USER"})
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id).toDTO());
    }

    @Secured({"ROLE_USER"})
    @PostMapping
    public ResponseEntity<ReservationDTO> insert(@RequestBody ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation(reservationDTO,
                bookService.findById(reservationDTO.getBookId()),
                userService.findById(reservationDTO.getUserId()));
        return ResponseEntity.ok(service.insert(reservation).toDTO());
    }

    @Secured({"ROLE_USER"})
    @GetMapping
    public ResponseEntity<List<ReservationDTO>> listAll() {
        return ResponseEntity.ok(service.listAll().stream().map(reservation -> reservation.toDTO()).toList());
    }

    @Secured({"ROLE_USER"})
    @PostMapping("/{id}")
    public ResponseEntity<ReservationDTO> update(@RequestBody ReservationDTO reservationDTO, @PathVariable Integer id) {
        Reservation reservation = new Reservation(reservationDTO,
                bookService.findById(reservationDTO.getBookId()),
                userService.findById(reservationDTO.getUserId()));
        reservation.setId(id);
        return ResponseEntity.ok(service.update(reservation).toDTO());
    }

    @Secured({"ROLE_USER"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
	@Secured({"ROLE_USER"})
	@GetMapping("/date/{date}")
	public ResponseEntity<List<ReservationDTO>> findByDate(@PathVariable String date) {
		return ResponseEntity.ok(service.findByDate(date).stream().map(reservation -> reservation.toDTO()).toList());
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/date/{dateIn}/{dateFin}")
	public ResponseEntity<List<ReservationDTO>> findByDateBetween(@PathVariable String dateIn, @PathVariable String dateFin) {
		return ResponseEntity.ok(service.findByDateBetween(dateIn, dateFin).stream().map(reservation -> reservation.toDTO()).toList());
	}

    @Secured({"ROLE_USER"})
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReservationDTO>> findByBook(@PathVariable Integer bookId) {
        return ResponseEntity.ok(service.findByBook(bookService.findById(bookId)).stream()
                .map(reservation -> reservation.toDTO()).toList());
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationDTO>> findByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.findByUser(userService.findById(userId)).stream()
                .map(reservation -> reservation.toDTO()).toList());
    }
}

