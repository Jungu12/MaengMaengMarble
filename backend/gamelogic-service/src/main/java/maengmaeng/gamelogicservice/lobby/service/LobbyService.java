package maengmaeng.gamelogicservice.lobby.service;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.exception.ExceptionCode;
import maengmaeng.gamelogicservice.exception.LobbyException;
import maengmaeng.gamelogicservice.lobby.repository.LobbyRepository;
import maengmaeng.gamelogicservice.lobby.util.RedisInOutManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.time.format.SignStyle;

@RequiredArgsConstructor
@Service
public class LobbyService {
    private final LobbyRepository lobbyRepository;
    // 로비(topic)에 발행되는 메시지를 처리할 Listener
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private final RedisInOutManager redisSubscriberManager;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final RedisTemplate<String, String> redisTemplate;

    // 로비 입장 : redis에 'lobby' topic을 만들고 통신을 하기 위해 리스너를 설정
    public void enterLobby(String userId) {
        String lobbyTopic = "lobby";
        // lobby 토픽을 생성
        ChannelTopic LobbyTopic = new ChannelTopic(lobbyTopic);

        try {
            // Redis 메시지 리스너 등록
            redisMessageListener.addMessageListener(redisSubscriber, LobbyTopic);

            userJoinedLobby(lobbyTopic, userId);
        } catch (Exception e) {
            throw new LobbyException(ExceptionCode.LOBBY_ENTER_FAILED);
        }
    }

    // 사용자가 로비에 들어왔을때 구독자로 등록한다.
    public void userJoinedLobby(String topic, String userId) {
        redisSubscriberManager.addUserTopic(topic, userId);
    }

    // 사용자가 로비에서 나갔을때 구독자에서 제거한다.
    public void userLeftLobby(String topic, String userId) {
        redisSubscriberManager.removeUserFromTopic(topic, userId);
    }

    // 사용자가 로비에 구독 중인지 확인한다.
    public boolean isUserSubscribedToLobby(String topic, String userId) {
        return redisSubscriberManager.isUserSubscribedToTopic(topic,userId);
    }
}
