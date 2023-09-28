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

public class Player implements Serializable {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;;
	private String playerId;
	private String nickname;
	private int avatarId;
	private long money;
	private long asset;
	private List<Integer> lands;
	private boolean alive;
	private int currentTurn;
	private int stopTradeCount;
	private int doubleCount;
	private int currentLap;
	private int[] stocks;
	private int loan;
	private boolean[] cards;
	private int currentLocation;
}
