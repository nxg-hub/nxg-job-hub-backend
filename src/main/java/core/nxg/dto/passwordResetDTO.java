package core.nxg.dto;

//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.Size;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class passwordResetDTO {


    @NotNull(message = "Please provide a new password equal to or greater than 6 characters")
    private String newPassword;

    @NotNull(message = "Please provide a confirm password")
    private String confirmPassword;

}
