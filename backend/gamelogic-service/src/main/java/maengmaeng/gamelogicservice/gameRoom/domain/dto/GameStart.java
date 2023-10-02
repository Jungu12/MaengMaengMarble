package maengmaeng.gamelogicservice.gameRoom.domain.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import maengmaeng.gamelogicservice.gameRoom.domain.StartCard;

@Getter
@Setter
@Builder
public class GameStart {
    private StartCard[] cards;
//    private boolean[] selected;

}
