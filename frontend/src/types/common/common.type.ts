/* eslint-disable import/named */
import { AxiosRequestConfig } from 'axios';

export type UserType = {
  userId: string;
  nickname: string;
  characterId: number;
};

export type DetailUserType = {
  avatarId: number;
  lose: number;
  nickname: string;
  point: number;
  userId: string;
  win: number;
  avatarImageBg: string;
  avatarImageNoBg: string;
};

export type ParticipantsType = {
  userId: string;
  nickname: string;
  characterId: number;
  ready: boolean;
  closed: boolean;
};

export type ChatMessageType = {
  message: string;
  roomCode: string;
  sender: string;
};

export type AxiosResponse<T> = {
  data: T;
  status: number;
  statusText: string;
  headers: {
    [key: string]: string;
  };
  config: AxiosRequestConfig;
  request: XMLHttpRequest;
};

export type CharacterType = {
  avatarId: number;
  avatarName: string;
  avatarPrice: number;
  hasAvatar: boolean;
  avatarImageBg: string;
  avatarImageNoBg: string;
};

export type WSResponseType<T> = {
  tpye: string;
  data: T;
};
