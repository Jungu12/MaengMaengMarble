package maengmaeng.gamelogicservice.gameRoom.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class News implements Serializable {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;;
	private long newsId;
	private String imageUrl;
	private String content;
	private List<Map<String, Integer>> countryEffects;
	private List<Map<String, Integer>> stockEffects;
}
