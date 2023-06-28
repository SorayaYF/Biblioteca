package br.com.trier.biblioteca.services;

import java.util.List;

import br.com.trier.biblioteca.domain.User;

public interface UserService {

	User findById(Integer id);

	User insert(User user);

	List<User> listAll();

	User update(User user);

	void delete(Integer id);

	List<User> findByNameStartsWithIgnoreCase(String name);

}
