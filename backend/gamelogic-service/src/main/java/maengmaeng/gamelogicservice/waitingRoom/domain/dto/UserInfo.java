package maengmaeng.gamelogicservice.waitingRoom.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserInfo implements Serializable {
    String userId;
    String nickname;
    int characterId;

}
