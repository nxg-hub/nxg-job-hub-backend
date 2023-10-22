package core.nxg.dto;

import lombok.*;

@Data
@RequiredArgsConstructor
public class EmailDTO {
    private String email;
    private String message;
    @Override
    public String toString() {

        return "UserDto [ address = " + email + ", message = " + message + "]";
    }


    
    
}
