package it.prova.gestionesocieta.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.prova.gestionesocieta.eccezioni.SocietaAssociataADipendentiException;
import it.prova.gestionesocieta.eccezioni.SocietaNotExistException;
import it.prova.gestionesocieta.model.Dipendente;
import it.prova.gestionesocieta.model.Societa;

@Service
public class BatteriaDiTestService {

	@Autowired
	private SocietaService societaService;
	@Autowired
	private DipendenteService dipendenteService;

	public void testInserisciNuovaSocieta() {
		Long nowInMillisecondi = new Date().getTime();

		Societa nuovaSocieta = new Societa("RS" + nowInMillisecondi, "VIA" + nowInMillisecondi, LocalDate.now());
		if (nuovaSocieta.getId() != null)
			throw new RuntimeException("testInserisciNuovoMunicipio...failed: transient object con id valorizzato");
		// salvo
		societaService.inserisciNuovo(nuovaSocieta);
		if (nuovaSocieta.getId() == null || nuovaSocieta.getId() < 1)
			throw new RuntimeException("testInserisciNuovoMunicipio...failed: inserimento fallito");

		System.out.println("testInserisciNuovoMunicipio........OK");

	}

	public void testFindSocietaByExample() {
		Long nowInMillisecondi = new Date().getTime();

		Societa nuovaSocietaTest = new Societa("RS" + nowInMillisecondi, "VIA" + nowInMillisecondi, LocalDate.now());
		if (nuovaSocietaTest.getId() != null)
			throw new RuntimeException("testFindSocietaByExample...failed: transient object con id valorizzato");
		//
		IntStream.range(1, 4).forEach(i -> {
			societaService
					.inserisciNuovo(new Societa("RS" + nowInMillisecondi, "VIA" + nowInMillisecondi, LocalDate.now()));
		});
		// controllo il numero esatto di insert?

		// se provo il metodo che cerca i primi tre deve estrane solo tre
		if (societaService.findByExample(nuovaSocietaTest).size() != 3)
			throw new RuntimeException("testFindSocietaByExample...failed: le societa non sono il numero previsto");

		System.out.println("testFindSocitaByExample........OK");

	}

	public void testDeleteSocietaConDipendenti() {
		Long nowInMillisecondi = new Date().getTime();

		Societa nuovaSocieta = new Societa("RS" + nowInMillisecondi, "VIA" + nowInMillisecondi, LocalDate.now());
		if (nuovaSocieta.getId() != null)
			throw new RuntimeException("testDeleteSocietaConDipendenti...failed: transient object con id valorizzato");
		societaService.inserisciNuovo(nuovaSocieta);

		if (nuovaSocieta.getId() == null || nuovaSocieta.getId() < 1)
			throw new RuntimeException("testInserisciNuovoMunicipio...failed: inserimento fallito");

		// inserisco 5 abitanti usando i range degli stream
		IntStream.range(1, 6).forEach(i -> {
			dipendenteService
					.inserisciNuovo(new Dipendente("nome" + i, "cognome" + i, LocalDate.now(), 50 + i, nuovaSocieta));
		});

		// controllo che siano stati inseriti tutti e 5
		if (dipendenteService.cercaPerSocietaEager(nuovaSocieta).size() != 5)
			throw new RuntimeException(
					"testDeleteSocietaConDipendenti...failed: non tutti gli elementi sono stati trovati");
		// caricamento eager della societa

		// test
		Societa societaReload = societaService.caricaSingolaSocietaEager(nuovaSocieta.getId());
		try {
			societaService.rimuoviConEccezione(societaReload);
			throw new RuntimeException("testRemoveConEccezioneVaInRollback...failed: eccezione non lanciata"); //
		} catch (SocietaAssociataADipendentiException e) {
			e.printStackTrace();
			System.out.println("tested");
		}

		if (societaReload.getId() == null || societaReload.getId() < 1)
			throw new RuntimeException("testDeleteSocietaConDipendenti...failed: inserimento fallito");

		System.out.println("testDeleteSocietaConDipendenti........OK");

	}
	
	public void testInserisciNuovoDipendenteDataSocieta() {
		Long nowInMillisecondi = new Date().getTime();

		Societa nuovaSocieta = new Societa("RS" + nowInMillisecondi, "VIA" + nowInMillisecondi, LocalDate.now());
		if (nuovaSocieta.getId() != null)
			throw new RuntimeException("testInserisciNuovoMunicipio...failed: transient object con id valorizzato");
		// salvo
		societaService.inserisciNuovo(nuovaSocieta);
		if (nuovaSocieta.getId() == null || nuovaSocieta.getId() < 1)
			throw new RuntimeException("testInserisciNuovoMunicipio...failed: inserimento fallito");
		
		Dipendente nuovoDipendente = new Dipendente("nome" + nowInMillisecondi, "cognome" + nowInMillisecondi, LocalDate.now(), 55 , null);
		
		try {
			dipendenteService.insertDipendenteConSocieta(nuovoDipendente, nuovaSocieta);
		} catch (SocietaNotExistException e) {
			e.printStackTrace();
		}
 
		//controllo sia inserita
		if (nuovoDipendente.getId() == null || nuovoDipendente.getId() < 1)
			throw new RuntimeException("testInserisciNuovoDipendenteDataSocieta...failed: inserimento fallito");
		
		System.out.println("testInserisciNuovoDipendenteDataSocieta........OK");

	}

}
