import { UserType } from '@/types/common/common.type';
import { RoomType } from '@/types/lobby/lobby.type';
import { gameHttp } from '@utils/http';

// 추후 gameHttp -> http로 변경해야함.
export async function createRoom(
  user: UserType,
  title: string,
  maxParticipants: string
): Promise<{ roomCode: string }> {
  return gameHttp.post(`maeng/lobby/rooms`, {
    userInfo: user,
    title: title,
    maxParticipants: maxParticipants,
  });
}

export async function getRooms(): Promise<{ waitingRooms: RoomType[] }> {
  return gameHttp.get(`maeng/lobby`);
}

export async function validateInviteCode(roomCode: string): Promise<boolean> {
  return gameHttp.get(`maeng/lobby/validation/${roomCode}`);
}
