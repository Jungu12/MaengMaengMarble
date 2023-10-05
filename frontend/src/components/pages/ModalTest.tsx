// import InvestmentMachineModal from '@components/gameRoom/InvestmentMachineModal';
// import { useCallback, useState } from 'react';
import { images } from '@constants/images';
// import InvestmentModal from '@components/gameRoom/InvestmentModal';

const ModalTest = () => {
  // const [isOpenInvestment, setIsOpenInvestment] = useState(true);
  // const handleInvestment = useCallback(() => {
  //   setIsOpenInvestment((prev) => !prev);
  // }, []);

  return (
    <>
      {/* <InvestmentModal
        isOpen={isOpenInvestment}
        handleInvestment={handleInvestment}
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
