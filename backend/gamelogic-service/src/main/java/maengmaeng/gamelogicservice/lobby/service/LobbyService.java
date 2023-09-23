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

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LobbyService {
    private final static int MAX_PARTICIPANTS = 4;
    private final WaitingRoomService waitingRoomService;
    private final LobbyRepository lobbyRepository;
    // 로비(topic)에 발행되는 메시지를 처리할 Listener
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public String createRoomCode() {
        return UUID.randomUUID().toString();
    }

    public void saveNewWaitingRoom(WaitingRoomCreateRequest roomInfo) {
        CurrentParticipant currentParticipant = CurrentParticipant.builder()
            .userId(roomInfo.getUserInfo().getUserId())
            .nickname(roomInfo.getUserInfo().getNickname())
            .characterId(roomInfo.getUserInfo().getCharacterId())
            .closed(false)
            .ready(false)
            .build();

        WaitingRoom waitingRoom = WaitingRoom.builder()
            .code(createRoomCode())
            .title(roomInfo.getTitle())
            .build();

        //방장을 CurrentParticipants에 추가
        waitingRoom.addCurrentParticipants(currentParticipant);

        //닫힌 자리를 CurrentParticipants에 추가
        for (int cnt = 0; cnt < MAX_PARTICIPANTS - roomInfo.getMaxParticipants(); cnt++) {
            CurrentParticipant closed = CurrentParticipant.builder()
                .closed(true)
                .build();

            waitingRoom.addCurrentParticipants(closed);
        }

        waitingRoomService.saveWaitingRoom(waitingRoom);
    }

    public List<WaitingRoom> findWaitingRooms() {
        return waitingRoomService.getWaitingRooms();
    }

    public void removeWaitingRoom(String roomCode) {
        waitingRoomService.removeWaitingRoom(roomCode);
    }
}
