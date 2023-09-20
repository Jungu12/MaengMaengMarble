package maengmaeng.gamelogicservice.gameRoom.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class GameInfo implements Serializable {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;;
}
