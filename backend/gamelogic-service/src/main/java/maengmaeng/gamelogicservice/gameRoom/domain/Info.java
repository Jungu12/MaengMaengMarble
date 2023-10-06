package maengmaeng.gamelogicservice.gameRoom.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Info implements Serializable {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;
	private String currentPlayer;
	private int playerCnt;
	private int turnCount;
	private Queue<News> effectNews;
	private Queue<WaitingNews> waitingNews; //<,
	private int doorCheck;

}
