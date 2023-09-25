package maengmaeng.gamelogicservice.global.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ResponseDto {

    private String type;
    private Object data;

}
