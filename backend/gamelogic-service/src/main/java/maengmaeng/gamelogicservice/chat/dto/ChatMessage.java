package maengmaeng.gamelogicservice.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatMessage {
	private String roomCode;
	private String sender;
	private String message;
}
