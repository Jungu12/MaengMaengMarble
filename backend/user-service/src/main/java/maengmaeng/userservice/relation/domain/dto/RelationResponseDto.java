package maengmaeng.userservice.relation.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RelationResponseDto {
    public Long relationId;
    public String fromId;
    public String toId;

}
