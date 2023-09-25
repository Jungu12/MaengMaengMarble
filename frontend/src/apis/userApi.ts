import { DetailUserType } from '@/types/common/common.type';
import { authAxios } from '@utils/http';

export async function getMyProfile(): Promise<DetailUserType> {
  return authAxios.get('user-service/users/user');
}
