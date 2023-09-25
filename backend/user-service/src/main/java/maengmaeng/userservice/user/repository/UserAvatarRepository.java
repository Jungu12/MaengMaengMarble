package maengmaeng.userservice.user.repository;

import maengmaeng.userservice.user.domain.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAvatarRepository extends JpaRepository<UserAvatar, Integer> {

    @Query("SELECT ua.avatar.avatarId FROM UserAvatar ua WHERE ua.user.userId = :userId")
    List<Integer> findUserAvatarsByUserId(@Param("userId") String userId);

    // userId를 기준으로 마운트된 아바타의 "mounting" 값을 false로 변경
    @Modifying
    @Query("UPDATE UserAvatar ua SET ua.mounting = false WHERE ua.user = :userId")
    void unmountAvatarByUserId(@Param("userId") String userId);

    // userId와 avatarId를 기준으로 해당 아바타의 "mounting" 값을 true로 변경
    @Modifying
    @Query("UPDATE UserAvatar ua SET ua.mounting = true WHERE ua.user = :userId AND ua.avatar = :avatarId")
    void mountAvatarByUserIdAndAvatarId(@Param("userId") String userId, @Param("avatarId") int avatarId);


    boolean existsByUserUserIdAndAvatarAvatarId(String userId, int avatarId);

}
