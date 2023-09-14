package maengmaeng.userservice.user.repository;

import maengmaeng.userservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUserId(String userId);

    Optional<User> findByUserId(String userId);

    boolean existsByNickname(String nickname);
}
