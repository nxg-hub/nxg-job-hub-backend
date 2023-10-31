package core.nxg.exceptions;

public class ErrorException extends RuntimeException{
    public ErrorException(String message, Exception e) {
        super(message);
    }
}
