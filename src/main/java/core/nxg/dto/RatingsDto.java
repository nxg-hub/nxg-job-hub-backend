package core.nxg.dto;

import core.nxg.entity.Employer;
import core.nxg.entity.User;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingsDto {
//    private String ratingsId;
    private String Id;
    private String rating;

    @OneToOne(mappedBy = "ratings")
    private Employer employer;
}
