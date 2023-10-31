package core.nxg.serviceImpl;

import core.nxg.dto.RatingsDto;
import core.nxg.entity.Employer;
import core.nxg.entity.Ratings;
import core.nxg.enums.Rating;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.EmployerRepository;
import core.nxg.repository.RatingsRepository;
import core.nxg.service.RatingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingsServiceImpl implements RatingsService {

    private final RatingsRepository ratingsRepository;
    private final EmployerRepository employerRepository;

@Override
public RatingsDto createRatings(Long Id, RatingsDto ratingsDto) {
    Employer employer = employerRepository.findById(Id)
            .orElseThrow(() -> new NotFoundException("Employer with id " + Id + " not found"));

    Ratings ratings = new Ratings();
    ratings.setEmployer(employer);
    ratings.setRating(Rating.valueOf(ratingsDto.getRating())); // Assuming Rating is an enum type

    Ratings savedRatings = ratingsRepository.save(ratings);
    return mapToDto(savedRatings);
}


    @Override
    public List<RatingsDto> getRatingsForEmployer(Long Id) {
        Employer employer = employerRepository.findById(Id)
                .orElseThrow(() -> new NotFoundException("Employer with id " + Id + " not found"));

        List<Ratings> ratingsForEmployer = ratingsRepository.findAllByEmployer(employer);

        return ratingsForEmployer.stream().map(this::mapToDto).collect(Collectors.toList());
    }

//    @Override
//    public List<RatingsDto> getAllRatings() {
//        List<Ratings> allRatings = ratingsRepository.findAll();
//        return allRatings.stream().map(this::mapToDto).collect(Collectors.toList());
//    }


    @Override
    public RatingsDto getRatingsById(Long Id) {
        Ratings ratings = ratingsRepository.findById(Id)
                .orElseThrow(() -> new NotFoundException("Ratings with ID " + Id + " not found"));
        return mapToDto(ratings);
    }

//    @Override
//    public void updateRatings(Long ratingsId, RatingsDto ratingsDto) {
//        Ratings ratings = ratingsRepository.findById(ratingsId)
//                .orElseThrow(() -> new NotFoundException("Ratings with ID " + ratingsId + " not found"));
//        ratings.setRating(Rating.valueOf(ratingsDto.getRating()));
//
//        ratingsRepository.save(ratings);
//    }

    @Override
    public void deleteRatings(Long ratingsId) {
        ratingsRepository.findById(ratingsId).ifPresent(ratingsRepository::delete);
    }

    private RatingsDto mapToDto(Ratings ratings) {
        RatingsDto ratingsDto = new RatingsDto();
        ratingsDto.setId(String.valueOf(ratings.getEmployer().getId()));
        ratingsDto.setRating(ratings.getRating().name());
        return ratingsDto;
    }
}



