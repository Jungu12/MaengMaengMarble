package maengmaeng.gamelogicservice.waitingRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import maengmaeng.gamelogicservice.waitingRoom.domain.CurrentParticipant;

import java.util.List;

@Getter
@Setter
@Builder
public class StartResponseDto {
    private String test;
    private String roomCode;
    private List<CurrentParticipant> currentParticipantList;
}
