package maengmaeng.gamelogicservice.lobby.domain.dto;

import lombok.Getter;
import maengmaeng.gamelogicservice.global.dto.UserInfo;

@Getter
public class WaitingRoomCreateRequest {
	private UserInfo userInfo;
	private String title;
}
