// import SellStockMachineModal from '@components/gameRoom/SellStockMachineModal';
// import { useCallback, useState } from 'react';
import { images } from '@constants/images';
// import SellStockModal from '@components/gameRoom/SellStockModal';

const ModalTest = () => {
  // const [isOpenSellStock, setIsOpenSellStock] = useState(true);
  // const handleSellStock = useCallback(() => {
  //   setIsOpenSellStock((prev) => !prev);
  // }, []);

  return (
    <>
      {/* <SellStockModal
        stockCnt={10}
        stock={{
          id: 1,
          name: 'LG 화학',
          cost: 580000,
          currentCost: 980000,
          dividends: 5,
        }}
        isOpen={isOpenSellStock}
        handleSellStock={handleSellStock}
      /> */}
      <div
        className='flex flex-col w-full h-full relative p-[45px] overflow-auto'
        style={{
          backgroundImage: `url(${images.gameRoom.background})`,
          backgroundSize: 'cover',
        }}
      ></div>
    </>
  );
};

export default ModalTest;
