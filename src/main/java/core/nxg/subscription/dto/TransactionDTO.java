package core.nxg.subscription.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {

   private String reference;
   private int amount;
    private  String email;
    private String plan;
    private String callback_url;
}
