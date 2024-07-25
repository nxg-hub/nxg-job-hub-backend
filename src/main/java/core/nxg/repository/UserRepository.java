package core.nxg.repository;

import core.nxg.configs.oauth2.OAuth2Provider;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import core.nxg.enums.UserType;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    UserResponseDto findByEmailAndEnabledTrue(String email);

    Optional<User> findByEmailAndProvider(@Email String email, OAuth2Provider provider);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    Page<User> findByUserType(UserType userType, Pageable pageable);


    boolean existsByPhoneNumber(String phoneNumber);

    @Query( value ="{ 'registrationDate' : { $gte: ?0 } }", count = true)
    long countNewUsersSince(LocalDateTime date);

    @Query(value = "{}", fields = "{ 'timeOnPlatform' : 1 }" , count = true)
    List<User> findAllUsersForAverageTimeCalculation();

        @Query(value = "{ 'monthJoined' : { $gte: ?0, $lt: ?1 } }", count = true)
        long countUsersByMonth(LocalDate startOfMonth, LocalDate startOfNextMonth);

    List<User> findByRolesAuthority(String authority);
    }



