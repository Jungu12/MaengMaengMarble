package maengmaeng.gamelogicservice.lobby.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.global.dto.GameData;
import maengmaeng.gamelogicservice.lobby.domain.dto.WaitingRoomCreateRequest;
import maengmaeng.gamelogicservice.lobby.service.LobbyService;
import maengmaeng.gamelogicservice.util.RedisPublisher;
import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lobby")
public class LobbyRestController {
	private final LobbyService lobbyService;
	private final RedisPublisher redisPublisher;
	private final ChannelTopic lobbyTopic;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@PostMapping("/rooms")
	public void waitingRoomCreate(@RequestBody WaitingRoomCreateRequest roomInfo) {
		logger.debug("waitingRoomCreate()");

		WaitingRoom waitingRoom = lobbyService.createWaitingRoom(roomInfo.getUserInfo(), roomInfo.getTitle());

		GameData gameData = GameData.builder()
			.type("LOBBY")
			.roomCode(waitingRoom.getCode())
			.data(waitingRoom)
			.build();

		redisPublisher.publish(lobbyTopic, gameData);
	}
}
