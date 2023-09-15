package maengmaeng.userservice.shop.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserAvatarDto {
    String userId;
    int avatarId;
}
