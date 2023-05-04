package it.prova.gestionesocieta.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionesocieta.eccezioni.SocietaAssociataADipendentiException;
import it.prova.gestionesocieta.model.Societa;
import it.prova.gestionesocieta.repository.SocietaRepository;

@Service
public class SocietaServiceImpl implements SocietaService {

	@Autowired
	SocietaRepository societaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional(readOnly = true)
	public List<Societa> listAllAbitanti() {
		return (List<Societa>) societaRepository.findAll();
	}

	@Transactional
	public void inserisciNuovo(Societa societa) {
		societaRepository.save(societa);

	}
	
	@Transactional(readOnly = true)
	public Societa caricaSingolaSocietaEager(Long id) {
		return societaRepository.searchById(id);
	}

	@Override
	public List<Societa> findByExample(Societa example) {

		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder("select s from Societa s where s.id = s.id ");

		if (StringUtils.isNotEmpty(example.getRagioneSociale())) {
			whereClauses.add(" s.ragioneSociale  like :ragioneSociale ");
			paramaterMap.put("ragioneSociale", "%" + example.getRagioneSociale() + "%");
		}
		if (StringUtils.isNotEmpty(example.getIndirizzo())) {
			whereClauses.add(" s.indirizzo like :indirizzo ");
			paramaterMap.put("indirizzo", "%" + example.getIndirizzo() + "%");
		}
		if (example.getDataFondazione() != null) {
			whereClauses.add("s.dataFondazione >= :dataFondazione ");
			paramaterMap.put("dataFondazione", example.getDataFondazione());
		}
		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Societa> typedQuery = entityManager.createQuery(queryBuilder.toString(), Societa.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}

		return typedQuery.getResultList();
	}

	@Override
	public void rimuoviConEccezione(Societa societa) {
		if (societa.getDipendenti().size() > 0) {
			throw new SocietaAssociataADipendentiException("collegamenti esistenti");
		}
		societaRepository.delete(societa);

	}

	@Transactional(readOnly = true)
	public List<Societa> findByDipendentiConRedditoAnnuoLordoMaggioreDi(int ragioneSociale) {
		return societaRepository.findAllDistinctByDipendenti_redditoAnnuoLordoGreaterThan(ragioneSociale);
	}

	@Override
	public void deleteWithExceptionBySocietyId(Long id) throws SocietaAssociataADipendentiException {
		Societa societa = societaRepository.searchById(id);
		if (societa.getDipendenti().size() > 0) {
			throw new SocietaAssociataADipendentiException("collegamenti esistenti");
		}
		societaRepository.delete(societa);
		
		
	}

}
