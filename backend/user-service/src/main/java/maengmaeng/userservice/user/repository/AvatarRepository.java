package maengmaeng.userservice.user.repository;

import maengmaeng.userservice.user.domain.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvatarRepository extends JpaRepository<Avatar, Integer> {
    List<Avatar> findAll();
}
