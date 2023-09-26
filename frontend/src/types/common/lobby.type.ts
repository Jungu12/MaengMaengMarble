import { ParticipantsType } from './common.type';

export type RoomType = {
  title: string;
  currentParticipant: ParticipantsType[];
  code: string;
};
