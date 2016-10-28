package exception;

/**
 * 
 */
public class FeatureDoesntExistException extends Exception{
        public FeatureDoesntExistException(String message) {
            super(message);
        }

        public FeatureDoesntExistException(String message, Throwable cause) {
            super(message, cause);
        }
}
