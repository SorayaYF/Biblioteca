package br.com.trier.biblioteca;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import br.com.trier.biblioteca.services.AuthorService;
import br.com.trier.biblioteca.services.LoanService;
import br.com.trier.biblioteca.services.PublishingCompanyService;
import br.com.trier.biblioteca.services.ReservationService;
import br.com.trier.biblioteca.services.UserService;
import br.com.trier.biblioteca.services.impl.AuthorServiceImpl;
import br.com.trier.biblioteca.services.impl.LoanServiceImpl;
import br.com.trier.biblioteca.services.impl.PublishingCompanyServiceImpl;
import br.com.trier.biblioteca.services.impl.ReservationServiceImpl;
import br.com.trier.biblioteca.services.impl.UserServiceImpl;

@TestConfiguration
@SpringBootTest
@ActiveProfiles("test")
public class BaseTests {

	@Bean
	public UserService userService() {
		return new UserServiceImpl();
	}

	@Bean
	public AuthorService authorService() {
		return new AuthorServiceImpl();
	}

	@Bean
	public PublishingCompanyService publishingCompanyService() {
		return new PublishingCompanyServiceImpl();
	}

	@Bean
	public ReservationService reservationService() {
		return new ReservationServiceImpl();
	}

	@Bean
	public LoanService loanService() {
		return new LoanServiceImpl();
	}
}
