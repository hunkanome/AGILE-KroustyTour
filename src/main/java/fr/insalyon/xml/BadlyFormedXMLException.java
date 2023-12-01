package fr.insalyon.xml;

/**
 * Thrown when an error occurs during the parsing of the XML file. <br/>
 * The error is due to the content of the XML file (unknown tag or attribute,
 * incoherent data, ...)
 * 
 * @see XMLParserException
 */
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
