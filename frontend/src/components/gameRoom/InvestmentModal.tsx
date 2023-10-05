import { images } from '@constants/images';
import { AnimatePresence, motion } from 'framer-motion';
import InvestmentStockCard from './InvestmentStockCard';
import { StockType } from '@/types/gameRoom/game.type';

type Props = {
  stockList: StockType[] | null;
  isOpen: boolean;
  handleInvestment: () => void;
};

const InvestmentModal = ({ stockList, isOpen, handleInvestment }: Props) => {
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
            className='flex flex-col items-center w-fit h-[650px] p-[20px] bg-white bg-opacity-90 rounded-[15px] relative overflow-auto'
          >
            <button
              className='w-[40px] h-[40px] ml-auto'
              onClick={handleInvestment}
            >
              <img src={images.icon.close} alt='닫기 버튼' />
            </button>
            <p className='text-[36px] text-black font-black mb-[30px]'>
              투자장
            </p>
            <div className='flex-1 flex flex-col pr-[20px] pl-[10px] space-y-[15px] overflow-auto scrollbar-invest'>
              {stockList &&
                stockList.map((stock) => (
                  <InvestmentStockCard
                    key={stock.id}
                    myMoney={500000}
                    stock={stock}
                  />
                ))}
            </div>
          </motion.div>
        </div>
      )}
    </AnimatePresence>
  );
};

export default InvestmentModal;
