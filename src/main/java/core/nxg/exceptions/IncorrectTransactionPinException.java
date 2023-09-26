package core.nxg.exceptions;

public class IncorrectTransactionPinException extends RuntimeException {
    public IncorrectTransactionPinException(String message) {
        super(message);
    }
}
