package core.nxg.service;

import core.nxg.dto.RatingsDto;
import core.nxg.entity.Ratings;

import java.util.List;

public interface RatingsService {
    RatingsDto createRatings(RatingsDto ratingsDto);

    List<RatingsDto> getRatingsForEmployer(Long employerId);

    List<RatingsDto> getAllRatings();

    List<Ratings> getAllRating();

    RatingsDto getRatingsById(Long ratingsId);

    void updateRatings(Long ratingsId, RatingsDto ratingsDto);

    void deleteRatings(Long ratingsId);
}
