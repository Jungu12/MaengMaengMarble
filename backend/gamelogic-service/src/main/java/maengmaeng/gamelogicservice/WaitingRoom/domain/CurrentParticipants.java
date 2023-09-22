package maengmaeng.gamelogicservice.waitingRoom.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class CurrentParticipants implements Serializable {
    private static final long serialVersionUID = 207207207207L;
    String userId;
    String nickname;
    int characterId;
    boolean closed;
    boolean ready;



}
