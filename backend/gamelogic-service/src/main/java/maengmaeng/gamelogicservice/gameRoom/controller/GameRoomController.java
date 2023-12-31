package maengmaeng.gamelogicservice.gameRoom.controller;

import lombok.Synchronized;
import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;
import maengmaeng.gamelogicservice.gameRoom.domain.News;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;
import maengmaeng.gamelogicservice.gameRoom.domain.StartCard;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.*;
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

import com.google.common.collect.ForwardingMapEntry;

@RequiredArgsConstructor
@Controller
public class GameRoomController {

    private final RedisPublisher redisPublisher;
    private final GameRoomService gameRoomService;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ChannelTopic gameRoomTopic;

    /**
     * Game 시작
     * 게임이 시작되고 턴 순서를 정하기위해 카드를 전송
     */
    @Transactional
    @MessageMapping("/game-rooms/start/{roomCode}")
    public void start(@DestinationVariable String roomCode, PlayerCount playerCount) {
        logger.info("start(), roomCode = {}, PlayerCnt, = {}", roomCode, playerCount.getCnt());
        GameStart cards = gameRoomService.setStart(roomCode, playerCount.getCnt());
        GameData gameData = GameData.builder()
                .type("GAME_ROOM")
                .data(ResponseDto.builder().type("플레이순서").data(cards).build())
                .roomCode(roomCode)
                .build();

        redisPublisher.publish(gameRoomTopic, gameData);

    }

