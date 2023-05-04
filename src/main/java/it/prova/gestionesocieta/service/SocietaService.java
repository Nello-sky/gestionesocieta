package it.prova.gestionesocieta.service;

import java.util.List;

import it.prova.gestionesocieta.model.Societa;

public interface SocietaService {
	
	List<Societa> listAllAbitanti();
	
	void inserisciNuovo(Societa societa);
	
	 List<Societa> findByExample(Societa example);

}
