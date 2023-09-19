package maengmaeng.gamelogicservice.gameRoom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class GoldenKeys {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;;
	private int silver;
	private int gold;
	private int platinum;
	private int newsBan;
}
