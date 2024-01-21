package core.nxg.subscription;

import core.nxg.entity.User;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @ManyToOne(targetEntity = User.class)
    private User user;


}
