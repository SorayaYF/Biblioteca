package br.com.trier.biblioteca.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.biblioteca.domain.User;
import br.com.trier.biblioteca.repositories.UserRepository;
import br.com.trier.biblioteca.services.UserService;
import br.com.trier.biblioteca.services.exceptions.IntegrityViolation;
import br.com.trier.biblioteca.services.exceptions.ObjectNotFound;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;

	public User findByEmail(String email) {
	    Optional<User> userOptional = repository.findByEmail(email);
	    if (userOptional.isPresent()) {
	        return userOptional.get();
	    } else {
	        throw new ObjectNotFound("O usuário com o e-mail " + email + " não existe");
	    }
	}

	@Override
	public User findById(Integer id) {
		Optional<User> user = repository.findById(id);
		return user.orElseThrow(() -> new ObjectNotFound("O usuário %s não existe".formatted(id)));
	}

	@Override
	public User insert(User user) {
		return repository.save(user);
	}

	@Override
	public List<User> listAll() {
		List<User> lista = repository.findAll();
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum usuário cadastrado");
		}
		return lista;
	}

	@Override
	public User update(User user) {
		findById(user.getId());
		return repository.save(user);
	}

	@Override
	public void delete(Integer id) {
		User user = findById(id);
		repository.delete(user);
	}

	@Override
	public List<User> findByNameStartsWithIgnoreCase(String name) {
		List<User> lista = repository.findByNameStartsWithIgnoreCase(name);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum nome de usuário inicia com %s cadastrado".formatted(name));
		}
		return lista;
	}

}
