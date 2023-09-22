package maengmaeng.gamelogicservice.util;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.GameData;

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
			// 발행된 데이터를 받아 deserialize
//			String publishMessage = (String)redisTemplate.getStringSerializer().deserialize(message.getBody());
//			String topic = new String(pattern);
//			if("WAITING_ROOM".equals(topic)){
//				messagingTemplate.convertAndSend("/sub/waitingRooms"+,publishMessage);
//			}
//			GameData gameData = objectMapper.readValue(publishMessage, GameData.class);
			// destination으로 data 전송
//			if (gameData.getType().equals("WAITING_ROOM")) {
				//messagingTemplate.convertAndSend("/sub/waitingRooms",publishMessage);
//				log.debug(gameData.getRoomCode());
//			}

			// 발행된 데이터를 받아 deserialize
			String publishMessage = (String)redisTemplate.getStringSerializer().deserialize(message.getBody());
			GameData gameData = objectMapper.readValue(publishMessage, GameData.class); // string을 다시 gamedata로 변환

			// 토픽명 확인
			String topic = new String(pattern);
			if("WAITINGROOM".equals(topic)){ // WAITINGROOM토픽으로 전송된 메세지이면
				// "/sub/waitingRooms{roomCode}"를 구독하고 있는 사람들한테 publishMessage, 즉 발행된 메세지를 전송해서 모두 볼 수 있게 함
				messagingTemplate.convertAndSend("/sub/waitingRooms"+gameData.getRoomCode(),publishMessage);
			}


		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
