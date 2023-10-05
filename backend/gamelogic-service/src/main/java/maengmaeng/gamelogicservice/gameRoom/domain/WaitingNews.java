package maengmaeng.gamelogicservice.gameRoom.domain;

import lombok.*;

import java.io.Serializable;
import java.util.Comparator;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class WaitingNews implements Serializable {
    @Builder.Default
    private static final long serialVersionUID = 207207207207L;;
    private News news;
    private int turn;
}
