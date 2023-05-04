package it.prova.gestionesocieta.eccezioni;

public class SocietaNotExistException extends Exception{

	private static final long serialVersionUID = 1L;

	public SocietaNotExistException() {
	}

	public SocietaNotExistException(String message) {
		super(message);
	}
}
