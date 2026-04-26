package exceptionPackage;

public class SearchInscriptionException extends Exception {

    public SearchInscriptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchInscriptionException(String message) {
        super(message);
    }
}
