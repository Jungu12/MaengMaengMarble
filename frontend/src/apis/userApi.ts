import { AxiosResponse, DetailUserType } from '@/types/common/common.type';
import { authAxios } from '@utils/http';

export async function getMyProfile(): Promise<AxiosResponse<DetailUserType>> {
  return authAxios.get('user-service/users/user');
}

export async function checkNickname(
  nickname: string
): Promise<AxiosResponse<boolean>> {
  return authAxios.get(
    `user-service/users/nicknames/duplication?nickname=${nickname}`
  );
}

export async function changeNickname(nickname: string) {
  console.log('이게 뭐노');

  return authAxios.patch(
    `user-service/users/nicknames?newNickname=${nickname}`
  );
}
