package maengmaeng.gamelogicservice.waitingRoom.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class GameData implements Serializable {
    private static final long serialVersionUID = 207207207207L;
    private String destination; // 안쓰기
    private String type; // 지우기
    private String roomCode; // 근데 ? 게임데이터에 룸코드를 넣어야하는가?
    private Object data;


}
