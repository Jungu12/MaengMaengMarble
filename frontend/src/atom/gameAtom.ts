import { LandType, PlayerType } from '@/types/gameRoom/game.type';
import { atom } from 'recoil';

export const playersState = atom<(PlayerType | null)[]>({
  key: 'players',
  default: [],
});

export const currentPlayerState = atom<string>({
  key: 'currentPlayer',
  default: '',
});

export const landListState = atom<LandType[]>({
  key: 'landList',
  default: [],
});
