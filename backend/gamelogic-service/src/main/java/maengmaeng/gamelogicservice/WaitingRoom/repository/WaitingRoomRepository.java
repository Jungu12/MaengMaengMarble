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
import java.util.List;
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


    public void saveWaitingRoom(WaitingRoom waitingRoom) {
        opsHashWaitingRoom.put(WAITING_ROOMS, waitingRoom.getCode(), waitingRoom);
    }

    public WaitingRoom getWaitingRoomNow(String roomCode) {
        return opsHashWaitingRoom.get(WAITING_ROOMS, roomCode);
    }


    public Map<String, WaitingRoom> getWaitingRooms() { return opsHashWaitingRoom.entries(WAITING_ROOMS); }



    public void removeWaitingRoom(String roomCode) {
        System.out.println("roomCode =" + roomCode);
        opsHashWaitingRoom.delete(WAITING_ROOMS, roomCode);
    }


    public void kick(String roomCode, UserInfo user, String outUser) {
        logger.info("kick()");
        WaitingRoom waitingRoom = opsHashWaitingRoom.get(WAITING_ROOMS, roomCode);

        List<CurrentParticipant> afterParticipant = new ArrayList<>();

        for(CurrentParticipant participant : waitingRoom.getCurrentParticipants()){
            if(!participant.getUserId().equals(outUser)){
                afterParticipant.add(participant);
            }
        }

        waitingRoom.setCurrentParticipants(afterParticipant);
        opsHashWaitingRoom.put(WAITING_ROOMS, roomCode, waitingRoom);

    }
}