package maengmaeng.userservice.relation.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RelationResponseDto {
    private String userId;
    private String nickname;
    private String character;

}