package maengmaeng.userservice.user.repository;

import maengmaeng.userservice.user.domain.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar, Integer> {

}
