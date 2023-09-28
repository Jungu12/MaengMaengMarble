package maengmaeng.gamelogicservice.gameRoom.repository;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;
import maengmaeng.gamelogicservice.gameRoom.domain.Info;
import maengmaeng.gamelogicservice.gameRoom.domain.Land;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;
import maengmaeng.gamelogicservice.gameRoom.domain.db.DbCountry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class GameInfoRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, GameInfo> opsHashGameInfo;
    private static final String GAME_INFOS = "gameInfo";

    @PostConstruct
    private void init() {
        opsHashGameInfo = redisTemplate.opsForHash();
    }


    /**
    *  게임 정보 저장
     * */
    public GameInfo createGameRoom(GameInfo gameInfo){

        opsHashGameInfo.put(GAME_INFOS,gameInfo.getRoomCode(),gameInfo);
        return gameInfo;
    }


    /**
     * 게임 정보 가져오기
    * */
    public GameInfo getGameInfo(String roomCode){

        GameInfo gameInfo= opsHashGameInfo.get(GAME_INFOS,roomCode);
        return gameInfo;

    }










}
