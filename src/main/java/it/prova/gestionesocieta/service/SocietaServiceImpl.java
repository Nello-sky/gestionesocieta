package it.prova.gestionesocieta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionesocieta.model.Societa;
import it.prova.gestionesocieta.repository.SocietaRepository;

@Service
public class SocietaServiceImpl implements SocietaService {

	@Autowired
	SocietaRepository societaRepository;

	@Transactional(readOnly = true)
	public List<Societa> listAllAbitanti() {
		return (List<Societa>) societaRepository.findAll();
	}

	@Transactional
	public void inserisciNuovo(Societa societa) {
		societaRepository.save(societa);
		
	}

	@Override
	public List<Societa> findByExample(Societa example) {
		// TODO Auto-generated method stub
		return null;
	}

}
