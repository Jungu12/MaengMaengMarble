package maengmaeng.userservice.relation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoResponseDto {
    private String userId;
    private String nickname;
    private int point;
    private int win;
    private int lose;
    private String avatarImage;
}
