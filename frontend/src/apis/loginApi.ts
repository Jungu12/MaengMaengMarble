import { ResponseAccessTokenType } from '@/types/common/auth.type';
import { authHttp, http } from '@utils/http';

export async function getAccessToken(
  code: string,
  state: string
): Promise<ResponseAccessTokenType> {
  return http.post(`user-service/auth/naver`, {
    code: code,
    state: state,
  });
}

export async function regenerateAccessToken(): Promise<ResponseAccessTokenType> {
  return authHttp.post(`user-service/auth/token`);
}
