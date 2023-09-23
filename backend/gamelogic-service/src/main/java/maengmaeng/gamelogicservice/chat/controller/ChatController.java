package maengmaeng.gamelogicservice.chat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.chat.dto.ChatMessage;
import maengmaeng.gamelogicservice.global.dto.GameData;
import maengmaeng.gamelogicservice.util.RedisPublisher;

@RequiredArgsConstructor
@Controller
public class ChatController {
	private static ChannelTopic chatTopic;
	private final RedisPublisher redisPublisher;
	private final ChatService chatService;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@MessageMapping("/chats")
	public void message(ChatMessage message) {
		GameData gameData = GameData.builder().roomCode(message.getRoomCode()).data(message).build();
		redisPublisher.publish(chatTopic, gameData);
	}
}
