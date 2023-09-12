package maengmaeng.userservice.myinfo.repository;

import maengmaeng.userservice.myinfo.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Integer> {

}
