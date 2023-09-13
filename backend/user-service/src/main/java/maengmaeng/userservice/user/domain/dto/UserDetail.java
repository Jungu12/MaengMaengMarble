package maengmaeng.userservice.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserDetail {
    private String id;
    private String nickname;
    private int characterId;
}
