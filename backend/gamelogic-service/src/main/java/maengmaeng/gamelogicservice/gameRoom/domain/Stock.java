package maengmaeng.gamelogicservice.gameRoom.domain;

import java.io.Serializable;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class Stock implements Serializable {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;
	private int id;
	private String name;
	private int cost;
	private int currentCost;
	private double dividends;
}
