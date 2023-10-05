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
  currentFees: [number, number, number, number];
  owner: number;
};

export type InfoType = {
  currentPlayer: string;
  playerCnt: number;
  turnCount: number;
  effectNews: NewsType[];
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

export type NewInfoType = {
  bronze: NewsType[];
  diamond: NewsType[];
  platinum: NewsType[];
};

export type FullGameDataType = {
  roomCode: string;
  players: (PlayerType | null)[];
  lands: LandType[];
  info: InfoType;
  goldenKeys: GoldKeyType;
  newsInfo: NewInfoType;
  stocks: StockType[];
  seqCards: TurnListType[];
};

export type TurnEndResponseType = {
  players: (PlayerType | null)[];
  lands: LandType[];
  stocks: StockType[];
  info: InfoType;
};

export type DiceResultType = {
  players: (PlayerType | null)[];
  dice1: 1 | 2 | 3 | 4 | 5 | 6;
  dice2: 1 | 2 | 3 | 4 | 5 | 6;
  doubleCount: number;
  lapCheck: boolean;
};

export type SlotType = {
  players: (PlayerType | null)[];
  num: number[];
};

export type GoldenKeyNewsResponseType = {
  choosed: NewsType[];
  goldenKeys: GoldKeyType;
};

export type GoldenKeyPlayerResponseType = {
  players: PlayerType[];
  goldenKeys: GoldKeyType;
};

export type GoldenKeyLandsResponseType = {
  lands: LandType[];
  goldenKeys: GoldKeyType;
};
