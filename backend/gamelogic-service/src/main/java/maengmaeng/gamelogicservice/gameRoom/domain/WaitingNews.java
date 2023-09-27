package maengmaeng.gamelogicservice.gameRoom.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class WaitingNews {
    @Builder.Default
    private static final long serialVersionUID = 207207207207L;;
    private News news;
    private int turn;
}
