package fr.insalyon.xml;

public class BadlyFormedXMLException extends Exception {
	/**
	 * Create a new BadlyFormedXMLException with the given message.
	 * 
	 * @param message The error or warning message.
	 */
	public BadlyFormedXMLException(String message) {
		super(message);
	}

}
