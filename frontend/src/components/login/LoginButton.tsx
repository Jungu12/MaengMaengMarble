import { images } from '@constants/images';
import { useCallback } from 'react';

const LoginButton = () => {
  const handleLoginButton = useCallback(() => {
    window.location.href =
      'https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=Kecej_d0LIleRy3N3wSL&state=3&redirect_uri=http://localhost:3000/login/oauth/naver/callback';
  }, []);

  return (
    <button
      onClick={handleLoginButton}
      className='w-[340px] h-[64px] relative bg-[#03C75A] rounded-md flex items-center justify-center'
    >
      <img className='w-[64px] h-[64px]' src={images.logo.naver} alt='logo' />
      <p className='ml-[67px] mr-[90px] text-2xl font-bold text-white'>
        로그인
      </p>
    </button>
  );
};

export default LoginButton;
