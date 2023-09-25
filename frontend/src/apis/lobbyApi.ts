import { UserType } from '@/types/common/common.type';
import { RoomType } from '@/types/common/lobby.type';
import { http } from '@utils/http';

export async function createRoom(
  user: UserType,
  title: string,
  maxParticipants: string
): Promise<{ code: string }> {
  return http.post(`maeng/lobby/rooms`, {
    userInfo: user,
    title: title,
    maxParticipants: maxParticipants,
  });
}

export async function getRooms(): Promise<{ waitingRooms: RoomType[] }> {
  return http.get(`maeng/lobby`);
}
