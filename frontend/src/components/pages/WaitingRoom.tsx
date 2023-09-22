import WaitingRoomCharaterCard from '@components/watingRoom/WaitingRoomCharaterCard';
import WaitingRoomChatting from '@components/watingRoom/WaitingRoomChatting';
import { images } from '@constants/images';
import * as StompJs from '@stomp/stompjs';
import { activateClient, getClient } from '@utils/socket';
import { motion } from 'framer-motion';
import { useCallback, useEffect, useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

const BoxAnimation = {
  start: { scale: 0, opacity: 0.5 },
  end: {
    scale: 1,
    opacity: 1,
    transition: {
      duration: 0.5,
      type: 'spring',
      stiffness: 110,
      delayChildren: 1,
      staggerChildren: 0.5,
    },
  },
};

const InnerAnimation = {
  start: { opacity: 0, y: 10 },
  end: { opacity: 1, y: 0 },
};

const WaitingRoom = () => {
  const navigate = useNavigate();
  const { roomId } = useParams();
  const isReady = true;
  const client = useRef<StompJs.Client>();

  const onClickExitButton = useCallback(() => {
    navigate(-1);
  }, [navigate]);

  useEffect(() => {
    client.current = getClient();
    activateClient(client.current);
    client.current.onConnect = () => {
      if (client.current) {
        client.current.subscribe('/sub/waiting-rooms/12345', (res) => {
          console.log(JSON.parse(res.body));
        });
        client.current.publish({
          destination: '/pub/lobby/12345',
          body: JSON.stringify({
            userid: '12345',
            nickname: '김상근',
            characterId: 1,
          }),
        });
      }
    };
  }, []);

  return (
    <motion.div
      className='flex flex-col w-full h-full min-h-[700px] overflow-hidden relative'
      style={{
        backgroundImage: `url(${images.waitingRoom.background})`,
        backgroundSize: 'cover',
      }}
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
    >
      <div className='flex items-center w-full h-[80px] border-b-2 border-white/80 bg-blue-400/40 shadow-2xl'>
        <p className='font-extrabold text-[36px] text-white ml-[24px]'>
          맹맹 시치 모여라~~
        </p>
        <div className='flex items-center'>
          <img
            className='w-[28px] h-[28px] ml-[80px]'
            src={images.waitingRoom.mail}
            alt='초대코드'
          />
          <span className='ml-[20px] text-white font-extrabold text-[20px]'>
            12345
          </span>
          <motion.img
            className='ml-[12px] w-[32px] h-[32px] cursor-pointer'
            src={images.waitingRoom.copy}
            alt='복사'
            whileHover={{ scale: 1.2 }}
            whileTap={{ scale: 0.9 }}
          />
        </div>
        <button className='ml-auto mr-[12px]' onClick={onClickExitButton}>
          <img
            className='w-[56px] h-[56px] cursor-pointer'
            src={images.waitingRoom.exit}
            alt='나가기'
          />
        </button>
      </div>
      <motion.div
        initial='start'
        animate='end'
        variants={BoxAnimation}
        className='flex justify-around h-full'
      >
        <WaitingRoomCharaterCard
          name={'상근시치'}
          avaterUrl={images.dummy.dummy1}
          isReady={false}
          isManager={true}
          isClose={false}
          animation={InnerAnimation}
        />
        <WaitingRoomCharaterCard
          name={'215'}
          avaterUrl={images.dummy.dummy2}
          isReady={true}
          isManager={false}
          isClose={false}
          animation={InnerAnimation}
        />
        <WaitingRoomCharaterCard
          name={''}
          avaterUrl={''}
          isReady={false}
          isManager={false}
          isClose={true}
          animation={InnerAnimation}
        />
        <WaitingRoomCharaterCard
          name={'기므나'}
          avaterUrl={images.dummy.dummy3}
          isReady={false}
          isManager={false}
          isClose={false}
          animation={InnerAnimation}
        />
      </motion.div>
      <div className='absolute bottom-[8px] left-[12px]'>
        <WaitingRoomChatting />
      </div>
      <motion.div
        className='z-10 cursor-pointer absolute bottom-[8px] left-[40%]'
        whileHover={{ scale: 1.1 }}
        whileTap={{ scale: 0.9 }}
        transition={{ type: 'spring', stiffness: 400, damping: 17 }}
      >
        <img
          className='h-[100px]'
          src={
            isReady
              ? images.waitingRoom.readyButton
              : images.waitingRoom.cancelButton
          }
          alt='button'
        />
      </motion.div>
    </motion.div>
  );
};

export default WaitingRoom;
