package maengmaeng.gamelogicservice.gameRoom.domain;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class NewsInfo implements Serializable {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;;
	private List<News> silver;
	private List<News> gold;
	private List<News> platinum;
}
