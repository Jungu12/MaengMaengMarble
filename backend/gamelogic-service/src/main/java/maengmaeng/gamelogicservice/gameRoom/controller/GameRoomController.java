package maengmaeng.gamelogicservice.gameRoom.controller;

import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;
import maengmaeng.gamelogicservice.gameRoom.domain.StartCard;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.Dice;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.GameStart;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.PlayerCount;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.PlayerSeq;
import maengmaeng.gamelogicservice.gameRoom.service.GameRoomService;
import maengmaeng.gamelogicservice.global.dto.GameData;
import maengmaeng.gamelogicservice.global.dto.ResponseDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.util.RedisPublisher;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Controller
public class GameRoomController {
	private final RedisPublisher redisPublisher;
	private final GameRoomService gameRoomService;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final ChannelTopic gameRoomTopic;

	/**
	 *  Game 시작
	 *  게임이 시작되고 턴 순서를 정하기위해 카드를 전송
	 * */
	@Transactional
	@MessageMapping("/game-rooms/start/{roomCode}")
	public void setPlayer(@DestinationVariable String roomCode, PlayerCount playerCount) {
		System.out.println(roomCode);

		GameStart cards = gameRoomService.setStart(roomCode, playerCount.getCnt());

		GameData gameData = GameData.builder()
			.type("GAME_ROOM")
			.data(ResponseDto.builder().type("플레이순서").data(cards).build())
			.roomCode(roomCode)
			.build();

		redisPublisher.publish(gameRoomTopic, gameData);

	}

	/**
	 *  플레이어가 카드를 골랐을 때 순서 세팅
	 * */
	@MessageMapping("/game-rooms/set-player/{roomCode}")
	public void setPlayer(@DestinationVariable String roomCode, PlayerSeq playerSeq) {
		System.out.println("setPlayer");

		StartCard[] startCards = gameRoomService.setPlayer(roomCode, playerSeq);
		GameInfo gameInfo = gameRoomService.getInfo(roomCode);
		int num = gameInfo.getInfo().getPlayerCnt();
		GameData gameData = GameData.builder()
			.type("GAME_ROOM")
			.roomCode(roomCode)
			.data(ResponseDto.builder().type("플레이순서").data(GameStart.builder().cards(startCards).build()).build())
			.build();
		redisPublisher.publish(gameRoomTopic, gameData);
		boolean check = true;

		// 모든 플레이어가 순서를 정했으면 게임정보 전송
		for(StartCard startCard : startCards){
			if(!startCard.isSelected()){
				check = false;
			}
		}
		if (check) {
			System.out.println("게임 세팅 완료");
			redisPublisher.publish(gameRoomTopic, GameData.builder()
				.type("GAME_ROOM")
				.roomCode(roomCode)
				.data(ResponseDto.builder().type("초기게임정보").data(gameInfo).build())
				.build());
		}

	}

