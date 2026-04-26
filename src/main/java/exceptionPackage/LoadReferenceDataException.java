package exceptionPackage;

public class LoadReferenceDataException extends Exception {

    public LoadReferenceDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadReferenceDataException(String message) {
        super(message);
    }
}
