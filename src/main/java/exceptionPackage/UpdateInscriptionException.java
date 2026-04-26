package exceptionPackage;

public class UpdateInscriptionException extends Exception {

    public UpdateInscriptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateInscriptionException(String message) {
        super(message);
    }
}
