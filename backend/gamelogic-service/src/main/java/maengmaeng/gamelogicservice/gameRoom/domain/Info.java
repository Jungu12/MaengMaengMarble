package maengmaeng.gamelogicservice.gameRoom.domain;

import java.io.Serializable;
import java.util.LinkedList;
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
public class Info implements Serializable {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;
	private String currentPlayer;
	private int playerCnt;
	private int turnCount;
	private List<News> effectNews;
	private List<WaitingNews> waitingNews;
	private int doorCheck;

}
