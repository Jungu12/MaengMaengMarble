package maengmaeng.gamelogicservice.chat.controller;

import maengmaeng.gamelogicservice.global.dto.ResponseDto;
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
	private final static String CHAT = "CHAT";
	private final ChannelTopic chatTopic;
	private final RedisPublisher redisPublisher;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@MessageMapping("/chats")
	public void message(ChatMessage message) {
		logger.debug("message()");
		GameData gameData = GameData.builder().type(CHAT).roomCode(message.getRoomCode()).data(ResponseDto.builder().type("chat").data(message).build()).build();
		redisPublisher.publish(chatTopic, gameData);
	}
}
