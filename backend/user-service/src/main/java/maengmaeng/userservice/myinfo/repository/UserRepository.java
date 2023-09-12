package maengmaeng.userservice.myinfo.repository;

import maengmaeng.userservice.myinfo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUserId(String userId);

    boolean existsByNickname(String nickname);
}
