package maengmaeng.gamelogicservice.waitingRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import maengmaeng.gamelogicservice.waitingRoom.domain.CurrentParticipant;

import java.util.List;

@Builder
@Getter
@Setter
public class OutUserResponse {
    private String outUser;
    private List<CurrentParticipant> currentParticipants;
}
