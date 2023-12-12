package fr.insalyon.city_map_xml;

/**
 * Thrown when an error occurs during the parsing of the XML file. <br/>
 * The error is not due to the content of the XML file but to other reasons
 * (IOException, ...)
 * 
 * @see BadlyFormedXMLException
 */
public class XMLParserException extends Exception {
	/**
	 * Create a new XMLParserException with the given message.
	 * 
	 * @param message The error or warning message.
	 */
	public XMLParserException(String message) {
		super(message);
	}

}
