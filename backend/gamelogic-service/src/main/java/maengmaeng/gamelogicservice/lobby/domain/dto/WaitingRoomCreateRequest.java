package maengmaeng.gamelogicservice.lobby.domain.dto;

import lombok.Getter;
import maengmaeng.gamelogicservice.global.dto.WaitingRoomUserInfo;

@Getter
public class WaitingRoomCreateRequest {
	private WaitingRoomUserInfo userInfo;
	private String title;
	private int maxParticipants;
}
