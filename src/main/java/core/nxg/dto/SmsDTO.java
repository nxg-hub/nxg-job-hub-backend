package core.nxg.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SmsDTO {

    private String email;
    private String phoneNumber;
}
