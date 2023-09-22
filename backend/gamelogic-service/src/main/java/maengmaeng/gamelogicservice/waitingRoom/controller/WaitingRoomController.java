package maengmaeng.gamelogicservice.waitingRoom.controller;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import maengmaeng.gamelogicservice.global.dto.GameData;
import maengmaeng.gamelogicservice.util.RedisPublisher;
import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.UserInfo;
import maengmaeng.gamelogicservice.waitingRoom.service.WaitingRoomService;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WaitingRoomController {
    private final WaitingRoomService waitingRoomService;
    private final RedisPublisher redisPublisher;
    private final ChannelTopic waitingRoomTopic;


    @MessageMapping("/lobby/{roomCode}")
    public void enter(@DestinationVariable String roomCode, UserInfo user) {
        // 방 입장 : 입장해서 모든 로직처리를 다 진행 (대기방(WaitingRoom) 정보에 사용자 추가하기)
        waitingRoomService.enter(roomCode, user);

        // 현재 방의 게임데이터를 불러오기
        WaitingRoom waitingRoom = waitingRoomService.getWaitingRoomNow(roomCode);

        // 현재 방의 최신 게임데이터 생성
        GameData gameData = GameData.builder()
                .data(waitingRoom)
                .roomCode(roomCode)
                .build();

      // WAITINGROOM topic에 gameData를 넣어서 발행하기
        redisPublisher.publish(waitingRoomTopic, gameData);
    }

}
