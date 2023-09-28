package maengmaeng.gamelogicservice.gameRoom.domain;

import java.io.Serializable;
import java.util.List;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class NewsInfo implements Serializable {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;;
	private List<News> bronze;
	private List<News> diamond;
	private List<News> platinum;

}
