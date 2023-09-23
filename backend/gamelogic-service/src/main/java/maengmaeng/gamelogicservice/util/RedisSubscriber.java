package maengmaeng.gamelogicservice.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maengmaeng.gamelogicservice.global.dto.GameData;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

	private final RedisTemplate redisTemplate;
	private final ObjectMapper objectMapper;
	private final SimpMessageSendingOperations messagingTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			// 발행된 데이터를 받아 역직렬화
			String publishMessage = (String)redisTemplate.getStringSerializer().deserialize(message.getBody());
			GameData gameData = objectMapper.readValue(publishMessage, GameData.class);

			String topicType = gameData.getType();

			if (topicType.equals("LOBBY")) {
				messagingTemplate.convertAndSend("/sub/lobby", gameData.getData());
			}
			if (topicType.equals("WAITING_ROOM")) {
				messagingTemplate.convertAndSend("/sub/waiting-rooms/" + gameData.getRoomCode(), gameData.getData());
			}
			if (topicType.equals("CHAT")) {
				messagingTemplate.convertAndSend("/sub/chat/" + gameData.getRoomCode(), gameData.getData());
			}
		} catch (Exception e) {
			System.out.println(message);
		}
	}
}
