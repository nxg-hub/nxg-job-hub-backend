package core.nxg.subscription.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationDTO {
    private String email;
    private String account_number;
    private String bvn;
    private String bank_code;
    private String first_name;
    private String last_name;
    private String customer_code;

}
