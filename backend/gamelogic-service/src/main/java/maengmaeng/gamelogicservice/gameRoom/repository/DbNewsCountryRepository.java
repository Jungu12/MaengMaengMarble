package maengmaeng.gamelogicservice.gameRoom.repository;

import maengmaeng.gamelogicservice.gameRoom.domain.db.DbNews;
import maengmaeng.gamelogicservice.gameRoom.domain.db.DbNewsCountry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DbNewsCountryRepository extends JpaRepository<DbNewsCountry, Long> {

//    List<DbNewsCountry> findByDbNewsNews(DbNews dbNews);


}
