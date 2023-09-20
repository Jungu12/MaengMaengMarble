package maengmaeng.gamelogicservice.gameRoom.domain;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Player implements Serializable {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;;
	private String playerId;
	private String nickname;
	private long money;
	private long asset;
	private int[] lands;
	private boolean alive;
	private int currentTurn;
	private int stopTradeCount;
	private int doubleCount;
	private int currentLap;
	private List<Integer> stocks;
	private int loan;
	private boolean[] cards;
	private int currentLocation;
}
