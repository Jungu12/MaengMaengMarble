package maengmaeng.gamelogicservice.global.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GameData {
	private String type;
	private String roomCode;
	private ResponseDto data;
}
