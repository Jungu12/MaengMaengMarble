import { FriendDetailType, FriendType } from '@/types/friend/friend.type';
import { authHttp } from '@utils/http';

export async function friendDetail(
  friendId: string
): Promise<FriendDetailType> {
  return authHttp.get(`user-service/relation/detail/${friendId}`);
}

export async function getFriendlist(): Promise<FriendType[]> {
  return authHttp.get(`user-service/relation`);
}

export async function addFriend(nickname: string): Promise<string> {
  return authHttp.post(`user-service/relation`, {
    to: nickname,
  });
}

export async function deleteFreind(friendId: string): Promise<string> {
  return authHttp.delete(`user-service/relation`, {
    to: friendId,
  });
}
