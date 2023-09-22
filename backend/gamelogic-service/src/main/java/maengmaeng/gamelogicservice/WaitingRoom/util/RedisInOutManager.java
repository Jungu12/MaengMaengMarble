package maengmaeng.gamelogicservice.waitingRoom.util;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RedisInOutManager {
    // 근데 이거는 안씀
    private final RedisTemplate<String, String> redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(getClass());
   	// 사용자를 특정 topic에 구독자로 등록
    // 특정 토픽에 사용자를 추가하는 로직을 구현
    // topic == roomCode
    public void addUserToTopic(String topic, UserInfo userInfo){
        redisTemplate.opsForHash().put("WaitingRoom",topic,userInfo.getUserId());
    }

    // 사용자가 특정 topic에 구독자인지 확인
    public boolean isUserSubscribedToTopic(String topic, String userId) {
        return redisTemplate.opsForHash().hasKey(topic, userId);
    }

    // 사용자를 특정 topic의 구독자에서 제거
    public void removeUserFromTopic(String topic, String userId) {
        redisTemplate.opsForHash().delete(topic, userId);
        logger.debug("UserExist = {}", isUserSubscribedToTopic(topic, userId));
    }

    // 특정 topic의 모든 구독자 조회
    public Map<Object, Object> getAllSubscribersForTopic(String topic) {
        return redisTemplate.opsForHash().entries(topic);
    }
}
