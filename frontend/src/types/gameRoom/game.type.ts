export type TurnListType = {
  seq: number;
  selected: boolean;
};

export type PlayerType = {
  playerId: string;
  nickname: string;
  avatarId: number;
  avatarImage: string;
  money: number;
  asset: number;
  lands: number[];
  alive: boolean;
  currentTurn: number;
  stopTradeCount: number;
  doubleCount: number;
  currentLap: number;
  stocks: number[];
  loan: number;
  cards: [boolean, boolean]; // 천사 카드와 언론 통제카드 보유 유무
  currentLocation: number;
};

export type LandType = {
  landId: number;
  name: string;
  landPrice: number;
  buildingPrices: [number, number, number];
  fees: [number, number, number];
  currentLandPrice: number;
  currentBuildingPrices: [number, number, number];
  buildings: [boolean, boolean, boolean, boolean];
  currentFees: [number, number, number];
  owner: number;
};

export type InfoType = {
  currentPlayer: string;
  playerCnt: number;
  turnCount: number;
  effectNews: number[];
  waitingNews: number[];
  doorCheck: number;
};

export type GoldKeyType = {
  silver: number;
  bronze: number;
  platinum: number;
  newsBan: number;
  hurricane: number;
  angel: number;
  kangJunGu: number;
  lotto: number;
  door: number;
  earthquake: number;
};

export type StockEffectType = {
  [stockName: string]: number;
};

export type CountryEffectType = {
  [countryName: string]: number;
};

export type NewsType = {
  newsId: number;
  imageUrl: string;
  content: string;
  countryEffects: CountryEffectType[];
  stockEffects: StockEffectType[];
};

export type StockType = {
  id: number;
  name: string;
  cost: number;
  currentCost: number;
  dividends: number;
};

export type FullGameDataType = {
  roomCode: string;
  players: (PlayerType | null)[];
  lands: LandType[];
  info: InfoType;
  goldenKeys: GoldKeyType;
  newsInfo: {
    bronze: NewsType[];
    diamond: NewsType[];
    platinum: NewsType[];
  };
  stocks: StockType[];
  seqCards: TurnListType[];
};
