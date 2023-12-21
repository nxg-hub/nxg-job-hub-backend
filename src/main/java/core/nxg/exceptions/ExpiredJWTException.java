package core.nxg.exceptions;

public class ExpiredJWTException extends RuntimeException{

    private String message = "Invalid or Expired JWT Token";
    public ExpiredJWTException(String message) {
        this.message = message;

    }

    @Override
    public String getMessage() {
        return message;
    }
}



