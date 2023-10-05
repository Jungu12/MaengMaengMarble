import { LandType } from '@/types/gameRoom/game.type';
import CButton from '@components/common/CButton';
import CModal from '@components/common/CModal';
import { calCurrentFees } from '@utils/game';

type Props = {
  isOpen: boolean;
  handleClose: () => void;
  handleTakeOver: () => void;
  land: LandType;
};

const TakeOverModal = ({
  isOpen,
  handleClose,
  handleTakeOver,
  land,
}: Props) => {
  console.log('[땅 체크]', land);

  return (
    <CModal isOpen={isOpen} handleClose={handleClose}>
      <div className='flex flex-col p-[16px] justify-center items-center w-[420px] h-[360px]'>
        <div className='text-5xl text-text-100 mb-[32px]'>
          인수하시겠습니까?
        </div>
        <div className='text-2xp text-text-50 mb-[24px]'>{`${calCurrentFees(
          land
        )}원`}</div>
        <div className='flex mt-auto w-full h-[32px] gap-[12px] justify-center items-center'>
          <CButton
            type={'green'}
            onClick={handleTakeOver}
            rounded={16}
            height={32}
            width={60}
          >
            <div className='text-[20px] text-white'>예</div>
          </CButton>
          <CButton
            type={'red'}
            onClick={handleClose}
            rounded={16}
            height={32}
            width={60}
          >
            <div className='text-[20px] text-white'>아니오</div>
          </CButton>
        </div>
      </div>
    </CModal>
  );
};

export default TakeOverModal;
