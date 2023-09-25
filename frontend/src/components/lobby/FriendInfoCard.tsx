import CButton from '@components/common/CButton';
import { motion } from 'framer-motion';

type FriendInfoProps = {
  id: number;
  name: string;
  img: string;
  onClickDeleteButton: (fid: number) => void;
};

const FriendInfoCard = ({
  id,
  name,
  img,
  onClickDeleteButton,
}: FriendInfoProps) => {
  const onClick = () => {
    onClickDeleteButton(id);
  };

  return (
    <div className='flex flex-row w-full h-fit items-center px-[30px] py-[10px] bg-primary-500 rounded-[20px] shadow-md'>
      <img
        className='mr-[20px] w-[70px] h-[70px] object-cover rounded-full'
        src={img}
        alt='친구 프로필 이미지'
      />
      <div className='flex flex-1 flex-row items-center justify-between'>
        <p className='text-[20px] font-black text-primary-dark300'>{name}</p>
        <motion.div
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.9 }}
          transition={{ type: 'spring', stiffness: 150, damping: 10 }}
        >
          <CButton
            type='red'
            onClick={onClick}
            width={60}
            height={35}
            rounded={20}
          >
            <p className='text-[15px] font-semibold text-primary-100'>삭제</p>
          </CButton>
        </motion.div>
      </div>
    </div>
  );
};

export default FriendInfoCard;
