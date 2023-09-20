package maengmaeng.gamelogicservice.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;

@Service
@RequiredArgsConstructor
public class RedisPublisher {
	private final RedisTemplate<String, Object> redisTemplate;

	public void publish(ChannelTopic topic, GameInfo gameInfo) {
		redisTemplate.convertAndSend(topic.getTopic(), gameInfo);
	}
}
