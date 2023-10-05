package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import maengmaeng.gamelogicservice.gameRoom.domain.GoldenKeys;
import maengmaeng.gamelogicservice.gameRoom.domain.News;

@Builder
@Getter
public class NewsResponse {
	private List<News> choosed;
	private GoldenKeys goldenKeys;
}
