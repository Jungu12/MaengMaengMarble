package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import maengmaeng.gamelogicservice.gameRoom.domain.Land;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;

@Builder
@Getter
@Setter
public class AfterMoveResponse {
	private Player[] players;
	private List<Land> lands;
}
