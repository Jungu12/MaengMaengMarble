import { ChatMessageType, ParticipantsType } from './common.type';

export type RoomType = {
  title: string;
  currentParticipants: ParticipantsType[];
  code: string;
  chatMessageList: null | ChatMessageType;
};
