import { StockType } from '@/types/gameRoom/game.type';
import { images } from '@constants/images';
import { AnimatePresence, motion } from 'framer-motion';
import { ToastMessageState } from '@atom/toastAtom';
import useToastList from '@hooks/useToastList';
import { addComma } from '@utils/format';
import { calStockPercentage } from '@utils/game';
import { useMemo, useState, useCallback } from 'react';
import { useSetRecoilState } from 'recoil';

type Props = {
  stockCnt: number;
  stock: StockType;
  isOpen: boolean;
  handleSellStock: () => void;
};

const SellStockModal = ({
  stockCnt,
  stock,
  isOpen,
  handleSellStock,
}: Props) => {
  const color =
    stock.currentCost > stock.cost
      ? '#FF3B2F'
      : stock.currentCost < stock.cost
      ? '#2F50FF'
      : '#1D2345';
  const bgColor =
    stock.currentCost > stock.cost
      ? '#FEE1DF'
      : stock.currentCost < stock.cost
      ? '#DFE4FE'
      : '#DCDDE2';
  const [count, setCount] = useState(1);
  // const [totalSellPrice, settotalSellPrice] = useState(
  //   stock.currentCost * count
  // );
  const totalSellPrice = useMemo(
    () => stock.currentCost * count,
    [count, stock.currentCost]
  );

  const { show } = useToastList();
  const setToastMessage = useSetRecoilState(ToastMessageState);

  const increase = useCallback(() => {
    if (stockCnt < count + 1) {
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '최대 보유 개수입니다.',
        };
      });
      show('error');
    } else {
      setCount((prev) => prev + 1);
    }
  }, [count, setToastMessage, show, stockCnt]);

  const decrease = useCallback(() => {
    if (count == 1) {
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '최소 1개부터 매도 가능합니다.',
        };
      });
      show('error');
    } else {
      setCount((prev) => prev - 1);
    }
  }, [count, setToastMessage, show]);

  return (
    <AnimatePresence>
      {isOpen && (
        <div
          className='absolute flex w-full h-full bg-black bg-opacity-50 items-center justify-center'
          style={{
            zIndex: 100,
          }}
        >
          <motion.div
            initial={{
              opacity: 0,
              scale: 0.75,
            }}
            animate={{
              opacity: 1,
              scale: 1,
              transition: {
                ease: 'easeOut',
                duration: 0.15,
              },
            }}
            exit={{
              opacity: 0,
              scale: 0.75,
              transition: {
                ease: 'easeIn',
                duration: 0.15,
              },
            }}
            className='flex flex-col items-center w-fit h-fit p-[20px] bg-white rounded-[15px] relative'
          >
            <button
              className='w-[40px] h-[40px] ml-auto'
              onClick={handleSellStock}
            >
              <img src={images.icon.close} alt='닫기 버튼' />
            </button>

            <div className='flex flex-row px-[60px] items-baseline mb-[30px]'>
              <p
                className='text-[28px] font-bold'
                style={{ color: color }}
              >{`${addComma(stock.currentCost)}원`}</p>
              <div
                className='rounded-[10px] p-[3px] mx-[15px]'
                style={{ backgroundColor: bgColor }}
              >
                <p className='text-[20px] font-bold' style={{ color: color }}>
                  {calStockPercentage(stock.currentCost, stock.cost)}
                </p>
              </div>
              <p className='text-[18px] font-bold opacity-80'>{`배당 ${stock.dividends}%`}</p>
            </div>

            <div className='flex flex-row px-[60px] items-baseline mb-[90px]'>
              <p className='text-[32px] font-bold mr-[20px]'>{stock.name}</p>
              <p className='text-[20px] font-bold opacity-60 mr-[10px]'>
                {`총 ${addComma(stock.currentCost * stockCnt)}원 보유`}
              </p>
            </div>

            <div className='flex flex-row px-[60px] items-center mb-[90px]'>
              <p className='text-[24px] font-bold opacity-60 mr-[10px]'>
                {`${addComma(totalSellPrice)}원`}
              </p>
              <div className='w-[90px] bg-black bg-opacity-10 border-2 border-[#D7D5D5] flex flex-row items-center justify-center rounded-[8px] py-[5px] mx-[25px]'>
                <p className='text-[22px] font-bold'>{count}</p>
              </div>
              <div className='flex flex-col'>
                <button className='w-[20px] h-[26px]' onClick={increase}>
                  <img src={images.icon.blackup} alt='플러스 버튼' />
                </button>
                <button className='w-[20px] h-[26px]' onClick={decrease}>
                  <img src={images.icon.blackdown} alt='마이너스 버튼' />
                </button>
              </div>
            </div>
            <button
              className={`bg-black bg-opacity-60 flex flex-row items-center justify-center rounded-[8px] py-[7px] px-[15px] `}
            >
              <p className='text-[24px] font-bold text-white px-[30px] py-[5px]'>
                매도
              </p>
            </button>
          </motion.div>
        </div>
      )}
    </AnimatePresence>
  );
};

export default SellStockModal;
