package core.nxg.repository;

import core.nxg.configs.oauth2.OAuth2Provider;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import core.nxg.enums.Provider;
import core.nxg.enums.UserType;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

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
}

