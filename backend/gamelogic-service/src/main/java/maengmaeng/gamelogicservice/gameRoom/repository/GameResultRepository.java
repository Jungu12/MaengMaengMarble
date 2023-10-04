package maengmaeng.gamelogicservice.gameRoom.repository;

import maengmaeng.gamelogicservice.gameRoom.domain.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResultRepository extends JpaRepository<GameResult, Integer> {
}
