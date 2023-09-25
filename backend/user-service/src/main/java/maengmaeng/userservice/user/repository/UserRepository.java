package maengmaeng.userservice.user.repository;

import maengmaeng.userservice.user.domain.Avatar;
import maengmaeng.userservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.OptionalInt;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUserId(String userId);

    Optional<User> findByUserId(String userId);

    Optional<User> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    @Query("SELECT ua.avatar FROM UserAvatar ua WHERE ua.user.userId = :userId AND ua.mounting = true")
    Avatar findMountedAvatarIdByUserId(@Param("userId") String userId);
}