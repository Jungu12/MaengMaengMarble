package maengmaeng.gamelogicservice.lobby.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.global.dto.GameData;
import maengmaeng.gamelogicservice.lobby.domain.dto.WaitingRoomCreateRequest;
import maengmaeng.gamelogicservice.lobby.domain.dto.WaitingRoomCreateResponse;
import maengmaeng.gamelogicservice.lobby.service.LobbyService;
import maengmaeng.gamelogicservice.util.RedisPublisher;
import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lobby")
public class LobbyRestController {
	private static final String LOBBY = "LOBBY";
	private final LobbyService lobbyService;
	private final RedisPublisher redisPublisher;
	private final ChannelTopic lobbyTopic;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	//게임 대기방을 생성하고,
	@PostMapping("/rooms")
	public ResponseEntity<?> waitingRoomCreate(@RequestBody WaitingRoomCreateRequest roomInfo) {
		logger.debug("waitingRoomCreate()");

		lobbyService.saveWaitingRoom(roomInfo);
		List<WaitingRoom> waitingRooms = lobbyService.findWaitingRooms();

		GameData gameData = GameData.builder()
			.type(LOBBY)
			.data(waitingRooms)
			.build();

		redisPublisher.publish(lobbyTopic, gameData);

		return ResponseEntity.ok().body(WaitingRoomCreateResponse.builder().roomCode(gameData.getRoomCode()).build());
	}

	//방장이 게임 대기방을 나갈 때, 대기방을 지우는 api
}
