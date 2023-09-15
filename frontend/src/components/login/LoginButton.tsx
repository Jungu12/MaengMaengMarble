import { images } from '@constants/images';

const LoginButton = () => {
  return (
    <button className='w-[340px] h-[64px] relative bg-[#03C75A] rounded-md flex items-center justify-center'>
      <img className='w-[64px] h-[64px]' src={images.logo.naver} alt='logo' />
      <p className='ml-[67px] mr-[90px] text-2xl font-bold text-white'>
        로그인
      </p>
    </button>
  );
};

export default LoginButton;
