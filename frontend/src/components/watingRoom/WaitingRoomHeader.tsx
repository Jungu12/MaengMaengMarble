import { images } from '@constants/images';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
type Props = {
  title: string;
  code: string;
};

const WaitingRoomHeader = ({ title, code }: Props) => {
  const navigation = useNavigate();

  return (
    <div className='flex items-center w-full h-[80px] border-b-2 border-white/80 bg-blue-400/40 shadow-2xl'>
      <p className='font-extrabold text-[36px] text-white ml-[24px]'>{title}</p>
      <div className='flex items-center'>
        <img
          className='w-[28px] h-[28px] ml-[80px]'
          src={images.waitingRoom.mail}
          alt='초대코드'
        />
        <span className='ml-[20px] text-white font-extrabold text-[20px]'>
          {code}
        </span>
        <motion.img
          className='ml-[12px] w-[32px] h-[32px] cursor-pointer'
          src={images.waitingRoom.copy}
          alt='복사'
          whileHover={{ scale: 1.2 }}
          whileTap={{ scale: 0.9 }}
        />
      </div>
      <button
        className='ml-auto mr-[12px] w-[56px] h-[56px] cursor-pointer'
        onClick={() => {
          navigation(-1);
        }}
      >
        <img
          className='w-[56px] h-[56px]'
          src={images.waitingRoom.exit}
          alt='나가기'
        />
      </button>
    </div>
  );
};

export default WaitingRoomHeader;
