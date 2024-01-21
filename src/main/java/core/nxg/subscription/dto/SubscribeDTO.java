package core.nxg.subscription.dto;

import core.nxg.subscription.PlanType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SubscribeDTO {
    private  String email;
    private String callback_url;

    private PlanType planType;


}
