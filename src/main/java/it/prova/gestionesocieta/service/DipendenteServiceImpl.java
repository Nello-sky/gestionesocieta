package it.prova.gestionesocieta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionesocieta.eccezioni.SocietaNotExistException;
import it.prova.gestionesocieta.model.Dipendente;
import it.prova.gestionesocieta.model.Societa;
import it.prova.gestionesocieta.repository.DipendenteRepository;

@Service
public class DipendenteServiceImpl implements DipendenteService {

	@Autowired
	DipendenteRepository dipendenteRepository;

	@Transactional
	public void inserisciNuovo(Dipendente dipendente) {
		dipendenteRepository.save(dipendente);
		
	}
	
	@Override
	public List<Dipendente> cercaPerSocietaEager(Societa societaInput) {
		return dipendenteRepository.searchBySocieta(societaInput);
	}

	@Transactional
	public void insertDipendenteConSocieta(Dipendente dipendente, Societa societa) throws SocietaNotExistException {
		if(societa.getId() == null) {
			throw new SocietaNotExistException("la societa non è inserita");
		}
		dipendente.setSocieta(societa);
		dipendenteRepository.save(dipendente);
		
	}

	@Transactional
	public void aggiorna(Dipendente dipendente) {
		dipendenteRepository.save(dipendente);
		
	}

	@Transactional //???
	public void insertDipendente(Dipendente dipendente) {
		dipendenteRepository.save(dipendente);
		
	}

	@Override
	public Dipendente caricaSingoloDipendente(Long id) {
		return dipendenteRepository.findById(id).orElse(null);
		
	}

	@Transactional(readOnly = true)
	public Dipendente findPerAnzianitaBySocietaFondatePrimaDi(int annoSup) {
		
		return dipendenteRepository.findOldBySocietaFondatePrimaDi(annoSup) ;
	}
	
}