	/**
	 * GameInfo 데이터 가져오기
	 * */
	@MessageMapping("/game-rooms/get-info/{roomCode}")
	public void setGame(@DestinationVariable String roomCode) {

		GameInfo gameInfo = gameRoomService.getInfo(roomCode);

		GameData gameData = GameData.builder()
			.data(ResponseDto.builder()
				.type("게임정보")
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

		ResponseDto responseDto = gameRoomService.rollDice(roomCode);
		GameData gameData = GameData.builder()
			.type("GAME_ROOM")
			.roomCode(roomCode)
			.data(responseDto)
			.build();
		redisPublisher.publish(gameRoomTopic, gameData);
	}

	/**
	 * 맹맹지급
	 *
	 * */
	@MessageMapping("/game-rooms/maengmaeng/{roomCode}")
	public void maengMaeng(@DestinationVariable String roomCode) {

		GameInfo gameInfo = gameRoomService.getInfo(roomCode);
		ResponseDto responseDto = gameRoomService.maengMaeng(gameInfo);
		GameData gameData = GameData.builder()
			.type("GAME_ROOM")
			.roomCode(roomCode)
			.data(responseDto)
			.build();
		redisPublisher.publish(gameRoomTopic, gameData);

	}

	/**
	 * 구매
	 */
	@MessageMapping("/game-rooms/purchase/{roomCode}")
	public void purchaseLandAndBuildings(@DestinationVariable String roomCode, boolean[] purchasedBuildings) {

		ResponseDto responseDto = gameRoomService.purchaseAndUpdateGameInfo(roomCode, purchasedBuildings);

		GameData gameData = GameData.builder()
			.type("GAME_ROOM")
			.roomCode(roomCode)
			.data(responseDto)
			.build();

		redisPublisher.publish(gameRoomTopic, gameData);
	}

	/**
	 * 통행료 지불을 위해 매각
	 */
	@MessageMapping("/game-rooms/for-sale/{roomCode}/{landId}")
	public void forSale(@DestinationVariable String roomCode, @DestinationVariable int landId) {

		ResponseDto responseDto = gameRoomService.forSale(roomCode, landId);

		GameData gameData = GameData.builder()
			.type("GAME_ROOM")
			.roomCode(roomCode)
			.data(responseDto)
			.build();

		redisPublisher.publish(gameRoomTopic, gameData);
	}

	/**
	 * 통행료 지불
	 */
	@MessageMapping("/game-rooms/fee/{roomCode}")
	public void payFee(@DestinationVariable String roomCode) {

		ResponseDto responseDto = gameRoomService.payFee(roomCode);

		GameData gameData = GameData.builder()
			.type("GAME_ROOM")
			.roomCode(roomCode)
			.data(responseDto)
			.build();

		redisPublisher.publish(gameRoomTopic, gameData);
	}

	/**
	 * 인수
	 */
	@MessageMapping("/game-rooms/take-over/{roomCode}")
	public void takeOver(@DestinationVariable String roomCode) {

		ResponseDto responseDto = gameRoomService.takeOver(roomCode);

		GameData gameData = GameData.builder()
			.type("GAME_ROOM")
			.roomCode(roomCode)
			.data(responseDto)
			.build();

		redisPublisher.publish(gameRoomTopic, gameData);
	}

	/** 거래 정지에서 주사위 굴리기
	 *
	 * */
	@MessageMapping("/game-rooms/stopTrade/{roomCode}")
	public void stopTrade(@DestinationVariable String roomCode) {

		ResponseDto responseDto = gameRoomService.stopTrade(roomCode);

		GameData gameData = GameData.builder()
			.type("GAME_ROOM")
			.roomCode(roomCode)
			.data(responseDto)
			.build();
		redisPublisher.publish(gameRoomTopic, gameData);

	}

	/**
	 * 황금 열쇠 고르기
	 */
	@MessageMapping("/game-rooms/golden-keys/{roomCode}")
	public void chooseGoldenKeys(@DestinationVariable String roomCode) {
		gameRoomService.chooseGoldenKeys(roomCode);
	}

	/**
	 * 이동후 로직
	 * */
	@MessageMapping("/game-rooms/afterMove/{roomCode}")
	public void afterMove(@DestinationVariable String roomCode){
		// 도착한 땅의 위치에 따라 행동 변환

		ResponseDto responseDto = gameRoomService.afterMove(roomCode);
		GameData gameData = GameData.builder()
				.type("GAME_ROOM")
				.roomCode(roomCode)
				.data(responseDto)

				.build();

		redisPublisher.publish(gameRoomTopic,gameData);

	}

	/**
	 * 턴 종료
	 * */

	@MessageMapping("/game-rooms/turn-end/{roomCode}")
	public void endTurn(@DestinationVariable String roomCode) {
		ResponseDto responseDto = gameRoomService.endTurn(roomCode);

		GameData gameData = GameData.builder()
				.type("GAME_ROOM")
				.roomCode(roomCode)
				.data(responseDto)
				.build();

		redisPublisher.publish(gameRoomTopic,gameData);


	}

}
