package core.nxg.utils.constants;

public class EmailConstant {

    public static final String PASSWORD_RESET_CONTENT = "<html>"
    + "<br> Dear [[name]],<br>"
    + "<head>"
    + "<style>"
    + "body {"
    + "  font-family: Arial, Helvetica, sans-serif;"
    + "  font-size: 1rem;"
    + "  line-height: 1.6;"
    + "  color: #000;"
    + "}"
    + "</style>"
    + "</head>"
    + "<body style=\"text-align: center;\">"
    + " <div style=\"margin: 0 auto; width: 50%;\">"
    + "   <h1 style=\"font-weight: bold;\">Reset Your Password!</h1>"
    + "   <p>Your request to <strong>Reset your password!</strong> To get started, please click the button.</p>"
    + "   <a href=\"[[URL]]\" style=\"text-decoration: none;\">"
    + "     <button style=\"background-color: #007BFF; color: #fff; font-weight: bold; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; transition: background-color 0.3s;\">Verify Your Email</button>"
    + "   </a>"
    + " </div>"
    + " <p style=\"margin-top: 50px;\">If you did not make this request please report this incident.</p>"
    + " <p>Thanks,</p>"
    + " <p style=\"font-weight: bold;\">The NXG-JOB HUB Team</p>"
    + "</body>"
    + "</html>";

    public static final String VERIFICATION_EMAIL_CONTENT = "<html>"
    + "<br> Dear [[name]],<br>"
    + "<head>"
    + "<style>"
    + "body {"
    + "  font-family: Arial, Helvetica, sans-serif;"
    + "  font-size: 1rem;"
    + "  line-height: 1.6;"
    + "  color: #000;"
    + "}"
    + "</style>"
    + "</head>"
    + "<body style=\"text-align: center;\">"
    + " <div style=\"margin: 0 auto; width: 50%;\">"
    + "   <h1 style=\"font-weight: bold;\">Verify your email address</h1>"
    + "   <p>Welcome to <strong>NXG-JOB HUB</strong>! To get started, please click the button below to verify your email address.</p>"
    + "   <a href=\"[[URL]]\" style=\"text-decoration: none;\">"
    + "     <button style=\"background-color: #007BFF; color: #fff; font-weight: bold; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; transition: background-color 0.3s;\">Verify Your Email</button>"
    + "   </a>"
    + " </div>"
    + " <p style=\"margin-top: 50px;\"><strong> This email expires in 24 hrs. If you did not create an account using this address, please ignore it.<strong></p>"
    + " <p>Thanks,</p>"
    + " <p style=\"font-weight: bold;\">The NXG-JOB HUB Team</p>"
    + "</body>"
    + "</html>";


    
}
