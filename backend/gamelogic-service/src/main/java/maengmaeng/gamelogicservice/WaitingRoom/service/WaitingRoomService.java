package maengmaeng.gamelogicservice.waitingRoom.service;

import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.ChatMessage;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.UserInfo;
import maengmaeng.gamelogicservice.waitingRoom.repository.WaitingRoomRepository;
import maengmaeng.gamelogicservice.waitingRoom.util.RedisInOutManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.util.RedisPublisher;
import maengmaeng.gamelogicservice.util.RedisSubscriber;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class WaitingRoomService {
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private final RedisInOutManager redisSubscriberManager;
    private final WaitingRoomRepository waitingRoomRepository;
//    private ChannelTopic topic;
    private final ChannelTopic gameTopic;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /*
        방 입장
     */
    public void enter(String roomCode, UserInfo userInfo) {
        // 대기방(WaitingRoom) 정보에 사용자 추가하기
        waitingRoomRepository.addNewMember(roomCode,userInfo);

    }
    /*
        현재 대기방 정보 얻기
     */
    public WaitingRoom getWaitingRoomNow(String roomCode) {
        return waitingRoomRepository.getWaitingRoomNow(roomCode);
    }

}
