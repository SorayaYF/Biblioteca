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

	private void findByEmail(User user) {
		Optional<User> busca = repository.findByEmail(user.getEmail());
		if (busca != null && !busca.get().getId().equals(user.getId())) {
			throw new IntegrityViolation("Email já existente: %s".formatted(user.getEmail()));
		}
	}

	@Override
	public User findById(Integer id) {
		Optional<User> user = repository.findById(id);
		return user.orElseThrow(() -> new ObjectNotFound("O usuário %s não existe".formatted(id)));
	}

	@Override
	public User insert(User user) {
		findByEmail(user);
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
		findByEmail(user);
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
