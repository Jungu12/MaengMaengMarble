package maengmaeng.gamelogicservice.gameRoom.controller;

import maengmaeng.gamelogicservice.gameRoom.service.GameRoomService;
import maengmaeng.gamelogicservice.waitingRoom.service.WaitingRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
// import maengmaeng.gamelogicservice.gameRoom.service.GameRoomService;
import maengmaeng.gamelogicservice.util.RedisPublisher;

@RequiredArgsConstructor
@Controller
public class GameRoomController {
	private final RedisPublisher redisPublisher;
	private final GameRoomService gameRoomService;
	private final Logger logger = LoggerFactory.getLogger(getClass());
//	private final WaitingRoomService waitingRoomService;
	private final ChannelTopic gameRoomTopic;

	@MessageMapping("/gamerooms/start/{roomCode}")
	public void qq(@DestinationVariable String roomCode) {

		// gameRoomService.setGameInfos(roomCode);

		System.out.println("test");
	}
}
