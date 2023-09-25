package maengmaeng.userservice.shop.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopResponseDto {
    private List<AvatarResponseDto> avatarList;
    private int point;

}
