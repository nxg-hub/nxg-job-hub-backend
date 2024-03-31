package core.nxg.subscription.dto;


import core.nxg.subscription.enums.PlanType;
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

    private String planType;

//    private Object metadata;

}
