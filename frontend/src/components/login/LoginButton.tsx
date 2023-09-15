import { images } from '@constants/images';
import { useCallback } from 'react';

const LoginButton = () => {
  const handleLoginButton = useCallback(() => {
    window.location.href = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${
      import.meta.env.VITE_CLIENT_ID
    }&state=3&redirect_uri=${import.meta.env.VITE_NAVER_REDIRECT_URL}`;
  }, []);

  return (
    <button
      onClick={handleLoginButton}
      className='w-[340px] h-[64px] relative bg-[#03C75A] rounded-md flex items-center justify-center'
    >
      <img className='w-[64px] h-[64px]' src={images.logo.naver} alt='logo' />
      <p className='ml-[17px] mr-[20px] text-2xl font-bold text-white'>
        네이버로 로그인하기
      </p>
    </button>
  );
};

export default LoginButton;
