package br.com.trier.biblioteca.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.biblioteca.domain.PublishingCompany;
import br.com.trier.biblioteca.repositories.PublishingCompanyRepository;
import br.com.trier.biblioteca.services.PublishingCompanyService;
import br.com.trier.biblioteca.services.exceptions.IntegrityViolation;
import br.com.trier.biblioteca.services.exceptions.ObjectNotFound;

@Service
public class PublishingCompanyServiceImpl implements PublishingCompanyService{

	@Autowired
	private PublishingCompanyRepository repository;
	
	private void findByName(PublishingCompany publishingCompany) {
		PublishingCompany busca = repository.findByName(publishingCompany.getName());
		if (busca != null && busca.getId() != publishingCompany.getId()) {
			throw new IntegrityViolation("Nome já cadastrado: %s".formatted(publishingCompany.getName()));
		}
	}
	
	@Override
	public PublishingCompany findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("A editora %s não existe".formatted(id)));
	}

	@Override
	public PublishingCompany insert(PublishingCompany publishingCompany) {
		findByName(publishingCompany);
		return repository.save(publishingCompany);
	}

	@Override
	public List<PublishingCompany> listAll() {
		List<PublishingCompany> lista = repository.findAll();
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma editora cadastrada");
		}
		return lista;
	}

	@Override
	public PublishingCompany update(PublishingCompany publishingCompany) {
		findById(publishingCompany.getId());
		findByName(publishingCompany);
		return repository.save(publishingCompany);
	}

	@Override
	public void delete(Integer id) {
		PublishingCompany publishingCompany = findById(id);
		repository.delete(publishingCompany);
	}

	@Override
	public List<PublishingCompany> findByNameStartsWithIgnoreCase(String name) {
		List<PublishingCompany> lista = repository.findByNameStartsWithIgnoreCase(name);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma editora inicia com %s".formatted(name));
		}
		return lista;
	}

}
