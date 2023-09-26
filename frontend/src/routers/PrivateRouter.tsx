import { Outlet, useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import { useSetRecoilState } from 'recoil';
import { regenerateAccessToken } from '@apis/loginApi';
import { getMyProfile } from '@apis/userApi';
import { accessTokenState, userState } from '@atom/userAtom';
import { useAxiosInterceptor } from '@hooks/useAxiosInterceptor';

const PrivateRoutes = () => {
  const navigation = useNavigate();
  const setAccessToken = useSetRecoilState(accessTokenState);
  const setUser = useSetRecoilState(userState);
  useAxiosInterceptor();

  useEffect(() => {
    // 새로고침 시 리프레시 토큰이 있다면 자동 로그인
    const initLogin = async () => {
      try {
        const response = await regenerateAccessToken();
        const userData = await getMyProfile();
        const newAccessToken = response;
        // Recoil 상태 업데이트
        setAccessToken(newAccessToken);
        setUser(userData.data);
      } catch (error) {
        // refreshToken이 만료되었거나 다른 오류 발생
        // 로그아웃 처리 등을 수행
        alert('로그인이 필요합니다.');
        navigation('/login');
      }
    };

    // initLogin();
  }, [navigation, setAccessToken, setUser]);

  return <Outlet />;
};

export default PrivateRoutes;
