package com.capgemini.exceptions;

/**
 * ArNet service exception.
 */
public class ArtNetServiceException extends Exception {

    /**
     * Build the exception type.
     * @param message is the message.
     */
    public ArtNetServiceException(final String message) {
        super(message);
    }

    /**
     * Build by exception.
     * @param exception is the exception.
     */
    public ArtNetServiceException(final Exception exception) {
        super(exception);
    }
}
