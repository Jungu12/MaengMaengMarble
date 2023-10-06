package maengmaeng.gamelogicservice.gameRoom.repository;

import maengmaeng.gamelogicservice.gameRoom.domain.db.DbNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DbNewsRepository extends JpaRepository<DbNews, String> {
    List<DbNews> findByNewsType(String newsType);

}
