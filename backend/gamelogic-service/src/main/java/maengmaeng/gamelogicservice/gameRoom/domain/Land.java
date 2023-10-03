package maengmaeng.gamelogicservice.gameRoom.domain;

import java.io.Serializable;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class Land implements Serializable {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;;
	private int landId;
	private String name;
	private int landPrice;
	private int[] buildingPrices;
	private int[] fees;
	private int currentLandPrice;
	private int [] currentBuildingPrices;
	private boolean [] buildings;
	private int[] currentFees;
	private int owner;
}
