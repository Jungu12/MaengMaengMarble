package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import lombok.Getter;
import maengmaeng.gamelogicservice.gameRoom.domain.News;

@Getter
public class ApplyNewsRequest {
	private News news;
	private String type;
}
