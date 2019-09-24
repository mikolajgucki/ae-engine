package com.andcreations.ae.studio.util;

/**
 * @author Mikolaj Gucki
 */
public abstract class ExceptionRunnable implements Runnable {
	/** */
	private Exception exception;
	
	/** */
	protected abstract void runWithException() throws Exception;
	
	/** */
	public void run() {
		try {
			runWithException();
		} catch (Exception exception) {
			this.exception = exception;
		}
	}
	
	/** */
	public Exception getException() {
		return exception;
	}
}
