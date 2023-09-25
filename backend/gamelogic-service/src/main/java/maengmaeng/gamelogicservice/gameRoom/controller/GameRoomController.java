package maengmaeng.gamelogicservice.gameRoom.controller;

import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.GameStart;
import maengmaeng.gamelogicservice.gameRoom.service.GameRoomService;
import maengmaeng.gamelogicservice.global.dto.GameData;
import maengmaeng.gamelogicservice.global.dto.ResponseDto;
import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.UserInfo;
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

import java.nio.file.attribute.UserPrincipal;
import java.util.Random;

@RequiredArgsConstructor
@Controller
public class GameRoomController {
	private final RedisPublisher redisPublisher;
	private final GameRoomService gameRoomService;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final WaitingRoomService waitingRoomService;
	private final ChannelTopic gameRoomTopic;


	/**
	 *  Game 시작
	 *  게임이 시작되었다는 것을 알림
	 * */
//	@MessageMapping("/game-rooms/start/{roomCode}")
//	public void startGame(@DestinationVariable String roomCode) {
//
//		GameData gameData = GameData.builder()
//				.data(GameStart.builder().message("start").build())
//				.roomCode(roomCode)
//				.type("GAME_ROOM")
//				.build();
//
//		redisPublisher.publish(gameRoomTopic, gameData);
//
//
//
//	}
	/**
	 * 초기 맵 데이터 세팅
	 * */
	@MessageMapping("/game-rooms/set-info/{roomCode}")
	public void setGame(@DestinationVariable String roomCode) {

		GameInfo gameInfo = gameRoomService.setInfo(roomCode);

		GameData gameData = GameData.builder()
				.data(ResponseDto.builder()
						.type("MAPINFO")
						.data(gameInfo)
						.build())
				.roomCode(roomCode)
				.type("GAME_ROOM")
				.build();

		redisPublisher.publish(gameRoomTopic, gameData);
	}

	/**
	 * 턴 종료
	 * */

	@MessageMapping("/game-rooms/turn-end/{roomCode}")
	public void endTurn(@DestinationVariable String roomCode) {



	}









}
