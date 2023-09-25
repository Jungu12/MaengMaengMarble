package maengmaeng.gamelogicservice.lobby.controller;

import java.util.List;

import maengmaeng.gamelogicservice.global.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.global.dto.GameData;
import maengmaeng.gamelogicservice.lobby.domain.dto.WaitingRoomCreateRequest;
import maengmaeng.gamelogicservice.lobby.domain.dto.WaitingRoomCreateResponse;
import maengmaeng.gamelogicservice.lobby.domain.dto.WaitingRoomListResponse;
import maengmaeng.gamelogicservice.lobby.service.LobbyService;
import maengmaeng.gamelogicservice.util.RedisPublisher;
import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/maeng/lobby")
public class LobbyRestController {
	private static final String LOBBY = "LOBBY";
	private final LobbyService lobbyService;
	private final RedisPublisher redisPublisher;
	private final ChannelTopic lobbyTopic;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 게임 대기방을 생성하고 모든 게임 대기방 목록을 반환
	 * @param roomInfo
	 * @return
	 */
	@PostMapping("/rooms")
	public ResponseEntity<?> waitingRoomCreate(@RequestBody WaitingRoomCreateRequest roomInfo) {
		logger.debug("waitingRoomCreate()");

		lobbyService.saveNewWaitingRoom(roomInfo);
		List<WaitingRoom> waitingRooms = lobbyService.findWaitingRooms();

		GameData gameData = GameData.builder()
			.type(LOBBY)
			.data(ResponseDto.builder().type("WAITINGROOMLIST").data(WaitingRoomListResponse.builder().waitingRooms(waitingRooms).build()).build())
			.build();

		//구독자들에게 대기 방 전체 목록 pub
		redisPublisher.publish(lobbyTopic, gameData);

		//방 생성한
		return ResponseEntity.ok().body(WaitingRoomCreateResponse.builder().roomCode(waitingRooms.get(0).getCode()).build());
	}

	@GetMapping()
	public ResponseEntity<?> waitingRoomList() {
		logger.debug("waitingRoomList()");

		return ResponseEntity.ok().body(WaitingRoomListResponse.builder().waitingRooms(lobbyService.findWaitingRooms()).build());
	}

	//방장이 게임 대기방을 나갈 때, 해당 대기방을 지우고 전체 게임 대기방 목록을 발행
	@DeleteMapping("/rooms/{roomCode}")
	public ResponseEntity<?> waitingRoomRemove(@PathVariable String roomCode) {
		logger.debug("waitingRoomRemove(), roomCode = {}", roomCode);

		lobbyService.removeWaitingRoom(roomCode);

		List<WaitingRoom> waitingRooms = lobbyService.findWaitingRooms();

		GameData gameData = GameData.builder()
			.type(LOBBY)
			.data(ResponseDto.builder().type("WAITINGROOMLIST").data(WaitingRoomListResponse.builder().waitingRooms(waitingRooms).build()).build())
			.build();

		//구독자들에게 대기 방 전체 목록 pub
		redisPublisher.publish(lobbyTopic, gameData);

		return ResponseEntity.ok().build();
	}
}
