import { images } from '@constants/images';
import { addComma } from '@utils/format';
import { motion } from 'framer-motion';
import { useCallback } from 'react';

type StoreCharacterInfoProps = {
  id: number;
  have: boolean;
  img: string;
  name: string;
  point: number;
  onClick: () => void;
  handleSelectedCid: (cid: number) => void;
};

const StoreCharacterCard = ({
  id,
  have,
  img,
  name,
  point,
  onClick,
  handleSelectedCid,
}: StoreCharacterInfoProps) => {
  const onClickPurchaseOk = useCallback(() => {
    onClick();
    handleSelectedCid(id);
  }, [handleSelectedCid, id, onClick]);

  return (
    <div
      className={`flex flex-col w-fit h-fit items-center justify-center ${
        have ? 'bg-primary-dark300' : 'bg-primary-500'
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
          src={have ? images.store.boughtgra : images.store.buygra}
          alt='그라데이션 뷰'
        />
        <p
          className={`w-full ${
            have ? 'text-primary-100 opacity-70' : 'text-primary-dark300'
          } z-20 absolute bottom-0 left-0 text-center font-semibold`}
        >
          {name}
        </p>
      </div>
      <motion.div
        className='w-full'
        whileHover={have ? {} : { scale: 1.05 }}
        whileTap={have ? {} : { scale: 0.9 }}
        transition={have ? {} : { type: 'spring', stiffness: 150, damping: 10 }}
      >
        <button
          onClick={have ? () => {} : onClickPurchaseOk}
          className={`w-full flex aspect-[194/61] bg-no-repeat ${
            have ? 'cursor-not-allowed' : ''
          }`}
          style={{
            backgroundImage: `url(${
              have ? images.button.bought : images.button.buy
            })`,
            backgroundSize: 'contain',
          }}
        >
          {!have && (
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
