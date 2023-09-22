 package maengmaeng.gamelogicservice.gameRoom.repository;

 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.data.redis.core.HashOperations;
 import org.springframework.data.redis.core.RedisTemplate;
 import org.springframework.stereotype.Repository;

 import lombok.RequiredArgsConstructor;
 import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;

 @RequiredArgsConstructor
 @Repository
 public class GameRoomRepository {
 	private static final String GAME_ROOM = "gameRoom";
 	private final RedisTemplate<String, Object> redisTemplate;
 	private HashOperations<String, String, GameInfo> opsHashGameInfo;
 	private final Logger logger = LoggerFactory.getLogger(getClass());


 }
