package core.nxg.exceptions;

import jakarta.servlet.ServletException;

public class ExpiredJWTException extends ServletException {

    private String message = "Invalid or Expired JWT Token";
    public ExpiredJWTException(String message) {
        this.message = message;

    }

    @Override
    public String getMessage() {
        return message;
    }
}



