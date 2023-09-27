package maengmaeng.gamelogicservice.gameRoom.controller;

import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.Dice;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.GameStart;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.PlayerCount;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.PlayerSeq;
import maengmaeng.gamelogicservice.gameRoom.service.GameRoomService;
import maengmaeng.gamelogicservice.global.dto.GameData;
import maengmaeng.gamelogicservice.global.dto.ResponseDto;
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
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	@MessageMapping("/game-rooms/start/{roomCode}")
	public void setPlayer(@DestinationVariable String roomCode, PlayerCount playerCount){
		System.out.println(roomCode);

		GameStart cards = gameRoomService.setStart(roomCode, playerCount.getCnt());


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
	public void setPlayer(@DestinationVariable String roomCode , PlayerSeq playerSeq){
		System.out.println("setPlayer");

		Player[] players = gameRoomService.setPlayer(roomCode, playerSeq);
		GameInfo gameInfo = gameRoomService.getInfo(roomCode);
		int num = gameInfo.getInfo().getPlayerCnt();
		int cnt =0;
		GameData gameData = GameData.builder()
				.type("GAME_ROOM")
				.roomCode(roomCode)
				.data(ResponseDto.builder().type("player").data(players).build()).build();
		redisPublisher.publish(gameRoomTopic,gameData);

		for(Player player : players){
			if(player !=null){
				cnt++;
			}
		}
		if(cnt==num){
			System.out.println("게임 세팅 완료");
			redisPublisher.publish(gameRoomTopic,GameData.builder().type("GAME_ROOM").roomCode(roomCode).data(ResponseDto.builder().type("message").data("모든 플레이어가 들어왔어요").build()).build());
		}

	}

	/**
	 * 초기 GameInfo 데이터 가져오기
	 * */
	@MessageMapping("/game-rooms/get-info/{roomCode}")
	public void setGame(@DestinationVariable String roomCode) {


		GameInfo gameInfo = gameRoomService.getInfo(roomCode);

		GameData gameData = GameData.builder()
				.data(ResponseDto.builder()
						.type("gameInfo")
						.data(gameInfo)
						.build())
				.roomCode(roomCode)
				.type("GAME_ROOM")
				.build();

		redisPublisher.publish(gameRoomTopic, gameData);
	}

	/**
	 * 주사위 굴리는 pub
	 * */
	@MessageMapping("/game-rooms/roll/{roomCode}")
	public void rollDice(@DestinationVariable String roomCode) {

		Dice dice = gameRoomService.rollDice(roomCode);
		GameData gameData = GameData.builder()
				.type("GAME_ROOM")
				.roomCode(roomCode)
				.data(ResponseDto.builder()
						.type("dice")
						.data(dice)
						.build())
				.build();

		redisPublisher.publish(gameRoomTopic,gameData);

	}




	/**
	 * 턴 종료
	 * */

	@MessageMapping("/game-rooms/turn-end/{roomCode}")
	public void endTurn(@DestinationVariable String roomCode) {




	}









}
