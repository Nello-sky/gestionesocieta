package it.prova.gestionesocieta.eccezioni;

public class SocietaAssociataADipendentiException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public SocietaAssociataADipendentiException() {
	}

	public SocietaAssociataADipendentiException(String message) {
		super(message);
	}

}
