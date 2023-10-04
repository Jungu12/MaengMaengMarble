package maengmaeng.gamelogicservice.gameRoom.controller;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.PlayerSeq;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.StockRequest;
import maengmaeng.gamelogicservice.gameRoom.service.GameRoomService;
import maengmaeng.gamelogicservice.gameRoom.service.StockService;
import maengmaeng.gamelogicservice.global.dto.GameData;
import maengmaeng.gamelogicservice.global.dto.ResponseDto;
import maengmaeng.gamelogicservice.util.RedisPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class StockController {
    private final RedisPublisher redisPublisher;
    private final StockService stockService;
    private final GameRoomService gameRoomService;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ChannelTopic gameRoomTopic;

    @MessageMapping("/stock/purchase/{roomCode}")
    public void purchase(@DestinationVariable String roomCode, StockRequest stockRequest){

        // 입력받은 사람이 주식 구매
        logger.info("Controller : purchase()");
        logger.info("roomCode : {} ",roomCode);
        stockService.purchase(roomCode,stockRequest);


        GameInfo gameInfo = gameRoomService.getInfo(roomCode);

        GameData gameData = GameData.builder()
                .data(ResponseDto.builder()
                        .type("자유")
                        .data(gameInfo)
                        .build())
                .roomCode(roomCode)
                .type("GAME_ROOM")
                .build();

        redisPublisher.publish(gameRoomTopic, gameData);

    }

    @MessageMapping("/stock/sell/{roomCode}")
    public void sell(@DestinationVariable String roomCode, StockRequest stockRequest){
        logger.info("CONTROLLER : sell()");

        // 입력받은 사람이 주식 매도
        stockService.sell(roomCode,stockRequest);


        GameInfo gameInfo = gameRoomService.getInfo(roomCode);

        GameData gameData = GameData.builder()
                .data(ResponseDto.builder()
                        .type("자유")
                        .data(gameInfo)
                        .build())
                .roomCode(roomCode)
                .type("GAME_ROOM")
                .build();

        redisPublisher.publish(gameRoomTopic, gameData);

    }
}
