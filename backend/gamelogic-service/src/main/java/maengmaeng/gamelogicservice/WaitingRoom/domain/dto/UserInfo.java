package maengmaeng.gamelogicservice.waitingRoom.domain.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserInfo implements Serializable {
    String userId;
    String nickname;
    int characterId;

}
