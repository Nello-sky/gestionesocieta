package it.prova.gestionesocieta.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.management.RuntimeErrorException;

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
			throw new RuntimeException("testInserisciNuovaSocieta...failed: transient object con id valorizzato");
		// salvo
		societaService.inserisciNuovo(nuovaSocieta);
		if (nuovaSocieta.getId() == null || nuovaSocieta.getId() < 1)
			throw new RuntimeException("testInserisciNuovaSocieta...failed: inserimento fallito");

		System.out.println("testInserisciNuovaSocieta........OK");

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
			throw new RuntimeException("testDeleteSocietaConDipendenti...failed: inserimento fallito");

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
			throw new RuntimeException(
					"testInserisciNuovoDipendenteDataSocieta...failed: transient object con id valorizzato");
		// salvo
		societaService.inserisciNuovo(nuovaSocieta);
		if (nuovaSocieta.getId() == null || nuovaSocieta.getId() < 1)
			throw new RuntimeException("testInserisciNuovoDipendenteDataSocieta...failed: inserimento fallito");

		Dipendente nuovoDipendente = new Dipendente("nome" + nowInMillisecondi, "cognome" + nowInMillisecondi,
				LocalDate.now(), 55, null);

		try {
			dipendenteService.insertDipendenteConSocieta(nuovoDipendente, nuovaSocieta);
		} catch (SocietaNotExistException e) {
			e.printStackTrace();
		}
		// controllo sia inserita
		if (nuovoDipendente.getId() == null || nuovoDipendente.getId() < 1)
			throw new RuntimeException("testInserisciNuovoDipendenteDataSocieta...failed: inserimento fallito");

		System.out.println("testInserisciNuovoDipendenteDataSocieta........OK");

	}

	public void testInserisciNuovoDipendente() {
		Long nowInMillisecondi = new Date().getTime();

		Societa nuovaSocieta = new Societa("RS" + nowInMillisecondi, "VIA" + nowInMillisecondi, LocalDate.now());
		if (nuovaSocieta.getId() != null)
			throw new RuntimeException("testInserisciNuovoDipendente...failed: transient object con id valorizzato");
		// salvo
		societaService.inserisciNuovo(nuovaSocieta);
		if (nuovaSocieta.getId() == null || nuovaSocieta.getId() < 1)
			throw new RuntimeException("testInserisciNuovoDipendente...failed: inserimento fallito");

		Dipendente nuovoDipendente = new Dipendente("nome" + nowInMillisecondi, "cognome" + nowInMillisecondi,
				LocalDate.now(), 55, nuovaSocieta);

		dipendenteService.inserisciNuovo(nuovoDipendente);

		// controllo sia inserita
		if (nuovoDipendente.getId() == null || nuovoDipendente.getId() < 1)
			throw new RuntimeException("testInserisciNuovoDipendente...failed: inserimento fallito");

		System.out.println("testInserisciNuovoDipendente........OK");

	}

	public void testAggiornaDipendente() {
		Long nowInMillisecondi = new Date().getTime();

		Societa nuovaSocieta = new Societa("RS" + nowInMillisecondi, "VIA" + nowInMillisecondi, LocalDate.now());
		if (nuovaSocieta.getId() != null)
			throw new RuntimeException("testAggiornaDipendente...failed: transient object con id valorizzato");
		// salvo
		societaService.inserisciNuovo(nuovaSocieta);
		if (nuovaSocieta.getId() == null || nuovaSocieta.getId() < 1)
			throw new RuntimeException("testAggiornaDipendente...failed: inserimento societa fallito");

		Dipendente nuovoDipendente = new Dipendente("nome" + nowInMillisecondi, "cognome" + nowInMillisecondi,
				LocalDate.now(), 55, nuovaSocieta);
		dipendenteService.inserisciNuovo(nuovoDipendente);
		if (nuovoDipendente.getId() == null || nuovoDipendente.getId() < 1)
			throw new RuntimeException("testAggiornaDipendente...failed: inserimento dipendente fallito");

		// ricarico per confronto
		Dipendente nuovoDipendenteUpload = dipendenteService.caricaSingoloDipendente(nuovoDipendente.getId());
		nuovoDipendenteUpload.setRedditoAnnuoLordo(56);
		// modifica
		dipendenteService.aggiorna(nuovoDipendenteUpload);
		// test
		if (nuovoDipendenteUpload.getRedditoAnnuoLordo() != 56)
			throw new RuntimeException("testAggiornaDipendente...failed: aggiornamento fallito");

		System.out.println("testAggiornaDipendente........OK");

	}

	public void testFindAllDistinctSocietaConDipendentiConRedditoAnnuoLordoMaggioreDi() {
		Long nowInMillisecondi = new Date().getTime();
		int redditoAnnuoTest = 30000;

		IntStream.range(1, 5).forEach(i -> {
			int redditoAnnuoToSet = i % 2 == 0 ? 10000 : 40000;
			Societa nuovaSocieta = new Societa("RS" + nowInMillisecondi, "VIA" + nowInMillisecondi, LocalDate.now());
			societaService.inserisciNuovo(nuovaSocieta);
			dipendenteService.inserisciNuovo(new Dipendente("nome" + nowInMillisecondi, "cognome" + nowInMillisecondi,
					LocalDate.now(), redditoAnnuoToSet, nuovaSocieta));
			dipendenteService.inserisciNuovo(new Dipendente("name" + nowInMillisecondi, "surname" + nowInMillisecondi,
					LocalDate.now(), redditoAnnuoToSet, nuovaSocieta));
		});
		// test numero societa
		List<Societa> societaAttese = societaService.findByDipendentiConRedditoAnnuoLordoMaggioreDi(redditoAnnuoTest);
		if (societaAttese.size() != 2)
			throw new RuntimeException(
					"testFindAllDistinctSocietaConDipendentiConRagioneSocialeMaggioreDi..failed:numero societa errato");
		// test numero dipendenti
		List<Dipendente> dipendentiAttesi = societaAttese.stream().flatMap(societa -> societa.getDipendenti().stream())
				.collect(Collectors.toList());
		if (dipendentiAttesi.size() != 4)
			throw new RuntimeException(
					"testFindAllDistinctSocietaConDipendentiConRagioneSocialeMaggioreDi..failed:numero dipendenti errato");
		System.out.println("testFindAllDistinctSocietaConDipendentiConRagioneSocialeMaggioreDi........OK");
	}

	public void testCercaDipendenteConMassimaAnzianitaInSocietaFondatePrimaDI() {
		int yearToCheck = 1990;
		int yearToAvoidControl = 1985; // non dovrebbero esserci dipendenti con ingaggi precendenti alla fondazione
		
		Societa nuovaSocieta = new Societa("RS", "VIA", LocalDate.of(yearToAvoidControl, 7, 7));
		if (nuovaSocieta.getId() != null)
			throw new RuntimeException(
					"testCercaDipendenteConMassimaAnzianitaInSocietaFondatePrimaDI...failed: transient object con id valorizzato");
		societaService.inserisciNuovo(nuovaSocieta);

		IntStream.range(1, 6).forEach(i -> {
			dipendenteService.inserisciNuovo(
					new Dipendente("nome" + i, "cognome" + i, LocalDate.of(yearToAvoidControl + i, 7, 7), 50 + i, nuovaSocieta));
		});
		
		
		Dipendente dipendenteTarget = dipendenteService.findPerAnzianitaBySocietaFondatePrimaDi(yearToCheck);
		if (dipendenteTarget.getId() == 0 )
			throw new RuntimeException(
					"testCercaDipendenteConMassimaAnzianitaInSocietaFondatePrimaDI..failed: non trovato per anzianita");
		System.out.println("testFindAllDistinctSocietaConDipendentiConRagioneSocialeMaggioreDi........OK");
		System.out.println(dipendenteTarget.getNome());
	} 

}
