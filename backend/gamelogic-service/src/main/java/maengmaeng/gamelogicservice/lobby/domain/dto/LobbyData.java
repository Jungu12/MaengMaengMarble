package maengmaeng.gamelogicservice.lobby.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LobbyData {
    private String destination;
    private Object data;
}
