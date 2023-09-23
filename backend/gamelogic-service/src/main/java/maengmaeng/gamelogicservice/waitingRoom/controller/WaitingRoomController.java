package maengmaeng.gamelogicservice.waitingRoom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maengmaeng.gamelogicservice.util.RedisPublisher;
import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;
import maengmaeng.gamelogicservice.global.dto.GameData;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.UserInfo;
import maengmaeng.gamelogicservice.waitingRoom.service.WaitingRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;


@Slf4j
@Controller
@RequiredArgsConstructor
public class WaitingRoomController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WaitingRoomService waitingRoomService;
    private final RedisPublisher redisPublisher;
    private final ChannelTopic waitingRoomTopic;


    @MessageMapping("/waiting-rooms/{roomCode}")
    public void enter(@DestinationVariable String roomCode, UserInfo user) {
        // 방 입장 : 입장해서 모든 로직처리를 다 진행 (대기방(WaitingRoom) 정보에 사용자 추가하기)
        waitingRoomService.enter(roomCode, user);

        // 현재 방의 게임데이터를 불러오기
        WaitingRoom waitingRoom = waitingRoomService.getWaitingRoomNow(roomCode);

        // 현재 방의 최신 게임데이터 생성
        GameData gameData = GameData.builder()
                .data(waitingRoom)
                .roomCode(roomCode)
                .type("WAITING_ROOM")
                .build();

        // WAITINGROOM topic에 gameData를 넣어서 발행하기
        redisPublisher.publish(waitingRoomTopic, gameData);

        logger.debug("방 입장");
    }


    @MessageMapping("/waiting-rooms/ready/{roomCode}")
    public void ready(@DestinationVariable String roomCode, UserInfo user){
        // 유저가 ready를 누른 것을 변경하기

        waitingRoomService.ready(roomCode, user);

        WaitingRoom waitingRoom = waitingRoomService.getWaitingRoomNow(roomCode);

        // 현재 방의 최신 게임데이터 생성
        GameData gameData = GameData.builder()
                .roomCode(roomCode)
                .type("WAITING_ROOM_CHANE_USER_STATE")
                .data(waitingRoom)
                .build();

        // WAITINGROOM topic에 gameData를 넣어서 발행하기 : 메세지를 redis topic에 발행
        redisPublisher.publish(waitingRoomTopic,gameData);
    }

    @MessageMapping("/waiting-rooms/exit/{roomCode}")
    public void exit(@DestinationVariable String roomCode, UserInfo user){
        waitingRoomService.exit(roomCode, user);
    }

}
