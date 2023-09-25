package maengmaeng.userservice.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserDetail {
    private String userId;
    private String nickname;
    private int point;
    private int win;
    private int lose;
    private int avatarId;
    private String avatarUrl;
}
