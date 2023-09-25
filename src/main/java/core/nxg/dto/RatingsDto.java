package core.nxg.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class RatingsDto {
    private String ratingsID;
    private String employerID;
    private String rating;
}
