export type StoreCharacterType = {
  avatarId: number;
  avatarName: string;
  avatarImage: string;
  avatarPrice: number;
  hasAvatar: boolean;
};

export type StoreInfoType = {
  point: number;
  avatarList: StoreCharacterType[];
};
