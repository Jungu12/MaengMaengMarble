package maengmaeng.userservice.shop.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvatarResponseDto {
    private int avatarId;
    private String avatarName;
    private String avatarImage;
    private String avatarPrice;

}
