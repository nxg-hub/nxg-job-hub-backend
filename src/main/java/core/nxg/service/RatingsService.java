package core.nxg.service;

import core.nxg.dto.RatingsDto;
import core.nxg.entity.Ratings;

import java.util.List;

public interface RatingsService {
    RatingsDto createRatings(RatingsDto ratingsDto);

    List<RatingsDto> getRatingsForEmployer(Long employerId);

    RatingsDto getRatingsById(Long Id);

    void deleteRatings(Long ratingsId);

//    List<RatingsDto> getRatingsForEmployer(Long employerId);
//
//    List<RatingsDto> getAllRatings();
//
//    List<Ratings> getAllRating();
//
//    void deleteRatings(Long ratingsId);
}
