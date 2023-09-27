package maengmaeng.gamelogicservice.gameRoom.domain;

import java.io.Serializable;
import java.util.List;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class GameInfo implements Serializable {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;
	private String roomCode;
	private Player[] players;
	private List<Land> lands;
	private Info info;
	private GoldenKeys goldenKeys;
	private NewsInfo newsInfo;

	private List<Stock> stocks;


}
