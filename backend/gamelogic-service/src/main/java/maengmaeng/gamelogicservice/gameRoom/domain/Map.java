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
public class Map implements Serializable {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;
	private String roomCode;
	private Player[] players;
	private Land[] lands;
	private Info info;
	private GoldenKeys goldenKeys;
	private NewsInfo newsInfo;
	private Stock[] stocks;
}
