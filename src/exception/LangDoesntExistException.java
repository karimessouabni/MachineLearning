package exception;

/**
 * 
 */
public class LangDoesntExistException extends Exception{
    public LangDoesntExistException(String message) {
        super(message);
    }

    public LangDoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }
}

