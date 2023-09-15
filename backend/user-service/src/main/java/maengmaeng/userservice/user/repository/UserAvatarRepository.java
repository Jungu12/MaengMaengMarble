package maengmaeng.userservice.user.repository;

import maengmaeng.userservice.user.domain.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAvatarRepository extends JpaRepository<UserAvatar, Integer> {

    @Query("SELECT ua.avatar.avatarId FROM UserAvatar ua WHERE ua.user.userId = :userId")
    List<Integer> findUserAvatarsByUserId(@Param("userId") String userId);


    Boolean existsByUserUserIdAndAvatarAvatarId(String userId, int avatarId);


}
