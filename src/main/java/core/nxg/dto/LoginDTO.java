package core.nxg.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginDTO {

    private String username;
    private String password;
    private String token;

}
