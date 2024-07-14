package core.nxg.service;

import core.nxg.dto.RatingsDto;
import core.nxg.entity.Ratings;

import java.util.List;

public interface RatingsService {
    RatingsDto createRatings(RatingsDto ratingsDto);

    List<RatingsDto> getRatingsForEmployer(String employerId);

    RatingsDto getRatingsById(String Id);

    void deleteRatings(String ratingsId);

//    List<RatingsDto> getRatingsForEmployer(Long employerId);
//
//    List<RatingsDto> getAllRatings();
//
//    List<Ratings> getAllRating();
//
//    void deleteRatings(Long ratingsId);
}
