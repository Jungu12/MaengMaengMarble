import { images } from '@constants/images';
import { addComma } from '@utils/format';
import { motion } from 'framer-motion';

type StoreCharacterInfoProps = {
  have: boolean;
  img: string;
  name: string;
  point: number;
  onClick: () => void;
};

const StoreCharacterCard = ({
  have,
  img,
  name,
  point,
  onClick,
}: StoreCharacterInfoProps) => {
  return (
    <div
      className={`flex flex-col w-fit h-fit items-center justify-center ${
        have ? 'bg-primary-500' : 'bg-primary-dark300'
      } p-[10px] rounded-[10px] relative`}
    >
      <div className='w-full relative mb-3'>
        <img
          className='w-full aspect-[2/3] relative'
          src={img}
          alt='캐릭터 뷰'
        />
        <img
          className='w-full absolute aspect-[2/3] top-0 left-0 bottom-0 right-0 z-10'
          src={have ? images.store.buygra : images.store.boughtgra}
          alt='그라데이션 뷰'
        />
        <p
          className={`w-full ${
            have ? 'text-primary-dark300' : 'text-primary-100 opacity-70'
          } z-20 absolute bottom-0 left-0 text-center font-semibold`}
        >
          {name}
        </p>
      </div>
      <motion.div
        className='w-full'
        whileHover={have ? { scale: 1.05 } : {}}
        whileTap={have ? { scale: 0.9 } : {}}
        transition={have ? { type: 'spring', stiffness: 150, damping: 10 } : {}}
      >
        <button
          onClick={onClick}
          className={`w-full flex aspect-[194/61] bg-no-repeat ${
            have ? '' : 'cursor-not-allowed'
          }`}
          style={{
            backgroundImage: `url(${
              have ? images.button.buy : images.button.bought
            })`,
            backgroundSize: 'contain',
          }}
        >
          {have && (
            <div className='flex w-full h-full flex-row items-center px-8 justify-between'>
              <img className='h-7' src={images.icon.point} alt='코인 아이콘' />
              <p className='text-[18px] font-black text-text-100'>
                {addComma(point)}
              </p>
            </div>
          )}
        </button>
      </motion.div>
    </div>
  );
};

export default StoreCharacterCard;
