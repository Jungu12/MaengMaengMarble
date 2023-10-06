package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import maengmaeng.gamelogicservice.gameRoom.domain.GoldenKeys;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;

@Builder
@Getter
public class GoldenKeysPlayersResponse {
	private Player[] players;
	private GoldenKeys goldenKeys;
	private String imgUrl;
}
