package exceptionPackage;

public class AddInscriptionException extends Exception {

    public AddInscriptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddInscriptionException(String message) {
        super(message);
    }
}
