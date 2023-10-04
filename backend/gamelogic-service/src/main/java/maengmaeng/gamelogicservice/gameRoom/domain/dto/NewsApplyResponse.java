package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import maengmaeng.gamelogicservice.gameRoom.domain.Info;
import maengmaeng.gamelogicservice.gameRoom.domain.Land;
import maengmaeng.gamelogicservice.gameRoom.domain.Stock;

@Builder
@Getter
public class NewsApplyResponse {
	private List<Land> lands;
	private List<Stock> stocks;
	private Info info;
}
