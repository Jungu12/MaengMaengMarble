package maengmaeng.gamelogicservice.gameRoom.repository;

import maengmaeng.gamelogicservice.gameRoom.domain.db.DbCountry;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DbCountryRespository extends JpaRepository<DbCountry,Long> {


}
