package core.nxg.exceptions;

public class AccountExpiredException extends RuntimeException{
    public AccountExpiredException(String message) {
        super(message);
    }
    
}
