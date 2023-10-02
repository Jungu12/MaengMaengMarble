package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;

@Getter
@Setter
@Builder
public class DoorResponse {
    boolean lapCheck;
    private Player[] players;
}
