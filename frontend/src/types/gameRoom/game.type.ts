export type TurnListType = {
  seq: number;
  selected: boolean;
};

export type LandType = {
  landId: number;
  name: string;
  landPrice: number;
  buildingPrices: number[];
  fees: number[];
  currentLandPrice: number;
  currentBuildingPrices: number[];
  buildings: boolean[];
  currentFees: number[];
  owner: number;
};
