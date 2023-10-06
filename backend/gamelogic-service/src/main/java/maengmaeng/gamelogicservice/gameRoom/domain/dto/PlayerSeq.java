package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class PlayerSeq {
    private String userId;
    private String nickname;
    private int characterId;
    private int playerCnt;

}
