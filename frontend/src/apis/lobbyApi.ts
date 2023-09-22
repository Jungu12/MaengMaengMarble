import { UserType } from '@/types/common/common.type';
import { http } from '@utils/http';

export async function createRoom(
  user: UserType,
  title: string,
  maxParticipants: string
): Promise<{ code: number }> {
  return http.post(`lobby/rooms`, {
    userInfo: user,
    title: title,
    maxParticipants: maxParticipants,
  });
}
