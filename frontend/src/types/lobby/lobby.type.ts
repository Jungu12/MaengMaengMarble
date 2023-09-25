import {
  ChatMessageType,
  ParticipantsType,
  UserType,
} from '../common/common.type';

export type RoomType = {
  title: string;
  currentParticipants: ParticipantsType[];
  code: string;
  chatMessageList: null | ChatMessageType;
};

export type PostRoomType = {
  userInfo: UserType;
  title: string;
  maxParticipants: string;
};
