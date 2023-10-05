package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;
import maengmaeng.gamelogicservice.gameRoom.domain.GoldenKeys;
import maengmaeng.gamelogicservice.gameRoom.domain.Info;

@Builder
@Getter
public class GoldenKeysInfoResponse {
	private Info info;
	private GoldenKeys goldenKeys;
	private String imgUrl;
}
