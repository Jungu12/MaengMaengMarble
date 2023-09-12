package maengmaeng.userservice.user.repository;

import maengmaeng.userservice.user.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Integer> {

}
