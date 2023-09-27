package maengmaeng.gamelogicservice.waitingRoom.repository;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.waitingRoom.domain.CurrentParticipant;
import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.UserInfo;
import maengmaeng.gamelogicservice.waitingRoom.exception.ExceptionCode;
import maengmaeng.gamelogicservice.waitingRoom.exception.WaitingRoomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class WaitingRoomRepository {
    private static final String WAITING_ROOMS = "WAITING_ROOM";
    private static final String JOINED_PERSON = "JOINEDPERSON";
    //Redis
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, WaitingRoom> opsHashWaitingRoom;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    private void init() {
        opsHashWaitingRoom = redisTemplate.opsForHash();
    }

    public void addNewMember(String roomCode, UserInfo userInfo) {

        // 대기방을 캐시에 저장해야하는 이유는? 서로 다른 서버에서 해당 대기방을 공유하기 위해서

        // <String, String, WaitingRoom> : "WAITINGROOM", roomCode , 대기방정보
        // 사용자가 들어온 기존의 대기방 정보를 찾는다.
        WaitingRoom waitingRoom = opsHashWaitingRoom.get(WAITING_ROOMS, roomCode); // WAITING_ROOM : roomCode : WaitingRoom(entity:대기방정보)


        // 현재대기방 정보가 없으면 새로만들어줌
        if (waitingRoom == null) {
            waitingRoom = WaitingRoom.builder()
                    .code(roomCode)
                    .currentParticipants(new ArrayList<>())
                    .build();
            logger.info("현재 대기방 정보가 없어서 새로 만들어 줌");
        }

        // 현재 참여하고있는 사용자가 4명이면 더이상 참여할 수 없음
        if (waitingRoom.getCurrentParticipants().size() == 4) {
            throw new WaitingRoomException(ExceptionCode.WAITINGROOM_FULLED);
        }


        // 입장한 사용자 객체 생성
        CurrentParticipant currentParticipants = CurrentParticipant.builder()
                .characterId(userInfo.getCharacterId())
                .userId(userInfo.getUserId())
                .nickname(userInfo.getNickname())
                .ready(false)
                .closed(false)
                .build();

        // 대기방 정보에 현재 들어온 사람 추가
        waitingRoom.addCurrentParticipants(currentParticipants);

        // 업데이트된 대기방 정보를 레디스에 업데이트

        // 근데 이렇게 저장을 안해주고 바로 쏴주면 안되는가에 대한 고찰
        // 그치만 저장을 해야하는 이유는 사방에서 하도 들어왔다나갔다 채팅했다안했다 레디눌렀다안눌렀다해서 현재상태를 저장해놓지않으면 모르기때문이 아닐까?
        // 대기방의 상태(사용자의 입장, 퇴장, 채팅 등)가 지속적으로 변하기 때문에, 현재 상태를 실시간으로 파악하기 위해서는 Redis와 같은 저장소에 상태를 저장해두어야 합니다.
        saveWaitingRoom(waitingRoom);
    }

    public void saveWaitingRoom(WaitingRoom waitingRoom) {
        opsHashWaitingRoom.put(WAITING_ROOMS, waitingRoom.getCode(), waitingRoom);
    }

    public WaitingRoom getWaitingRoomNow(String roomCode) {
        return opsHashWaitingRoom.get(WAITING_ROOMS, roomCode);
    }


    public Map<String, WaitingRoom> getWaitingRooms() { return opsHashWaitingRoom.entries(WAITING_ROOMS); }


    public synchronized void readyMember(String roomCode, UserInfo userInfo) {
        WaitingRoom waitingRoom = opsHashWaitingRoom.get(WAITING_ROOMS,roomCode);

        for(int i = 0 ; i < waitingRoom.getCurrentParticipants().size() ; i++) {
            if(waitingRoom.getCurrentParticipants().get(i).getUserId().equals(userInfo.getUserId())){
                // 지금 안에 들어있는 사용자와 ready누른 사용자가 같을때
                // 그 사람 상태 변경
                CurrentParticipant participant = waitingRoom.getCurrentParticipants().get(i);
                opsHashWaitingRoom.put(WAITING_ROOMS,roomCode,waitingRoom);
                break;
            }
        }

    }

    public void removeWaitingRoom(String roomCode) {
        System.out.println("roomCode =" + roomCode);
        opsHashWaitingRoom.delete(WAITING_ROOMS, roomCode);
    }
}
