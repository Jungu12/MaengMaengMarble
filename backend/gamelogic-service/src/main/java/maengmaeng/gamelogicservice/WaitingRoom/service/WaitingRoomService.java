package maengmaeng.gamelogicservice.WaitingRoom.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.util.RedisPublisher;
import maengmaeng.gamelogicservice.util.RedisSubscriber;

@RequiredArgsConstructor
@Service
public class WaitingRoomService {

	private final RedisMessageListenerContainer redisMessageListener;
	private final RedisPublisher redisPublisher;
	private final RedisSubscriber redisSubscriber;
	private final Logger logger = LoggerFactory.getLogger(getClass());


}
