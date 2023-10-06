package maengmaeng.gamelogicservice.gameRoom.domain;

import java.io.Serializable;
import java.util.Comparator;

public class WaitingNewsComparator implements Comparator<WaitingNews>, Serializable {
	@Override
	public int compare(WaitingNews w1, WaitingNews w2) {
		return Integer.compare(w1.getTurn(), w2.getTurn());
	}
}
