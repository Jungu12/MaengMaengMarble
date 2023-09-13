package maengmaeng.userservice.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CharacterChangeRequest {
    private String characterName;
    private String characterImage;
    private String characterPrice;
}
