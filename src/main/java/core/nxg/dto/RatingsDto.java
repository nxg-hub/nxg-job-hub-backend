package core.nxg.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RatingsDto {
    private String ratingsID;
    private String employerID;
    private String rating;
}
