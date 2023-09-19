package core.nxg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

// import org.springframework.data.jpa.repository.Modifying;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
// import org.springframework.transaction.annotation.Transactional;
import core.nxg.entity.User;
import java.util.List;
import core.nxg.entity.Role;

// import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByUserType(Role userType);
}
