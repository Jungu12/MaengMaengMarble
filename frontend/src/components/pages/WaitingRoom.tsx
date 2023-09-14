import WatingRoomCharaterCard from '@components/watingRoom/WatingRoomCharaterCard';
import WatingRoomChatting from '@components/watingRoom/WatingRoomChatting';
import { images } from '@constants/images';
import { motion } from 'framer-motion';

const WaitingRoom = () => {
  const isReady = true;

  return (
    <div
      className='flex flex-col w-full h-full min-h-[700px] overflow-hidden relative'
      style={{
        backgroundImage: `url(${images.waitingRoom.background})`,
        backgroundSize: 'cover',
      }}
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
        <img
          className='ml-auto mr-[12px] w-[56px] h-[56px] cursor-pointer'
          src={images.waitingRoom.exit}
          alt='나가기'
        />
      </div>
      <div className='flex justify-around h-full'>
        <WatingRoomCharaterCard
          name={'상근시치'}
          avaterUrl={images.dummy.dummy1}
          isReady={false}
          isManager={true}
          isClose={false}
        />
        <WatingRoomCharaterCard
          name={'215'}
          avaterUrl={images.dummy.dummy2}
          isReady={true}
          isManager={false}
          isClose={false}
        />
        <WatingRoomCharaterCard
          name={''}
          avaterUrl={''}
          isReady={false}
          isManager={false}
          isClose={true}
        />
        <WatingRoomCharaterCard
          name={'기므나'}
          avaterUrl={images.dummy.dummy3}
          isReady={false}
          isManager={false}
          isClose={false}
        />
      </div>
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
    </div>
  );
};

export default WaitingRoom;
