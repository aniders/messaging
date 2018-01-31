package com.sample.messagingservice.exception;

/**
 * M4essaging service Exception
 * 
 * @author arawo
 * 
 */
public class MessagingServiceException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public MessagingServiceException() {
        super();
    }

    public MessagingServiceException(String message) {
        super(message);
    }

    public MessagingServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessagingServiceException(Throwable cause) {
        super(cause);
    }
}
