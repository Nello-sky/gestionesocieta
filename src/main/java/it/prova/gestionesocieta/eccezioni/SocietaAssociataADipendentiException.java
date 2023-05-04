package it.prova.gestionesocieta.eccezioni;

public class SocietaAssociataADipendentiException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public SocietaAssociataADipendentiException() {
	}

	public SocietaAssociataADipendentiException(String message) {
		super(message);
	}

}
