import LoginButton from '@components/login/LoginButton';
import { images } from '@constants/images';
import Lottie from 'lottie-react';
import earthAnimation from '@/assets/lotties/earthAnimation.json';
import marbleAnimation from '@/assets/lotties/marbleTextAnimation.json';

const LoginPage = () => {
  return (
    <div
      className='flex flex-col w-full h-full min-h-[700px] overflow-hidden relative'
      style={{
        backgroundImage: `url(${images.login.background})`,
        backgroundSize: 'contain',
      }}
    >
      <div className='z-10 flex m-[80px] items-center justify-center'>
        <h1 className='text-8xl font-extrabold font-pretendard text-[#51E1C1] mr-[24px]'>
          MAENG MAENG
        </h1>
        <Lottie
          animationData={marbleAnimation}
          loop
          style={{
            height: '89px',
            width: '401px',
          }}
        />
      </div>
      <Lottie
        animationData={earthAnimation}
        loop
        style={{
          height: '900px',
          width: '900px',
          position: 'absolute',
          top: '60%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
        }}
      />
      <div className='z-10 flex w-full items-center justify-center mt-auto mb-[24px] pr-[64px]'>
        <LoginButton />
      </div>
    </div>
  );
};

export default LoginPage;
