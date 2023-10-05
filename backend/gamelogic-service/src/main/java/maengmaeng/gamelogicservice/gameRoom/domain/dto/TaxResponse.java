

package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class TaxResponse {
    private List<Player> players;
}
