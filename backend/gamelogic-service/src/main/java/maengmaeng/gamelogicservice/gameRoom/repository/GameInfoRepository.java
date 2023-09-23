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

    private final RedisTemplate <String,GameInfo> redisTemplate;
    private HashOperations<String, String, GameInfo> opsHashGameInfo;
    private static final String GAME_INFOS = "gameInfo";

    @PostConstruct
    private void init() {
        opsHashGameInfo = redisTemplate.opsForHash();
    }

    public GameInfo createGameRoom(){
        GameInfo gameInfo = new GameInfo();
        /*더미 데이터 */
        Player player1 = Player.builder()
                .playerId("ksg")
                .nickname("ksg")
                .money(100000000)
                .asset(100000000)
                .lands(new ArrayList<Integer>())
                .alive(true)
                .currentTurn(0)
                .stopTradeCount(0)
                .doubleCount(0)
                .currentLap(0)
                .stocks(new ArrayList<Map<String, Integer>>())
                .loan(0)
                .cards(new boolean[0])
                .currentLocation(0)
                .build();
        Player player2 = Player.builder()
                .playerId("LEE")
                .nickname("LEE")
                .money(100000000)
                .asset(100000000)
                .lands(new ArrayList<Integer>())
                .alive(true)
                .currentTurn(0)
                .stopTradeCount(0)
                .doubleCount(0)
                .currentLap(0)
                .stocks(new ArrayList<Map<String, Integer>>())
                .loan(0)
                .cards(new boolean[0])
                .currentLocation(0)
                .build();
        Player[] players = new Player[4];
        players[0] = player1;
        players[1] = player2;
        gameInfo = GameInfo.builder()
                .roomCode("roomCode")
                .players(players)
                .lands()
                .info()
                .goldenKeys()
                .newsInfo()
                .stocks()
                .build();

        opsHashGameInfo.put(GAME_INFOS,gameInfo.getRoomCode(),gameInfo);
    }




}
