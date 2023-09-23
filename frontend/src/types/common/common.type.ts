export type UserType = {
  userId: string;
  nickname: string;
  characterId: number;
};

export type DetailUserType = {
  id: string;
  nickname: string;
  characterId: number;
  win: number;
  lose: number;
  point: number;
};

export type ParticipantsType = {
  userId: string;
  nickname: string;
  characterId: number;
  ready: boolean;
  closed: boolean;
};

export type ChatMessageType = {
  test: string;
};
