package core.nxg.repository;

// import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> existsByEmail (String email);

    List<User> findAll();



}