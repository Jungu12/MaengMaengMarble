import { ResponseAccessTokenType } from '@/types/common/auth.type';
import { http } from '@utils/http';

export async function getAccessToken(
  code: string,
  state: string
): Promise<ResponseAccessTokenType> {
  return http.post(`user-service/auth/naver`, {
    code: code,
    state: state,
  });
}
