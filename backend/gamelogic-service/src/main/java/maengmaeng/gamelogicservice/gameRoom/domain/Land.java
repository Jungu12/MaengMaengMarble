package maengmaeng.gamelogicservice.gameRoom.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Land implements Serializable {
	@Builder.Default
	private static final long serialVersionUID = 207207207207L;;
	private int landId;
	private String name;
	private int landPrice;
	private int[] buildingPrices;
	private int[] fees;
	private int currentLandPrice;
	private int[] buildings;
	private int[] currentFees;
	private int owner;
	private String[] industries;
}
