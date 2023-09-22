package maengmaeng.gamelogicservice.waitingRoom.domain;

import lombok.Builder;
import lombok.Getter;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.ChatMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class WaitingRoom implements Serializable {
    private static final long serialVersionUID = 207207207207L;
    private int id;
    private String title;
    private List<CurrentParticipants> currentParticipants;
    private String code;
    private List<ChatMessage> chatMessageList;


    public void addCurrentParticipants(CurrentParticipants currentParticipant){
        if(currentParticipants==null){
            currentParticipants = new ArrayList<>();
        }
        currentParticipants.add(currentParticipant);
    }

    public void addChatMessage(ChatMessage chatMessage){
        if(chatMessageList==null){
            chatMessageList = new ArrayList<>();
        }
        chatMessageList.add(chatMessage);
    }
}
