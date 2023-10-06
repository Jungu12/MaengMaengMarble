package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;

@Getter
@Setter
@Builder
public class Dice {
    private Player[] players;
    int dice1;
    int dice2;
    int doubleCount;
    boolean lapCheck;
}
