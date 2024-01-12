package core.nxg.subscription;

import core.nxg.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "customers")
public class Subscriber {

    @Id
    private String customerId;

    private String email;

    @ManyToOne(targetEntity = User.class)
    private User user;


}
