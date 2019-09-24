package com.andcreations.ae.studio.plugins.builders;

/**
 * Thrown when build builder fails. The error handling must be done in
 * the builder itself. The exception is a notification which says to stop
 * processing (e.g. asset pipeline).
 *
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class BuilderException extends RuntimeException {
}