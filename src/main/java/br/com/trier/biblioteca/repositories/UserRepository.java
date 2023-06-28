package br.com.trier.biblioteca.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.biblioteca.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByName(String name);
	
	Optional<User> findByEmail(String email);

	List<User> findByNameStartsWithIgnoreCase(String name);

}
