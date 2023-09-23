package maengmaeng.gamelogicservice.waitingRoom.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Setter
@Getter
public class CurrentParticipant implements Serializable {
    private static final long serialVersionUID = 207207207207L;
    private String userId;
    private String nickname;
    private int characterId;
    private boolean ready;
    private boolean closed;

}
