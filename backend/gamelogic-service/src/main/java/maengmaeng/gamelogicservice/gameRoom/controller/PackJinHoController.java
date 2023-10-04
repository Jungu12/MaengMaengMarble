package maengmaeng.gamelogicservice.gameRoom.controller;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.PackJinHoRequest;
import maengmaeng.gamelogicservice.gameRoom.service.GameRoomService;
import maengmaeng.gamelogicservice.gameRoom.service.PackJinHoService;
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
public class PackJinHoController {
    private final RedisPublisher redisPublisher;
    private final PackJinHoService packJinHoService;
    private final GameRoomService gameRoomService;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ChannelTopic gameRoomTopic;


    @MessageMapping("/game-rooms/parkjinho/{roomCode}")
    public void betting(@DestinationVariable String roomCode, PackJinHoRequest packJinHoRequest) {
        logger.info("betting() 함수 시작");
        ResponseDto responseDto = packJinHoService.betting(roomCode, packJinHoRequest);

        GameData gameData = GameData.builder()
                .type("GAME_ROOM")
                .roomCode(roomCode)
                .data(responseDto)
                .build();

        redisPublisher.publish(gameRoomTopic, gameData);
    }
}
