package maengmaeng.gamelogicservice.lobby.controller;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.lobby.service.LobbyService;
import maengmaeng.gamelogicservice.util.RedisPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class LobbyController {
    private final RedisPublisher redisPublisher;
    private final LobbyService lobbyService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    //대기방 생성
    @MessageMapping("/lobby/rooms/{roomCode}")
    public void waitingRoomCreate(@DestinationVariable String roomCode) {

    }

}
