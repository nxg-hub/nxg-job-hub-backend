package core.nxg.dto;

import lombok.*;

@Data
@RequiredArgsConstructor
public class EmailDTO {
    private String message;
    @Override
    public String toString() {

        return "UserDto [ address = " + ", message = " + message + "]";
    }


    
    
}