    /**
     * 플레이어가 카드를 골랐을 때 순서 세팅
     */
    @MessageMapping("/game-rooms/set-player/{roomCode}")
    public void setPlayer(@DestinationVariable String roomCode, PlayerSeq playerSeq) {
        logger.info("setPlayer(), roomCode = {}, userId = {}, nickname = {}, characterId ={}" +
                        ", playerCnt = {}", roomCode, playerSeq.getUserId(), playerSeq.getNickname()
                , playerSeq.getCharacterId(), playerSeq.getPlayerCnt());

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
        for (StartCard startCard : startCards) {
            if (!startCard.isSelected()) {
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
     */
    @MessageMapping("/game-rooms/get-info/{roomCode}")
    public void setGame(@DestinationVariable String roomCode) {
        logger.info("setGame() roomCode = {}", roomCode);

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
     */
    @MessageMapping("/game-rooms/roll/{roomCode}")
    public void rollDice(@DestinationVariable String roomCode) {
        logger.info("roll(), roomCode = {}", roomCode);
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
     */
    @MessageMapping("/game-rooms/maengmaeng/{roomCode}")
    public void maengMaeng(@DestinationVariable String roomCode) {
        logger.info("maengmaneg(), roomCode = {}", roomCode);
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
        logger.info("purchasedBuildings() roomCode = {} ,purchasedBuildings ={}",roomCode, purchasedBuildings.toString());
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
        logger.info("forSale(), roomCode = {}, landId = {}",roomCode,landId);
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
        logger.info("payFee() roomCode = {}", roomCode);
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
        logger.info("takeOver() roomCode = {} ",roomCode);
        ResponseDto responseDto = gameRoomService.takeOver(roomCode);

        GameData gameData = GameData.builder()
                .type("GAME_ROOM")
                .roomCode(roomCode)
                .data(responseDto)
                .build();

        redisPublisher.publish(gameRoomTopic, gameData);
    }

    /**
     * 거래 정지에서 주사위 굴리기
     */
    @MessageMapping("/game-rooms/stop-trade/{roomCode}")
    public void stopTrade(@DestinationVariable String roomCode) {
        logger.info("stopTrade(), roomCode = {}",roomCode);
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
        logger.info("chooseGoldenKeys(), roomCode = {}",roomCode);
        ResponseDto responseDto = gameRoomService.chooseGoldenKeys(roomCode);

        GameData gameData = GameData.builder()
                .type("GAME_ROOM")
                .roomCode(roomCode)
                .data(responseDto)
                .build();

        redisPublisher.publish(gameRoomTopic, gameData);
    }

    /**
     * 뉴스 세개 중 하나 골라서 적용 시키기
     */
    @MessageMapping("/game-rooms/news/{roomCode}")
    public void applyNews(@DestinationVariable String roomCode, ApplyNewsRequest applyNewsRequest) {
        logger.info("applyNews(), roomCode = {}, News ={}, type = {}",roomCode,applyNewsRequest.getNews().toString(), applyNewsRequest.getType());
        ResponseDto responseDto = gameRoomService.applyNews(roomCode, applyNewsRequest.getNews(), applyNewsRequest.getType());

        GameData gameData = GameData.builder()
                .type("GAME_ROOM")
                .roomCode(roomCode)
                .data(responseDto)
                .build();

        redisPublisher.publish(gameRoomTopic, gameData);
    }

    /**
     * 이동후 로직
     */
    @MessageMapping("/game-rooms/after-move/{roomCode}")
    public void afterMove(@DestinationVariable String roomCode) {
        logger.info("afterMove(), roomCode = {}",roomCode);
        // 도착한 땅의 위치에 따라 행동 변환

        ResponseDto responseDto = gameRoomService.afterMove(roomCode);
        GameData gameData = GameData.builder()
                .type("GAME_ROOM")
                .roomCode(roomCode)
                .data(responseDto)

                .build();

        redisPublisher.publish(gameRoomTopic, gameData);

    }

    /**
     * 어디로든 문 (강준구의 문단속 적용 X)
     */

    @MessageMapping("/game-rooms/door/{roomCode}")
    public void door(@DestinationVariable String roomCode, Door door) {
        logger.info("door() roomCode = {}, door ={}",roomCode,door.toString());
        ResponseDto responseDto = gameRoomService.door(roomCode, door);

        GameData gameData = GameData.builder()
                .type("GAME_ROOM")
                .roomCode(roomCode)
                .data(responseDto)
                .build();

        redisPublisher.publish(gameRoomTopic, gameData);

    }

    /**
     * 어디로든 문 (강준구의 문단속 적용 O)
     */

    @MessageMapping("/game-rooms/jungu-door/{roomCode}")
    public void junguDoor(@DestinationVariable String roomCode) {
        logger.info("junguDoor() roomCode = {}",roomCode);
        ResponseDto responseDto = gameRoomService.junguDoor(roomCode);

        GameData gameData = GameData.builder()
                .type("GAME_ROOM")
                .roomCode(roomCode)
                .data(responseDto)
                .build();

        redisPublisher.publish(gameRoomTopic, gameData);

    }

    /**
     * 턴 종료
     */

    @MessageMapping("/game-rooms/end-turn/{roomCode}")
    public void endTurn(@DestinationVariable String roomCode) throws InterruptedException {
        logger.info("endTurn() roomCode = {}", roomCode);
        ResponseDto responseDto = gameRoomService.endTurn(roomCode);

        GameData gameData = GameData.builder()
                .type("GAME_ROOM")
                .roomCode(roomCode)
                .data(responseDto)
                .build();

        Thread.sleep(500);

        redisPublisher.publish(gameRoomTopic, gameData);
    }

	/**
	 * 파산
	 */
	@MessageMapping("/game-rooms/bankruptcy/{roomCode}")
	public void bankruptcy(@DestinationVariable String roomCode) {
		ResponseDto responseDto = gameRoomService.bankruptcy(roomCode);

		GameData gameData = GameData.builder()
			.type("GAME_ROOM")
			.roomCode(roomCode)
			.data(responseDto)
			.build();

		redisPublisher.publish(gameRoomTopic, gameData);
	}

    /**
     * 게임 종료
     */
    @MessageMapping("/game-rooms/end-game/{roomCode}")
    public void endGame(@DestinationVariable String roomCode) {
        logger.info("endGame() roomCode = {}", roomCode);
        ResponseDto responseDto = gameRoomService.endGame(roomCode);

        GameData gameData = GameData.builder()
                .type("GAME_ROOM")
                .roomCode(roomCode)
                .data(responseDto)
                .build();

        redisPublisher.publish(gameRoomTopic, gameData);
    }

}
