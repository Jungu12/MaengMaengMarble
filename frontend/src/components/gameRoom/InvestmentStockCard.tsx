import { StockType } from '@/types/gameRoom/game.type';
import { ToastMessageState } from '@atom/toastAtom';
import { images } from '@constants/images';
import useToastList from '@hooks/useToastList';
import { addComma } from '@utils/format';
import { calStockPercentage } from '@utils/game';
import { useCallback, useMemo, useState } from 'react';
import { useSetRecoilState } from 'recoil';

type Props = {
  myMoney: number;
  stock: StockType;
};

const InvestmentStockCard = ({ myMoney, stock }: Props) => {
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
  // const [totalPurchasePrice, setTotalPurchasePrice] = useState(
  //   stock.currentCost * count
  // );
  const totalPurchasePrice = useMemo(
    () => stock.currentCost * count,
    [count, stock.currentCost]
  );

  const { show } = useToastList();
  const setToastMessage = useSetRecoilState(ToastMessageState);

  const increase = useCallback(() => {
    if (myMoney < stock.currentCost * (count + 1)) {
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '보유 현금이 부족합니다.',
        };
      });
      show('error');
    } else {
      setCount((prev) => prev + 1);
    }
  }, [count, myMoney, setToastMessage, show, stock.currentCost]);

  const decrease = useCallback(() => {
    if (count == 1) {
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '최소 1개부터 구매 가능합니다.',
        };
      });
      show('error');
    } else {
      setCount((prev) => prev - 1);
    }
  }, [count, setToastMessage, show]);

  return (
    <div className='flex flex-row w-[680px] items-center justify-between bg-white rounded-[10px] shadow-md py-[10px] px-[30px]'>
      <p className='w-1/3 text-[22px] font-bold mr-[8px]'>{stock.name}</p>

      <div className='w-1/5 flex flex-col items-end'>
        <p
          className='text-[18px] font-bold'
          style={{ color: color }}
        >{`${addComma(stock.currentCost)}원`}</p>
        <div className='flex flex-row items-center'>
          <p className='text-[12px] font-bold opacity-80 mr-[8px]'>{`배당 ${stock.dividends}%`}</p>
          <div
            className='rounded-[5px] p-[3px]'
            style={{ backgroundColor: bgColor }}
          >
            <p className='text-[12px] font-bold' style={{ color: color }}>
              {calStockPercentage(stock.currentCost, stock.cost)}
            </p>
          </div>
        </div>
      </div>
      <div className='flex-1 flex flex-row justify-end items-center'>
        <p className='text-[15px] font-bold opacity-60 mr-[10px]'>
          {`${addComma(totalPurchasePrice)}원`}
        </p>
        <div className='w-[45px] bg-black bg-opacity-10 border-2 border-[#D7D5D5] flex flex-row items-center justify-center rounded-[8px] py-[5px]'>
          <p className='text-[15px] font-bold'>{count}</p>
        </div>
        <div className='flex flex-col mx-[10px]'>
          <button className='w-[15px] h-[20px]' onClick={increase}>
            <img src={images.icon.blackup} alt='플러스 버튼' />
          </button>
          <button className='w-[15px] h-[20px]' onClick={decrease}>
            <img src={images.icon.blackdown} alt='마이너스 버튼' />
          </button>
        </div>
        <button
          className={`bg-black bg-opacity-60 flex flex-row items-center justify-center rounded-[8px] py-[7px] px-[15px] ${
            totalPurchasePrice > myMoney ? 'cursor-not-allowed' : ''
          }`}
          disabled={totalPurchasePrice > myMoney ? true : false}
        >
          <p className='text-[15px] font-bold text-white'>매수</p>
        </button>
      </div>
    </div>
  );
};

export default InvestmentStockCard;
