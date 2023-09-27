package maengmaeng.gamelogicservice.gameRoom.repository;

import maengmaeng.gamelogicservice.gameRoom.domain.db.DbNewsStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbNewsStockRepository extends JpaRepository<DbNewsStock,Long> {

}
