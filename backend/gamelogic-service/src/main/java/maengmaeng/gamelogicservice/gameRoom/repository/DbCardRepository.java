package maengmaeng.gamelogicservice.gameRoom.repository;


import maengmaeng.gamelogicservice.gameRoom.domain.db.DbCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbCardRepository extends JpaRepository<DbCard, Long> {

}
