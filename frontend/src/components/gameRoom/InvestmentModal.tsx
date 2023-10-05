import { images } from '@constants/images';
import { AnimatePresence, motion } from 'framer-motion';
import InvestmentStockCard from './InvestmentStockCard';

const stockList = [
  {
    id: 1,
    name: 'LG 화학',
    cost: 580000,
    currentCost: 980000,
    dividends: 5,
  },
  {
    id: 2,
    name: '애플',
    cost: 215102,
    currentCost: 248167,
    dividends: 0.8,
  },
  {
    id: 3,
    name: '현대건설',
    cost: 35550,
    currentCost: 25550,
    dividends: 3,
  },
  {
    id: 4,
    name: '삼성전자',
    cost: 66900,
    currentCost: 79900,
    dividends: 2,
  },
  {
    id: 5,
    name: '에어비앤비',
    cost: 172797,
    currentCost: 92797,
    dividends: 0.5,
  },
  {
    id: 6,
    name: '테슬라',
    cost: 339878,
    currentCost: 609878,
    dividends: 0.5,
  },
  {
    id: 7,
    name: '농심',
    cost: 458500,
    currentCost: 268500,
    dividends: 5,
  },
  {
    id: 8,
    name: 'Sofina Foods Inc.',
    cost: 302005,
    currentCost: 302005,
    dividends: 2,
  },
  {
    id: 9,
    name: '나이키',
    cost: 133530,
    currentCost: 133530,
    dividends: 1.5,
  },
  {
    id: 10,
    name: '셀트리온',
    cost: 145800,
    currentCost: 145800,
    dividends: 4,
  },
  {
    id: 11,
    name: '대한한공',
    cost: 24450,
    currentCost: 24450,
    dividends: 4,
  },
  {
    id: 12,
    name: '포스코인터내셔널',
    cost: 78400,
    currentCost: 96400,
    dividends: 6,
  },
  {
    id: 13,
    name: '마린하베스트',
    cost: 23810,
    currentCost: 45810,
    dividends: 2,
  },
  {
    id: 14,
    name: 'SK하이닉스',
    cost: 118500,
    currentCost: 98500,
    dividends: 1,
  },
];

type Props = {
  isOpen: boolean;
  handleInvestment: () => void;
};

const InvestmentModal = ({ isOpen, handleInvestment }: Props) => {
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
            className='flex flex-col items-center sapce-y-[30px] w-fit h-[650px] p-[20px] bg-white bg-opacity-90 rounded-[15px] relative overflow-auto'
          >
            <button
              className='w-[40px] h-[40px] ml-auto'
              onClick={handleInvestment}
            >
              <img src={images.icon.close} alt='닫기 버튼' />
            </button>
            <p className='text-[36px] text-black font-black'>투자장</p>
            <div className='flex-1 flex flex-col pr-[20px] pl-[10px] space-y-[15px] overflow-auto scrollbar-invest'>
              {stockList.map((stock) => (
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
