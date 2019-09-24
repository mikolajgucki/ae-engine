package com.andcreations.iab;

/**
 * Thrown if a signature sent back from the Google server cannot be verified.
 *
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class SignatureVerificationException extends Exception {
	/** */
	SignatureVerificationException(String message,Throwable cause) {
		super(message,cause);
	}
}
