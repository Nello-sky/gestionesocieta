package it.prova.gestionesocieta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.gestionesocieta.model.Dipendente;
import it.prova.gestionesocieta.model.Societa;

public interface DipendenteRepository extends CrudRepository<Dipendente, Long> {
	
	@EntityGraph(attributePaths = { "societa" })
	List<Dipendente> searchBySocieta(Societa societa);
	
	@Query("select d from Dipendente d left join fetch d.societa s where d.dataAssunzione =(select min(d.dataAssunzione) from Dipendente d) "
			+ "and year(s.dataFondazione) < ?1")
	Dipendente findOldBySocietaFondatePrimaDi (int annoSup);
	
}
