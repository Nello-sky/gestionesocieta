package it.prova.gestionesocieta.service;

import java.util.List;

import it.prova.gestionesocieta.eccezioni.SocietaNotExistException;
import it.prova.gestionesocieta.model.Dipendente;
import it.prova.gestionesocieta.model.Societa;

public interface DipendenteService {
	
	void inserisciNuovo(Dipendente dipendente);
	
	List<Dipendente> cercaPerSocietaEager(Societa societaInput);
	
	void insertDipendenteConSocieta(Dipendente dipendente, Societa societa) throws SocietaNotExistException;
	
	void aggiorna(Dipendente dipendente);
	
	void insertDipendente(Dipendente dipendente);
	
	Dipendente caricaSingoloDipendente(Long id);
	
	Dipendente findPerAnzianitaBySocietaFondatePrimaDi (int annoSup);

}
