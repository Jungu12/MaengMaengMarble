package maengmaeng.gamelogicservice.waitingRoom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maengmaeng.gamelogicservice.global.dto.ResponseDto;
import maengmaeng.gamelogicservice.lobby.domain.dto.WaitingRoomListResponse;
import maengmaeng.gamelogicservice.lobby.service.LobbyService;
import maengmaeng.gamelogicservice.util.RedisPublisher;
import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;
import maengmaeng.gamelogicservice.global.dto.GameData;

import maengmaeng.gamelogicservice.waitingRoom.domain.dto.ChangeStateRequest;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.OutUser;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.UserInfo;
import maengmaeng.gamelogicservice.waitingRoom.service.WaitingRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
public class WaitingRoomController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WaitingRoomService waitingRoomService;
    private final RedisPublisher redisPublisher;
    private final ChannelTopic waitingRoomTopic;
    private final ChannelTopic lobbyTopic;
    private final LobbyService lobbyService;
    private final static String WAITINGROOM = "WAITING_ROOM";

    private final static String LOBBY = "LOBBY";


    @MessageMapping("/waiting-rooms/{roomCode}")
    public void enter(@DestinationVariable String roomCode, UserInfo user) {
        // 방 입장 : 입장해서 모든 로직처리를 다 진행 (대기방(WaitingRoom) 정보에 사용자 추가하기)
        waitingRoomService.enter(roomCode, user);

        // 현재 방의 게임데이터를 불러오기
        WaitingRoom waitingRoom = waitingRoomService.getWaitingRoomNow(roomCode);

        // 현재 방의 최신 게임데이터 생성
        GameData gameData = GameData.builder()
                .data(ResponseDto.builder().type("waitingRoom").data(waitingRoom).build())
                .roomCode(roomCode)
                .type(WAITINGROOM)
                .build();

        // WAITINGROOM topic에 gameData를 넣어서 발행하기
        redisPublisher.publish(waitingRoomTopic, gameData);


        // 전체 방 목록 반환
        roomList();

        logger.debug("방 입장");
    }

    @MessageMapping("/waiting-rooms/ready/{roomCode}")
    public void ready(@DestinationVariable String roomCode, UserInfo user) {
        // 유저가 ready를 누른 것을 변경하기
        waitingRoomService.ready(roomCode, user);

        // 현재 방의 게임데이터를 불러오기
        WaitingRoom waitingRoom = waitingRoomService.getWaitingRoomNow(roomCode);

        // 현재 방의 최신 게임데이터 생성
        GameData gameData = GameData.builder()
                .data(ResponseDto.builder().type("waitingRoom").data(waitingRoom).build())
                .roomCode(roomCode)
                .type(WAITINGROOM)
                .build();


        // WAITINGROOM topic에 gameData를 넣어서 발행하기 : 메세지를 redis topic에 발행
        redisPublisher.publish(waitingRoomTopic, gameData);
    }

    @MessageMapping("/waiting-rooms/exit/{roomCode}")
    public void exit(@DestinationVariable String roomCode, UserInfo user){
    logger.info("exit() 실행");
        // 현재 방의 게임데이터를 불러오기
        WaitingRoom waitingRoom = waitingRoomService.getWaitingRoomNow(roomCode);

        // 방장이 퇴장할 때 방 폭파
        if(waitingRoom.getCurrentParticipants().get(0).getUserId().equals(user.getUserId())){
            // 방장이 사라지면 로비로 방이 없어졌다는 걸 알려주기 (로비로 전체 게임방 데이터 다시 전송)
            waitingRoomService.removeWaitingRoom(roomCode);


            // 현재 방의 최신 게임데이터 생성
            GameData gameroomData = GameData.builder()
                    .data(ResponseDto.builder().type("방 폭파").data(null).build())
                    .roomCode(roomCode)
                    .type(WAITINGROOM)
                    .build();
            // WAITINGROOM topic에 gameData를 넣어서 발행하기 : 메세지를 redis topic에 발행
            redisPublisher.publish(waitingRoomTopic, gameroomData);

            List<WaitingRoom> waitingRooms = lobbyService.findWaitingRooms();

            // 대기방 목록 반환
            logger.info("방장이 사라져서 로비로 방이 없어짐");
            GameData gameData = GameData.builder()
                    .type(LOBBY)
                    .data(ResponseDto.builder().type("lobby").data(WaitingRoomListResponse.builder().waitingRooms(waitingRooms).build()).build())
                    .build();

            //구독자들에게 대기 방 전체 목록 pub
            redisPublisher.publish(lobbyTopic, gameData);
        }else { // 다른사람이 퇴장할때는 방정보 반환
            waitingRoomService.exit(roomCode, user);
            // 여러명 남아있을때는 안에 들어있는 사람들 정보를 다 줘야한다
            // 현재 방의 최신 게임데이터 생성
            waitingRoom = waitingRoomService.getWaitingRoomNow(roomCode);
            GameData gameData = GameData.builder()
                    .data(ResponseDto.builder().type("waitingRoom").data(waitingRoom).build())
                    .roomCode(roomCode)
                    .type(WAITINGROOM)
                    .build();


            // WAITINGROOM topic에 gameData를 넣어서 발행하기 : 메세지를 redis topic에 발행
            redisPublisher.publish(waitingRoomTopic, gameData);
            roomList();
        }
    }

    @MessageMapping("/waiting-rooms/start/{roomCode}")
    public void start(@DestinationVariable String roomCode){
        boolean startPossible = waitingRoomService.start(roomCode);
        if(startPossible){
            logger.info("게임시작 가능~");
            WaitingRoom waitingRoom = waitingRoomService.getWaitingRoomNow(roomCode);
            GameData gameData = GameData.builder()
                    .data(ResponseDto.builder().type("gameStart").data(null).build())
                    .roomCode(roomCode)
                    .type(WAITINGROOM)
                    .build();

            redisPublisher.publish(waitingRoomTopic, gameData);
        }


    }


    @MessageMapping("/waiting-rooms/kick/{roomCode}")
    public void kick(@DestinationVariable String roomCode, @Payload OutUser outUser){
        // 유저가 kick 눌러서 내보내기
        waitingRoomService.kick(roomCode,outUser.getOutUser());

        WaitingRoom waitingRoom = waitingRoomService.getWaitingRoomNow(roomCode);

        GameData gameData = GameData.builder()
                .data(ResponseDto.builder().type(outUser.getOutUser() + " 강퇴 waitingRoom").data(waitingRoom).build())
                .roomCode(roomCode)
                .type(WAITINGROOM)
                .build();
        redisPublisher.publish(waitingRoomTopic, gameData);

        // 목록리스트에도 업데이트된 정보 보내기
        roomList();

    }

    public void roomList(){
        List<WaitingRoom> waitingRooms = lobbyService.findWaitingRooms();

        logger.info("{}", waitingRooms.get(0).getTitle());

        GameData gameData = GameData.builder()
                .type(LOBBY)
                .data(ResponseDto.builder().type("lobby").data(WaitingRoomListResponse.builder().waitingRooms(waitingRooms).build()).build())
                .build();


        redisPublisher.publish(lobbyTopic, gameData);
    }

    @MessageMapping("/waiting-rooms/state/{roomCode}")
    public void changeState(@DestinationVariable String roomCode, ChangeStateRequest num){
        int th = num.getNum();
        waitingRoomService.changeState(roomCode, th);

        WaitingRoom waitingRoom = waitingRoomService.getWaitingRoomNow(roomCode);

        GameData gameData = GameData.builder()
                .data(ResponseDto.builder().type("waitingRoom").data(waitingRoom).build())
                .roomCode(roomCode)
                .type(WAITINGROOM)
                .build();
        redisPublisher.publish(waitingRoomTopic, gameData);

        // 목록리스트에도 업데이트된 정보 보내기
        roomList();

    }

}
