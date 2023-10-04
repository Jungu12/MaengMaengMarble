package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;

@Getter
@Builder
public class LoanResponse {
    private Player player;
}
