package maengmaeng.gamelogicservice.gameRoom.controller;


import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;
import maengmaeng.gamelogicservice.gameRoom.service.GameRoomService;
import maengmaeng.gamelogicservice.gameRoom.service.TaxService;
import maengmaeng.gamelogicservice.global.dto.GameData;
import maengmaeng.gamelogicservice.global.dto.ResponseDto;
import maengmaeng.gamelogicservice.util.RedisPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class TaxController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TaxService taxService;
    private final GameRoomService gameRoomService;
    private final RedisPublisher redisPublisher;
    private final ChannelTopic gameRoomTopic;

    @MessageMapping("/game-rooms/tax/{roomCode}")
    public void tax(@DestinationVariable String roomCode){
        taxService.pay(roomCode);


        GameInfo gameInfo = gameRoomService.getInfo(roomCode);

        GameData gameData = GameData.builder()
                .data(ResponseDto.builder()
                        .type("세금 징수")
                        .data(gameInfo)
                        .build())
                .roomCode(roomCode)
                .type("GAME_ROOM")
                .build();

        redisPublisher.publish(gameRoomTopic, gameData);
    }
}
