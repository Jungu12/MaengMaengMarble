package maengmaeng.gamelogicservice.gameRoom.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class PlayerSeq {
    String userId;
    String nickname;
    int characterId;
    int playerCnt;

}
