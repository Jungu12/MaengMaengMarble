package maengmaeng.gamelogicservice.waitingRoom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.UserInfo;
import maengmaeng.gamelogicservice.waitingRoom.exception.ExceptionCode;
import maengmaeng.gamelogicservice.waitingRoom.exception.WaitingRoomException;
import maengmaeng.gamelogicservice.waitingRoom.repository.WaitingRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import maengmaeng.gamelogicservice.util.RedisSubscriber;

@RequiredArgsConstructor
@Service
public class WaitingRoomService {
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private final WaitingRoomRepository waitingRoomRepository;
    private final ChannelTopic gameRoomTopic;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /*
        방 입장
     */
    public void enter(String roomCode, UserInfo userInfo) {
        // 대기방(WaitingRoom) 정보에 사용자 추가하기
        waitingRoomRepository.addNewMember(roomCode, userInfo);
    }

    /*
        현재 대기방 정보 얻기
     */
    public WaitingRoom getWaitingRoomNow(String roomCode) {
        return waitingRoomRepository.getWaitingRoomNow(roomCode);
    }

    public List<WaitingRoom> getWaitingRooms() {
        Map<String, WaitingRoom> waitingRoomMap = waitingRoomRepository.getWaitingRooms();
        List<WaitingRoom> waitingRooms = new ArrayList<>();

        waitingRoomMap.forEach((roomCode, waitingRoom) -> {
            waitingRooms.add(waitingRoom);
        });

        return waitingRooms;
    }

    /*
        대기방 Redis에 저장
     */
    public void saveWaitingRoom(WaitingRoom waitingRoom) {
        waitingRoomRepository.saveWaitingRoom(waitingRoom);
    }

    public void removeWaitingRoom(String roomCode) {
        waitingRoomRepository.removeWaitingRoom(roomCode);
    }

    public void ready(String roomCode, UserInfo user) {
        waitingRoomRepository.readyMember(roomCode, user);
    }


}