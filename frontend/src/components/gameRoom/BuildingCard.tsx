import { motion } from 'framer-motion';
import { images } from '@constants/images';
import { BuildingType, addAmountUnit } from '@utils/game';
import CCheckBox from '@components/common/CCheckBox';

type Props = {
  type: BuildingType;
  price: number;
  width: number;
  height: number;
  isChecked?: boolean | null;
  handleCheck?: (() => void) | null;
  leftTurn?: number;
};

const BuildingCard = ({
  type,
  price,
  width,
  height,
  isChecked,
  handleCheck,
  leftTurn,
}: Props) => {
  return (
    <button
      onClick={
        handleCheck == null || (leftTurn != null && leftTurn > 0)
          ? () => {}
          : handleCheck
      }
    >
      <motion.div
        className={`flex flex-col bg-primary-light200 rounded-[10px] p-[10px] border-primary-dark100 border-opacity-80 border-2 relative ${
          isChecked == null || (leftTurn != null && leftTurn > 0)
            ? 'cursor-not-allowed'
            : ''
        }`}
        style={{
          width: `${width}px`,
          height: `${height}px`,
          boxShadow: '5px 5px 0px 0px rgba(95, 89, 75, 0.80)',
        }}
        whileHover={
          isChecked == null || (leftTurn != null && leftTurn > 0)
            ? {}
            : { scale: 1.05 }
        }
        whileTap={
          isChecked == null || (leftTurn != null && leftTurn > 0)
            ? {}
            : { scale: 0.9 }
        }
        transition={
          isChecked != null || (leftTurn != null && leftTurn > 0)
            ? {}
            : { type: 'spring', stiffness: 150, damping: 10 }
        }
      >
        {leftTurn != null && leftTurn > 0 && (
          <div className='flex flex-col w-full h-full absolute items-center justify-center bg-black bg-opacity-60 top-0 left-0 z-20 rounded-[8px]'>
            <p className='text-primary-light100 font-bold text-[18px] px-[35px]'>
              {' '}
              {leftTurn} 바퀴를 더 돌아야 건설 가능
            </p>
          </div>
        )}
        <div className='flex flex-row w-full h-1/5 bg-primary-dark100 bg-opacity-80 rounded-tl-[10px] rounded-tr-[10px] px-[15px] items-center justify-end'>
          <p className='text-primary-light100 font-bold text-[18px]'>{type}</p>
        </div>
        <div
          className='flex flex-row w-full h-3/5 rounded-bl-[10px] rounded-br-[10px] relative justify-center'
          style={{
            backgroundImage: `url(${images.gameRoom.buildingbackground})`,
            backgroundSize: 'cover',
          }}
        >
          <img
            className='absolute h-full aspect-[428/583] z-10'
            src={
              type == '땅값'
                ? images.building.blueground
                : type == '별장'
                ? images.building.bluepension
                : type == '빌딩'
                ? images.building.bluebuilding
                : images.building.bluehotel
            }
            alt='건물 이미지'
          />
          {type == '땅값' && (
            <div className='flex flex-row w-5/6 absolute items-center justify-center rounded-[10px] top-2 bg-black bg-opacity-50 z-20'>
              <p className='text-primary-light100 font-bold text-[14px] py-[2px]'>
                필수 건설지
              </p>
            </div>
          )}
          {type != '땅값' &&
            (leftTurn == null || leftTurn == 0) &&
            isChecked != null &&
            handleCheck != null && (
              <div className='absolute left-0 bottom-0 z-20'>
                <CCheckBox isChecked={isChecked} />
              </div>
            )}
        </div>
        <div className='flex flex-row w-full h-1/5 items-center justify-center'>
          <p className=' text-third-100 font-bold text-[18px]'>
            {addAmountUnit(price)}
          </p>
        </div>
      </motion.div>
    </button>
  );
};

export default BuildingCard;
