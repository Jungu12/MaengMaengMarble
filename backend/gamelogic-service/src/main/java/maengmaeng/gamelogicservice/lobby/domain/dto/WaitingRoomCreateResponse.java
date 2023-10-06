package maengmaeng.gamelogicservice.lobby.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WaitingRoomCreateResponse {
	private String roomCode;
}
