package it.prova.gestionesocieta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import it.prova.gestionesocieta.model.Societa;

public interface SocietaRepository extends CrudRepository<Societa, Long> {

	@EntityGraph(attributePaths = { "dipendenti" })
	Societa searchById(Long id);
	
	@EntityGraph(attributePaths = { "dipendenti" })
	List<Societa> findAllDistinctByDipendenti_redditoAnnuoLordoGreaterThan(int ragioneSociale);
	
}
