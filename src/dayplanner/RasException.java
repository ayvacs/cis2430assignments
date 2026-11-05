package dayplanner;

/**
 * Denotes that an error has occurred with serialization.
 * Full RAS specification is available in README.
 */
public class RasException extends Exception {
    /**
     * Create new RasException with no extra information.
     */
    public RasException() {
        super();
    }

    /**
     * Create new RasException with a message.
     * 
     * @param message The message.
     */
    public RasException(String message) {
        super(message);
    }

    /**
     * Create new RasException with a message and a cause.
     * 
     * @param message The message.
     * @param cause   The cause.
     */
    public RasException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create new RasException with a cause.
     * 
     * @param cause The cause.
     */
    public RasException(Throwable cause) {
        super(cause);
    }
}

