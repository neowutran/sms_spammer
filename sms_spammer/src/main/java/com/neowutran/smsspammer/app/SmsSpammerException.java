package com.neowutran.smsspammer.app;

/**
 * Created by draragar on 4/15/14.
 */
public class SmsSpammerException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new mini project exception.
     */
    public SmsSpammerException() {

        super();
    }

    /**
     * Instantiates a new mini project exception.
     *
     * @param message the message
     */
    public SmsSpammerException(final String message) {

        super(message);
    }

    /**
     * Instantiates a new mini project exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public SmsSpammerException(final String message, final Throwable cause) {

        super(message, cause);
    }

    /**
     * Instantiates a new mini project exception.
     *
     * @param cause the cause
     */
    public SmsSpammerException(final Throwable cause) {

        super(cause);
    }
}

