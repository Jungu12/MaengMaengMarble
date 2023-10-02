package maengmaeng.gamelogicservice.waitingRoom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import maengmaeng.gamelogicservice.waitingRoom.domain.CurrentParticipant;
import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.StartResponseDto;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.UserInfo;
import maengmaeng.gamelogicservice.waitingRoom.exception.ExceptionCode;
import maengmaeng.gamelogicservice.waitingRoom.exception.WaitingRoomException;
import maengmaeng.gamelogicservice.waitingRoom.repository.WaitingRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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
        WaitingRoom waitingRoom = waitingRoomRepository.getWaitingRoomNow(roomCode);

        if (waitingRoom == null) {
            logger.info("해당 대기방이 없습니다.");
            throw new WaitingRoomException(ExceptionCode.WAITINGROOM_NOT_FOUND);
        }

        // 현재 참여자들 리스트를 돌면서 들어갈 자리가 있는지 확인
        boolean enterPossible = false;
        for (CurrentParticipant participant : waitingRoom.getCurrentParticipants()) {
            // 참여가능한 경우 참가자배열을 돌면서 빈자리에 들어가기
            if (participant.getNickname() == null && !participant.isClosed()) {
                participant.setUserId(userInfo.getUserId());
                participant.setNickname(userInfo.getNickname());
                participant.setCharacterId(userInfo.getCharacterId());
                participant.setClosed(false);
                participant.setReady(false);
                enterPossible = true;
                break;
            }
        }

        // 참여할 수 없으면 메세지 전송
        if (!enterPossible) {
            throw new WaitingRoomException(ExceptionCode.WAITINGROOM_FULLED);
        }
        saveWaitingRoom(waitingRoom);
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

    public void ready(String roomCode, UserInfo userInfo) {
        WaitingRoom waitingRoom = waitingRoomRepository.getWaitingRoomNow(roomCode);

        List<CurrentParticipant> participants = waitingRoom.getCurrentParticipants();
        for (CurrentParticipant participant : participants) {
            if (userInfo.getUserId().equals(participant.getUserId())) {

                participant.setReady(!participant.isReady());
                waitingRoomRepository.saveWaitingRoom(waitingRoom);
                break;
            }
        }
    }


    public boolean start(String roomCode) {
        WaitingRoom waitingRoom = waitingRoomRepository.getWaitingRoomNow(roomCode);
        // 인원 2명이상 있어야 시작
        boolean startPossible = false;
        int startCnt = 1;
        for (int i = 1; i < waitingRoom.getCurrentParticipants().size(); i++) {
            if (waitingRoom.getCurrentParticipants().get(i).getNickname() != null && waitingRoom.getCurrentParticipants().get(i).isReady()) {
                startCnt += 1;
            }
        }
        if(startCnt >=2 ){
            startPossible = true;
        }


        return startPossible;
    }

    public void exit(String roomCode, UserInfo user) {
        WaitingRoom waitingRoom = waitingRoomRepository.getWaitingRoomNow(roomCode);
        if (waitingRoom.getCurrentParticipants().size() == 1) {
            removeWaitingRoom(roomCode);
        } else {
            List<CurrentParticipant> participants = waitingRoom.getCurrentParticipants();

            for (CurrentParticipant participant : participants) {
                // 똑같은거 찾으면 null로
                if (participant.getNickname() != null && participant.getNickname().equals(user.getNickname())) {
                    participant.setCharacterId(-1);
                    participant.setUserId(null);
                    participant.setNickname(null);
                    participant.setClosed(false);
                    participant.setReady(false);
                }
            }
            waitingRoomRepository.saveWaitingRoom(waitingRoom);
        }
    }

    public void kick(String roomCode, String outUser){
        WaitingRoom waitingRoom = waitingRoomRepository.getWaitingRoomNow(roomCode);

        List<CurrentParticipant> participants = waitingRoom.getCurrentParticipants();


        for (CurrentParticipant participant : participants) {
            if(participant.getNickname() != null && participant.getNickname().equals(outUser)){
                participant.setUserId(null);
                participant.setNickname(null);
                participant.setCharacterId(-1);
                participant.setReady(false);
                participant.setClosed(false);
            }
        }
        waitingRoomRepository.saveWaitingRoom(waitingRoom);
    }


    public void changeState(String roomCode, int num){
        // 전체 정보 받기
        WaitingRoom waitingRoom = waitingRoomRepository.getWaitingRoomNow(roomCode);

        // n번째 사용자 closed true로 변경하기
        waitingRoom.getCurrentParticipants().get(num-1).setClosed(!waitingRoom.getCurrentParticipants().get(num-1).isClosed());

        waitingRoomRepository.saveWaitingRoom(waitingRoom);
    }




}



