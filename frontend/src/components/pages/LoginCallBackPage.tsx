import { getAccessToken } from '@apis/loginApi';
import { accessTokenState, userState } from '@atom/userAtom';
import { useEffect } from 'react';
import { useRecoilState, useSetRecoilState } from 'recoil';
import Lottie from 'lottie-react';
import flyBee from '@/assets/lotties/flyBee.json';
import { images } from '@constants/images';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import { getMyProfile } from '@apis/userApi';

const LoadingAnimation = {
  start: { scale: 0, opacity: 0.0 },
  end: {
    scale: 1,
    opacity: 1,
    transition: {
      duration: 0.5,
      type: 'spring',
      stiffness: 110,
      delayChildren: 0.5,
      staggerChildren: 0.2,
    },
  },
};

const InnerAnimation = {
  start: { opacity: 0, x: 100, y: 50 },
  end: { opacity: 1, x: 0, y: 0 },
};

const LoginCallBackPage = () => {
  const navigate = useNavigate();
  const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
  const setUser = useSetRecoilState(userState);
  const loginLoadingMsg = [
    '로',
    '그',
    '인',
    ' ',
    '중',
    '입',
    '니',
    '다',
    '.',
    '.',
    '.',
  ];
  // 인가 코드 파싱해서 서버에 보내주기
  useEffect(() => {
    const params = new URL(document.location.toString()).searchParams;
    const code = params.get('code');
    const state = params.get('state');

    if (code && state) {
      console.log(code);
      console.log(state);

      getAccessToken(code, state)
        .then((res) => {
          if (res) {
            console.log('맹맹');
            setAccessToken({ accessToken: res.accessToken });
          }
          console.log(res);
          // 유저 정보 저장 -> 로비로 이동
          // setUser(res);
        })
        .then(() => {
          getMyProfile()
            .then((res) => {
              setUser(res.data);
              console.log(res.data);
              navigate('/lobby');
            })
            .catch(() => {
              navigate('/login');
            });
        })
        .catch((err) => {
          alert(err);
          navigate('/login');
        });
    }
  }, [navigate, setAccessToken, setUser]);

  useEffect(() => {
    console.log(accessToken?.accessToken);
  }, [accessToken]);

  return (
    <div
      className='w-screen h-screen flex flex-col items-center justify-center'
      style={{
        backgroundImage: `url(${images.login.background})`,
        backgroundSize: 'contain',
      }}
    >
      <div className='mb-[24px]'>
        <Lottie
          animationData={flyBee}
          loop
          style={{
            height: '350px',
            width: '400px',
          }}
        />
      </div>
      <motion.div
        className='flex'
        initial='start'
        animate='end'
        variants={LoadingAnimation}
      >
        {loginLoadingMsg.map((msg, index) => (
          <motion.p
            key={index}
            className='mr-[12px] text-[40px] text-white font-semibold'
            variants={InnerAnimation}
            transition={{
              repeat: Infinity,
              repeatType: 'reverse',
              repeatDelay: 2.6,
            }}
          >
            {msg}
          </motion.p>
        ))}
      </motion.div>
    </div>
  );
};

export default LoginCallBackPage;
