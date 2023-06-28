package br.com.trier.biblioteca.services;

import java.util.List;

import br.com.trier.biblioteca.domain.PublishingCompany;

public interface PublishingCompanyService {
	
	PublishingCompany findById(Integer id);

	PublishingCompany insert(PublishingCompany publishingCompany);

	List<PublishingCompany> listAll();

	PublishingCompany update(PublishingCompany publishingCompany);

	void delete(Integer id);

	List<PublishingCompany> findByNameStartsWithIgnoreCase(String name);

}
