package it.prova.gestionesocieta.service;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.prova.gestionesocieta.model.Societa;

@Service
public class BatteriaDiTestService {

	@Autowired
	private SocietaService societaService;
	@Autowired
	private DipendenteService dipendenteService;
	

	public void testInserisciNuovoMunicipio() {
		Long nowInMillisecondi = new Date().getTime();

		Societa nuovaSocieta = new Societa("RS" + nowInMillisecondi, "VIA" + nowInMillisecondi, LocalDate.now());
		if (nuovaSocieta.getId() != null )
			throw new RuntimeException("testInserisciNuovoMunicipio...failed: transient object con id valorizzato");
		// salvo
		societaService.inserisciNuovo(nuovaSocieta);
		if (nuovaSocieta.getId() == null || nuovaSocieta.getId() < 1)
			throw new RuntimeException("testInserisciNuovoMunicipio...failed: inserimento fallito");

		System.out.println("testInserisciNuovoMunicipio........OK");
		
	}
	
	
	
}
