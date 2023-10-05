package maengmaeng.gamelogicservice.gameRoom.domain;

import lombok.*;

import java.io.Serializable;
import java.util.Comparator;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class WaitingNews implements Serializable, Comparator<WaitingNews> {
    @Builder.Default
    private static final long serialVersionUID = 207207207207L;;
    private News news;
    private int turn;
    @Override
    public int compare(WaitingNews w1, WaitingNews w2) {
        return Integer.compare(w1.getTurn(),w2.getTurn());
    }
}
