import WatingRoomCharaterCard from '@components/watingRoom/WatingRoomCharaterCard';
import WatingRoomChatting from '@components/watingRoom/WatingRoomChatting';
import WatingRoomHeader from '@components/watingRoom/WatingRoomHeader';
import { images } from '@constants/images';
import { CompatClient, Stomp } from '@stomp/stompjs';
import { motion } from 'framer-motion';
import SockJS from 'sockjs-client';
import { useEffect, useRef } from 'react';

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
  const client = useRef<CompatClient>();
  const isReady = true;

  // 입장 시 소켓 연결
  useEffect(() => {}, []);

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
      <WatingRoomHeader />
      <motion.div
        initial='start'
        animate='end'
        variants={BoxAnimation}
        className='flex justify-around h-full'
      >
        <WatingRoomCharaterCard
          name={'상근시치'}
          avaterUrl={images.dummy.dummy1}
          isReady={false}
          isManager={true}
          isClose={false}
          animation={InnerAnimation}
        />
        <WatingRoomCharaterCard
          name={'215'}
          avaterUrl={images.dummy.dummy2}
          isReady={true}
          isManager={false}
          isClose={false}
          animation={InnerAnimation}
        />
        <WatingRoomCharaterCard
          name={''}
          avaterUrl={''}
          isReady={false}
          isManager={false}
          isClose={true}
          animation={InnerAnimation}
        />
        <WatingRoomCharaterCard
          name={'기므나'}
          avaterUrl={images.dummy.dummy3}
          isReady={false}
          isManager={false}
          isClose={false}
          animation={InnerAnimation}
        />
      </motion.div>
      <div className='absolute bottom-[8px] left-[12px]'>
        <WatingRoomChatting />
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
