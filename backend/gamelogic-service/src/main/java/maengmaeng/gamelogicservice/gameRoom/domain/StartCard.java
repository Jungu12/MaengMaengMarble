package maengmaeng.gamelogicservice.gameRoom.domain;


import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StartCard implements Serializable {
    @Builder.Default
    private static final long serialVersionUID = 207207207207L;
    private int seq;
    private boolean selected;

}
