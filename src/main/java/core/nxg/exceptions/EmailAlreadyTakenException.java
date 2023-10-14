package core.nxg.exceptions;

public class EmailAlreadyTakenException extends RuntimeException{

    public EmailAlreadyTakenException(String message){
        super(message);
    }
}
