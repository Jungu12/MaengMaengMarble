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
	 *  게임이 시작되고 턴 순서를 정하기위해 카드를 전송
	 * */
	@MessageMapping("/game-rooms/start/{roomCode}")
	public void setPlayer(@DestinationVariable String roomCode){
		System.out.println(roomCode);
		// TODO: WaitingRoom이 작성되면 참여 인원 불러와서 인원 만큼 카드 제공

		int cnt =4;
		GameStart cards = gameRoomService.setStart(roomCode, cnt);


		GameData gameData = GameData.builder()
				.type("GAME_ROOM")
				.data(ResponseDto.builder().type("startCard").data(cards).build())
				.roomCode(roomCode)
				.build();

		redisPublisher.publish(gameRoomTopic, gameData);

	}
	/**
	*  플레이어가 카드를 골랐을 때 순서 세팅
	* */
	@MessageMapping("/game-rooms/set-player/{roomCode}")
	public void setPlayer(@DestinationVariable String roomCode , UserInfo userInfo, int num){



//		GameData gameData = GameData.builder()
//				.type("GAME_ROOM")
//				.roomCode(roomCode)
//				.data().build();
	}
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
