package maengmaeng.gamelogicservice.gameRoom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class WaitingNews {
    @Builder.Default
    private static final long serialVersionUID = 207207207207L;;
    private News news;
    private int turn;
}
