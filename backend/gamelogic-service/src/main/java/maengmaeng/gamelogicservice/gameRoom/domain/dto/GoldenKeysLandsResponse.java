package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import maengmaeng.gamelogicservice.gameRoom.domain.GoldenKeys;
import maengmaeng.gamelogicservice.gameRoom.domain.Land;

@Builder
@Getter
public class GoldenKeysLandsResponse {
	private List<Land> Lands;
	private GoldenKeys goldenKeys;
	private String imgUrl;
}
