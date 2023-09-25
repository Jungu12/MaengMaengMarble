import { getAccessToken } from '@apis/loginApi';
import { accessTokenState } from '@atom/userAtom';
import { useEffect } from 'react';
import { useRecoilState } from 'recoil';

const LoginCallBackPage = () => {
  const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
  // 인가 코드 파싱해서 서버에 보내주기
  useEffect(() => {
    const params = new URL(document.location.toString()).searchParams;
    const code = params.get('code');
    const state = params.get('state');

    if (code && state) {
      console.log(code);
      console.log(state);

      getAccessToken(code, state).then((res) => {
        if (res) {
          console.log('맹맹');

          setAccessToken({ accessToken: res.accessToken });
        }
        console.log(res);
      });
    }
  }, [setAccessToken]);

  useEffect(() => {
    console.log(accessToken?.accessToken);
  }, [accessToken]);

  return <div>{accessToken?.accessToken}</div>;
};

export default LoginCallBackPage;
