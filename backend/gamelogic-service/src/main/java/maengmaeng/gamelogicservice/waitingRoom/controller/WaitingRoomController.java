package maengmaeng.gamelogicservice.waitingRoom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maengmaeng.gamelogicservice.global.dto.ResponseDto;
import maengmaeng.gamelogicservice.lobby.domain.dto.WaitingRoomListResponse;
import maengmaeng.gamelogicservice.lobby.service.LobbyService;
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

import java.util.List;


@Slf4j
@Controller
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
        // 유저가 exit 누른 것을 변경하기
        waitingRoomService.exit(roomCode, user);

        // 현재 방의 게임데이터를 불러오기
        WaitingRoom waitingRoom = waitingRoomService.getWaitingRoomNow(roomCode);


        // 한명 남았는데 퇴장했을 때
        logger.info("한명일때 waitingRoom 상태 : {}",waitingRoom);
        if(waitingRoom==null){
            // 한명일때 방이 사라지면 로비로 방이 없어졌다는 걸 알려주기 (로비로 전체 게임방 데이터 다시 전송)
            List<WaitingRoom> waitingRooms = lobbyService.findWaitingRooms();

            logger.info("{}", waitingRooms.get(0).getTitle());
            logger.info("한명일때 방이 사라져서 로비로 방이 없어짐");
            GameData gameData = GameData.builder()
                    .type(LOBBY)
                    .data(ResponseDto.builder().type("lobby").data(WaitingRoomListResponse.builder().waitingRooms(waitingRooms).build()).build())
                    .build();

            //구독자들에게 대기 방 전체 목록 pub
            redisPublisher.publish(lobbyTopic, gameData);
        }else {

            // 여러명 남아있을때는 안에 들어있는 사람들 정보를 다 줘야한다
            // 현재 방의 최신 게임데이터 생성
            GameData gameData = GameData.builder()
                    .data(ResponseDto.builder().type("waitingRoom").data(waitingRoom).build())
                    .roomCode(roomCode)
                    .type(WAITINGROOM)
                    .build();


            // WAITINGROOM topic에 gameData를 넣어서 발행하기 : 메세지를 redis topic에 발행
            redisPublisher.publish(waitingRoomTopic, gameData);
        }
    }

    // 어차피 프론트에서 방장만 퇴장가능하니까 해당부분을 따로 내가 확인해줘야하는가?
    @MessageMapping("/waiting-rooms/kick/{roomCode}")
    public void kick(@DestinationVariable String roomCode, UserInfo user, String outUser) {
        // 유저가 kick 눌러서 내보내기
        waitingRoomService.kick(roomCode, user, outUser);

        WaitingRoom waitingRoom = waitingRoomService.getWaitingRoomNow(roomCode);

        GameData gameData = GameData.builder()
                .data(ResponseDto.builder().type("waitingRoom").data(waitingRoom).build())
                .roomCode(roomCode)
                .type(WAITINGROOM)
                .build();



        redisPublisher.publish(waitingRoomTopic, gameData);


    }

}
