package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import maengmaeng.gamelogicservice.gameRoom.domain.Info;
import maengmaeng.gamelogicservice.gameRoom.domain.Land;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;
import maengmaeng.gamelogicservice.gameRoom.domain.Stock;

import java.util.List;

@Builder
@Getter
@Setter
public class EndTurnResponse {

    private Info info;
    private Player[] players;
    private List<Stock> stocks;
    private List<Land> lands;




}
