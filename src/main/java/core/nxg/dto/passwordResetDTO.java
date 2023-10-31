package core.nxg.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class passwordResetDTO {

    @Email
    private String email;

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;

}
