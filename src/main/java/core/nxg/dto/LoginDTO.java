package core.nxg.dto;

import lombok.*;


@Data
@RequiredArgsConstructor
public class LoginDTO {

    private String username;
    private String password;
    private String token;


    }
