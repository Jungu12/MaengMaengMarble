package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GameData {
	private String destination;
	private Object data;
}
