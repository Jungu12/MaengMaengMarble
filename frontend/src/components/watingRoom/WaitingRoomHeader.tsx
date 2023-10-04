import { ToastMessageState } from '@atom/toastAtom';
import { images } from '@constants/images';
import useToastList from '@hooks/useToastList';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
type Props = {
  title: string;
  code: string;
};

const WaitingRoomHeader = ({ title, code }: Props) => {
  const navigation = useNavigate();
  const setToastMessage = useSetRecoilState(ToastMessageState);
  const { show } = useToastList();

  const handleCopyClipBoard = async (text: string) => {
    console.log(text);

    try {
      await navigator.clipboard.writeText(text);
      setToastMessage((prev) => {
        return {
          ...prev,
          success: '복사 완료',
        };
      });
      show('success');
    } catch (e) {
      console.log(e);
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '복사 실패',
        };
      });
      show('error');
    }
  };

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
        <button onClick={() => handleCopyClipBoard(code)}>
          <motion.img
            className='ml-[12px] w-[32px] h-[32px] cursor-pointer'
            src={images.waitingRoom.copy}
            alt='복사'
            whileHover={{ scale: 1.2 }}
            whileTap={{ scale: 0.9 }}
          />
        </button>
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
