package core.nxg.serviceImpl;

import core.nxg.dto.RatingsDto;
import core.nxg.entity.Employer;
import core.nxg.entity.Notification;
import core.nxg.entity.Ratings;
import core.nxg.entity.User;
import core.nxg.enums.NotificationType;
import core.nxg.enums.Rating;
import core.nxg.enums.UserType;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.EmployerRepository;
import core.nxg.repository.NotificationRepository;
import core.nxg.repository.RatingsRepository;
import core.nxg.service.RatingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingsServiceImpl implements RatingsService {

    private final RatingsRepository ratingsRepository;
    private final EmployerRepository employerRepository;

    private final NotificationRepository notificationRepository;

@Override
public RatingsDto createRatings(RatingsDto ratingsDto) {
    Employer employer = employerRepository.findById(String.valueOf(ratingsDto.getEmployerID()))
            .orElseThrow(() -> new NotFoundException("Employer with id " + ratingsDto.getEmployerID()+ " not found"));

    Ratings ratings = new Ratings();
    ratings.setEmployer(employer);
    ratings.setRaterID(ratingsDto.getRaterID());
    ratings.setRating(Rating.valueOf(ratingsDto.getRating())); // Assuming Rating is an enum type

    Ratings savedRatings = ratingsRepository.save(ratings);
    notify(savedRatings.getId(), employer, employer.getUser());
    return mapToDto(savedRatings);
}

private void notify(Long ratingsID, Employer employer, User sender){

    var notification = Notification.builder()
            .notificationType(NotificationType.RATING)
            .dateTime(LocalDateTime.now())
            .delivered(false)
            .senderID(sender.getId())
            .referencedUserID(employer.getUser().getId()) // we're using User id for ease. Profile
                                                        // picture, fName, lName, would be easy to fetch that way.
            .message("You have a new rating")
            .contentId(ratingsID)
            .build();
    notificationRepository.save(notification);
}



    @Override
    public List<RatingsDto> getRatingsForEmployer(Long Id) {
        Employer employer = employerRepository.findById(String.valueOf(Id))
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
        Ratings ratings = ratingsRepository.findById(String.valueOf(Id))
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
        ratingsRepository.findById(String.valueOf(ratingsId)).ifPresent(ratingsRepository::delete);
    }

    private RatingsDto mapToDto(Ratings ratings) {
        RatingsDto ratingsDto = new RatingsDto();
        ratingsDto.setId(String.valueOf(ratings.getEmployer().getEmployerID()));
        ratingsDto.setRating(ratings.getRating().name());
        return ratingsDto;
    }
}



