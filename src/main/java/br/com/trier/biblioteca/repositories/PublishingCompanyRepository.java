package br.com.trier.biblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.biblioteca.domain.PublishingCompany;

@Repository
public interface PublishingCompanyRepository extends JpaRepository<PublishingCompany, Integer>{

	PublishingCompany findByName(String name);

	List<PublishingCompany> findByNameStartsWithIgnoreCase(String name);
}
