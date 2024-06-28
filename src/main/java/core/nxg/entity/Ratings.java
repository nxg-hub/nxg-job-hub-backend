package core.nxg.entity;

import core.nxg.enums.Rating;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@RequiredArgsConstructor
@Getter
@Setter
@Document(collection = "ratings")
public class Ratings {
    @Id
    private Long id;

    private Rating rating;

//    yToOne
//    @JoinColumn(name = "employer_id")
    private Employer employer;

//    yToOne
    private User rater;

    @Getter
    private Long raterID;




}
