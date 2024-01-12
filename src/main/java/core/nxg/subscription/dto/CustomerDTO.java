package core.nxg.subscription.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CustomerDTO {
    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private Object metadata;
}
