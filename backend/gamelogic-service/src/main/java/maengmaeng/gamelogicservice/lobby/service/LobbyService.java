package maengmaeng.gamelogicservice.lobby.service;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.exception.ExceptionCode;
import maengmaeng.gamelogicservice.exception.LobbyException;
import maengmaeng.gamelogicservice.global.dto.UserInfo;
import maengmaeng.gamelogicservice.lobby.repository.LobbyRepository;
import maengmaeng.gamelogicservice.util.RedisSubscriber;
import maengmaeng.gamelogicservice.waitingRoom.domain.CurrentParticipants;
import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LobbyService {
    private final LobbyRepository lobbyRepository;
    // 로비(topic)에 발행되는 메시지를 처리할 Listener
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private final RedisTemplate<String, String> redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(getClass());


    // 로비 입장 : redis에 'lobby' topic을 만들고 통신을 하기 위해 리스너를 설정
    public void enterLobby(String userId) {
        String lobbyTopic = "lobby";
        // lobby 토픽을 생성
        ChannelTopic LobbyTopic = new ChannelTopic(lobbyTopic);

        try {
            // Redis 메시지 리스너 등록
            redisMessageListener.addMessageListener(redisSubscriber, LobbyTopic);
        } catch (Exception e) {
            throw new LobbyException(ExceptionCode.LOBBY_ENTER_FAILED);
        }
    }

    public String createRoomCode() {
        return UUID.randomUUID().toString();
    }

    public WaitingRoom createWaitingRoom(UserInfo userInfo, String title) {
        CurrentParticipants currentParticipant = CurrentParticipants.builder()
            .userId(userInfo.getUserId())
            .nickname(userInfo.getNickname())
            .characterId(userInfo.getCharacterId())
            .closed(false)
            .ready(false)
            .build();

        WaitingRoom waitingRoom = WaitingRoom.builder()
            .code(createRoomCode())
            .title(title)
            .build();
        waitingRoom.addCurrentParticipants(currentParticipant);

        return waitingRoom;
    }
}
