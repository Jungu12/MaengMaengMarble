package maengmaeng.gamelogicservice.gameRoom.domain;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class News {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;;
	private int newsId;
	private int year;
	private String event;
	private List<Map<String, Integer>> countryEffects;
	private List<Map<String, Integer>> industryEffects;
}
