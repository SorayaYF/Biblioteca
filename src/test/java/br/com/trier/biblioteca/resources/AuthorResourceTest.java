package br.com.trier.biblioteca.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.biblioteca.BibliotecaApplication;
import br.com.trier.biblioteca.config.jwt.LoginDTO;
import br.com.trier.biblioteca.domain.Author;

@ActiveProfiles("test")
@SpringBootTest(classes = BibliotecaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorResourceTest {

	@Autowired
	protected TestRestTemplate rest;

	private HttpHeaders getHeaders(String email, String password) {
		LoginDTO loginDTO = new LoginDTO(email, password);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);
		ResponseEntity<String> responseEntity = rest.exchange(
				"/auth/token", 
				HttpMethod.POST,  
				requestEntity,    
				String.class   
				);
		String token = responseEntity.getBody();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);
		return headers;
	}

	private ResponseEntity<Author> getAuthor(String url) {
		return rest.exchange(
			url,
			HttpMethod.GET,
			new HttpEntity<>(getHeaders("email1", "senha1")),
			Author.class
		);
	}

	private ResponseEntity<List<Author>> getAuthors(String url) {
		return rest.exchange(
			url,
			HttpMethod.GET,
			new HttpEntity<>(getHeaders("email1", "senha1")),
			new ParameterizedTypeReference<List<Author>>() {}
		);
	}

	@Test
	@DisplayName("Encontrar por ID")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/autor.sql" })
	public void findByIdTest() {
		ResponseEntity<Author> response = getAuthor("/authors/1");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Author author = response.getBody();
		assertEquals("Autor 1", author.getName());
	}

	@Test
	@DisplayName("Inserir autor")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	public void insertTest() {
		Author author = new Author(1, "Autor Atualizado");
		HttpHeaders headers = getHeaders("email1", "senha1");
		HttpEntity<Author> requestEntity = new HttpEntity<>(author, headers);
		ResponseEntity<Author> responseEntity = rest.exchange(
			"/authors",
			HttpMethod.POST,
			requestEntity,
			Author.class
		);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Author insertedAuthor = responseEntity.getBody();
		assertEquals("Autor 1", insertedAuthor.getName());
	}

	@Test
	@DisplayName("Listar todos os autor")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/autor.sql" })
	public void listAllTest() {
		ResponseEntity<List<Author>> response = getAuthors("/authors");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
	}

	@Test
	@DisplayName("Atualizar autor")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/autor.sql" })
	public void updateTest() {
		Author author = new Author(1, "Autor Atualizado");
		HttpHeaders headers = getHeaders("email1", "senha1");
		HttpEntity<Author> requestEntity = new HttpEntity<>(author, headers);
		ResponseEntity<Author> responseEntity = rest.exchange(
			"/authors/1",
			HttpMethod.PUT,
			requestEntity,
			Author.class
		);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Author updatedAuthor = responseEntity.getBody();
		assertEquals("Autor Atualizado", updatedAuthor.getName());
	}

	@Test
	@DisplayName("Excluir autor")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/autor.sql" })
	public void deleteTest() {
		HttpHeaders headers = getHeaders("email1", "senha1");
		HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
		ResponseEntity<Void> responseEntity = rest.exchange(
			"/authors/1",
			HttpMethod.DELETE,
			requestEntity,
			Void.class
		);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	@DisplayName("Buscar por nome")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/autor.sql" })
	public void findByNameTest() {
		ResponseEntity<List<Author>> response = getAuthors("/authors?name=Autor");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
	}
}
