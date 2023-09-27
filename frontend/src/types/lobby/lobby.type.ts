import { ParticipantsType, UserType } from '../common/common.type';

export type PostRoomType = {
  userInfo: UserType;
  title: string;
  maxParticipants: string;
};

export type RoomType = {
  title: string;
  currentParticipants: ParticipantsType[];
  code: string;
  createdTime: string;
};
