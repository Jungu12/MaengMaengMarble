package maengmaeng.gamelogicservice.lobby.service;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.lobby.domain.dto.WaitingRoomCreateRequest;
import maengmaeng.gamelogicservice.lobby.repository.LobbyRepository;
import maengmaeng.gamelogicservice.util.RedisSubscriber;
import maengmaeng.gamelogicservice.waitingRoom.domain.CurrentParticipant;
import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;
import maengmaeng.gamelogicservice.waitingRoom.service.WaitingRoomService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LobbyService {
    private final static int MAX_PARTICIPANTS = 4;
    private final WaitingRoomService waitingRoomService;

    public String createRoomCode() {
        return UUID.randomUUID().toString();
    }

    public String createRoomCreatedTime() {
        Date currentTime = new Date();

        return currentTime.toString();
    }

    public String saveNewWaitingRoom(WaitingRoomCreateRequest roomInfo) {
        WaitingRoom waitingRoom = WaitingRoom.builder()
            .code(createRoomCode())
            .title(roomInfo.getTitle())
            .createdTime(createRoomCreatedTime())
            .build();

//        CurrentParticipant currentParticipant = CurrentParticipant.builder()
//            .userId(roomInfo.getUserInfo().getUserId())
//            .nickname(roomInfo.getUserInfo().getNickname())
//            .characterId(roomInfo.getUserInfo().getCharacterId())
//            .closed(false)
//            .ready(false)
//            .build();

        //방장을 CurrentParticipants에 추가
//        waitingRoom.addCurrentParticipants(currentParticipant);

        int currentParticipants = 1;
        int maxParticipants = roomInfo.getMaxParticipants();

        for (int cnt = 0; cnt < maxParticipants ; cnt++) {
            CurrentParticipant empty = CurrentParticipant.builder().build();

            waitingRoom.addCurrentParticipants(empty);
        }

        //닫힌 자리를 CurrentParticipants에 추가
        currentParticipants = waitingRoom.getCurrentParticipants().size();

        for (int cnt = 0; cnt < MAX_PARTICIPANTS - currentParticipants; cnt++) {
            CurrentParticipant closed = CurrentParticipant.builder()
                .closed(true)
                .build();

            waitingRoom.addCurrentParticipants(closed);
        }

        waitingRoomService.saveWaitingRoom(waitingRoom);

        return waitingRoom.getCode();
    }

    public List<WaitingRoom> findWaitingRooms() {
        return waitingRoomService.getWaitingRooms();
    }

    public void removeWaitingRoom(String roomCode) {
        waitingRoomService.removeWaitingRoom(roomCode);
    }
}
