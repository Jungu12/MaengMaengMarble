package maengmaeng.gamelogicservice.waitingRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 207207207207L;
    private String roomCode; // 방번호
    private String senderId; // 메시지 보낸사람 Id
    private String message; // 메시지
    private String updatedTime; // 전송 시간

}