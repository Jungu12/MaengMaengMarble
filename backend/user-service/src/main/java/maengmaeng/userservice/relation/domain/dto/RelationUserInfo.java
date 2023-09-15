package maengmaeng.userservice.relation.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationUserInfo {
    int id;
    String nickname;
    int characterId;
    int win;
    int lose;

}
