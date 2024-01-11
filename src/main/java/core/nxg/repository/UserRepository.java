package core.nxg.repository;

import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import core.nxg.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    UserResponseDto findByEmailAndEnabledTrue(String email);

    Optional<User> findByEmailAndProvider(String email, Provider provider);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);

    List<User> findAll();


    boolean existsByPhoneNumber(String phoneNumber);
}

