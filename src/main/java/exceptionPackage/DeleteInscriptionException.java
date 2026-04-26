package exceptionPackage;

public class DeleteInscriptionException extends Exception {

    public DeleteInscriptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteInscriptionException(String message) {
        super(message);
    }
}
