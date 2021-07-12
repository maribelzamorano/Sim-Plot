package de.tudarmstadt.dvs.p4.simdb;

/**
 * The Class SimDBException.
 */
public class SimDBException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7882589071312086973L;

	/**
	 * Instantiates a new sim db exception.
	 * 
	 * @param message
	 */
	public SimDBException(final String message) {
		super(message);
	}

	/**
	 * Instantiates a new sim db exception.
	 * 
	 * @param message
	 * @param cause
	 */
	public SimDBException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
