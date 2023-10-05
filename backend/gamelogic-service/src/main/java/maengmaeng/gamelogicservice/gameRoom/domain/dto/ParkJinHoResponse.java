package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;

@Builder
@Getter
public class ParkJinHoResponse {
	private Player[] players;
	private int[] num;
}
