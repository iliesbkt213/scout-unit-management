package exceptionPackage;

public class BusinessTaskException extends Exception {

    public BusinessTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessTaskException(String message) {
        super(message);
    }
}
