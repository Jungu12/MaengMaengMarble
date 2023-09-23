package maengmaeng.gamelogicservice.waitingRoom.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.ChatMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class WaitingRoom implements Serializable {
    private static final long serialVersionUID = 207207207207L;
    private String title;
    private List<CurrentParticipant> currentParticipant;
    private String code;


    public void addCurrentParticipants(CurrentParticipant participant){
        if(currentParticipant==null){
            currentParticipant = new ArrayList<>();
        }
        currentParticipant.add(participant);
    }

}
