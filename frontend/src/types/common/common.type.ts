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
