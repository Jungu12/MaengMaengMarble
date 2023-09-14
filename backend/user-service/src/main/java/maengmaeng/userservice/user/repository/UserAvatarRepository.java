package maengmaeng.userservice.user.repository;

import maengmaeng.userservice.user.domain.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAvatarRepository extends JpaRepository<UserAvatar, Integer> {

    @Query(value = "SELECT uc.character FROM user_character uc WHERE uc.user_id = :userId", nativeQuery = true)
    List<Integer> findUserAvatarsByUserId(@Param("userId") String userId);
}
