import { PlayerType } from '@/types/gameRoom/game.type';
import { atom } from 'recoil';

export const playersState = atom<(PlayerType | null)[]>({
  key: 'players',
  default: [],
});
