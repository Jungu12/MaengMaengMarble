package maengmaeng.gamelogicservice.gameRoom.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class GoldenKeys implements Serializable {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;;
	// 남은 실버 카드
	private int silver;
	//남은 골드 카드
	private int bronze;
	//남은 플레 카드
	private int platinum;
	// 남은 언론 통제
	private int newsBan;
	// 남은 허리케인
	private int hurricane;
	// 천사
	private int angel;
	// 남은 강준구의 문단속
	private int kangJunGu;
	// 남은 로또 당첨
	private int lotto;
	// 남은 어디로든 문
	private int door;
	// 남은 지진
	private int earthquake;
}