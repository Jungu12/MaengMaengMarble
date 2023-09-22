package maengmaeng.gamelogicservice.waitingRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class GameData implements Serializable {
    private static final long serialVersionUID = 207207207207L;
    private String destination;
    private String type;
    private String roomCode;
    private Object data;


}
