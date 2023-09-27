package maengmaeng.gamelogicservice.gameRoom.repository;

import maengmaeng.gamelogicservice.gameRoom.domain.db.DbStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbStockRepository extends JpaRepository<DbStock,Long> {

}
