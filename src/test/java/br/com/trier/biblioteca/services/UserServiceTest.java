package br.com.trier.biblioteca.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.biblioteca.BaseTests;
import br.com.trier.biblioteca.domain.User;
import br.com.trier.biblioteca.domain.dto.UserDTO;
import br.com.trier.biblioteca.services.exceptions.IntegrityViolation;
import br.com.trier.biblioteca.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
class UserServiceTest extends BaseTests {

    @Autowired
    UserService userService;

    @Test
    @DisplayName("Buscar por id")
    @Sql({ "classpath:/resources/sqls/usuario.sql" })
    void findIdValid() {
        User user = userService.findById(1);
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("User 1", user.getName());
    }

    @Test
    @DisplayName("Buscar por id inexistente")
    @Sql({ "classpath:/resources/sqls/usuario.sql" })
    void findIdInvalid() {
        var ex = assertThrows(ObjectNotFound.class, () -> userService.findById(10));
        assertEquals("O usuário 10 não existe", ex.getMessage());
    }

    @Test
    @DisplayName("Listar Todos")
    @Sql({ "classpath:/resources/sqls/usuario.sql" })
    void listAll() {
        assertEquals(2, userService.listAll().size());
    }

    @Test
    @DisplayName("Listar Todos sem cadastro")
    void listAllEmpty() {
        var ex = assertThrows(ObjectNotFound.class, () -> userService.listAll());
        assertEquals("Nenhum usuário cadastrado", ex.getMessage());
    }

    @Test
    @DisplayName("Cadastrar usuário")
    void insert() {
        UserDTO userDTO = new UserDTO(null, "User 3", "email3", "senha3", "USER");
        User user = new User(userDTO);
        userService.insert(user);
        assertEquals(1, userService.listAll().size());
        assertEquals(1, user.getId());
        assertEquals("User 3", user.getName());
    }

    @Test
    @DisplayName("Cadastrar usuário com nome duplicado")
    @Sql({ "classpath:/resources/sqls/usuario.sql" })
    void insertWithSameName() {
        UserDTO userDTO = new UserDTO(null, "User 1", "email3", "senha3", "USER");
        User user = new User(userDTO);
        var ex = assertThrows(IntegrityViolation.class, () -> userService.insert(user));
        assertEquals("Nome já cadastrado: User 1", ex.getMessage());
    }

    @Test
    @DisplayName("Alterar usuário")
    @Sql({ "classpath:/resources/sqls/usuario.sql" })
    void update() {
		User user = userService.findById(1);
		assertNotNull(user);
		assertEquals(1, user.getId());
		assertEquals("User 1", user.getName());
		assertEquals("email1", user.getEmail());
		assertEquals("senha1", user.getPassword());
		assertEquals("ADMIN,USER", user.getRoles());
		user = new User(1, "User 3", "email3", "senha3", "ADMIN");
		userService.update(user);
		assertEquals(2, userService.listAll().size());
		assertEquals(1, user.getId());
		assertEquals("User 3", user.getName());
		assertEquals("email3", user.getEmail());
		assertEquals("senha3", user.getPassword());
		assertEquals("ADMIN", user.getRoles());
    }

    @Test
    @DisplayName("Alterar usuário inexistente")
    @Sql({ "classpath:/resources/sqls/usuario.sql" })
    void updateInvalid() {
        UserDTO userDTO = new UserDTO(10, "Modified User 10", "email10", "senha10", "USER");
        User user = new User(userDTO);
        var ex = assertThrows(ObjectNotFound.class, () -> userService.update(user));
        assertEquals("O usuário 10 não existe", ex.getMessage());
    }

    @Test
    @DisplayName("Excluir usuário")
    @Sql({ "classpath:/resources/sqls/usuario.sql" })
    void delete() {
        assertEquals(2, userService.listAll().size());
        userService.delete(1);
        assertEquals(1, userService.listAll().size());
        assertEquals(2, userService.listAll().get(0).getId());
    }

    @Test
    @DisplayName("Excluir usuário inexistente")
    @Sql({ "classpath:/resources/sqls/usuario.sql" })
    void deleteNonexistent() {
        assertEquals(2, userService.listAll().size());
        var ex = assertThrows(ObjectNotFound.class, () -> userService.delete(10));
        assertEquals("O usuário 10 não existe", ex.getMessage());
        assertEquals(2, userService.listAll().size());
        assertEquals(1, userService.listAll().get(0).getId());
    }

    @Test
    @DisplayName("Procurar por nome")
    @Sql({ "classpath:/resources/sqls/usuario.sql" })
    void findByName() {
        assertEquals(2, userService.findByNameStartsWithIgnoreCase("U").size());
        var ex = assertThrows(ObjectNotFound.class, () -> userService.findByNameStartsWithIgnoreCase("X"));
        assertEquals("Nenhum nome de usuário inicia com X cadastrado", ex.getMessage());
    }
}
