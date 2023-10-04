import SlotMachineModal from '@components/gameRoom/SlotMachineModal';
import { useCallback, useState } from 'react';
import { images } from '@constants/images';

const ModalTest = () => {
  const [isOpenSlot, setIsOpenSlot] = useState(true);
  const handleSlot = useCallback(() => {
    setIsOpenSlot((prev) => !prev);
  }, []);

  return (
    <>
      <SlotMachineModal
        slotResult={[7, 5, 7]}
        isOpen={isOpenSlot}
        handleSlot={handleSlot}
      />
      <div
        className='flex flex-col w-full h-full relative p-[45px] overflow-auto'
        style={{
          backgroundImage: `url(${images.lobby.background})`,
          backgroundSize: 'cover',
        }}
      ></div>
    </>
  );
};

export default ModalTest;
