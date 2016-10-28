package exception;

/**
 * 
 */
public class RoundOutOfBoundException extends Exception {
    public RoundOutOfBoundException(String message) {
        super(message);
    }

    public RoundOutOfBoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
