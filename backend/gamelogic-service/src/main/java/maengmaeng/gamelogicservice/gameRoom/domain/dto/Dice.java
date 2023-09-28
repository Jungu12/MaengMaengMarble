package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Dice {
    int dice1;
    int dice2;
    int doubleCount;
    boolean lapCheck;
}
