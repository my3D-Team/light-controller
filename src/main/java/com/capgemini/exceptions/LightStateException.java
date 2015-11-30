package com.capgemini.exceptions;

/**
 * Light state exception.
 */
public class LightStateException extends Exception {

    /**
     * Build the exception type.
     * @param message is the message.
     */
    public LightStateException(final String message) {
        super(message);
    }
}
