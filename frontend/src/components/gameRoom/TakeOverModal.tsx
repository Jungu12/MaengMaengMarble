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
  return (
    <CModal isOpen={isOpen} handleClose={handleClose}>
      <div className='flex flex-col p-[16px] justify-center items-center'>
        <div className='text-3xl text-text-100 mb-[32px]'>
          인수하시겠습니까?
        </div>
        <div className='text-2xp text-text-50 mb-[24px]'>{`${calCurrentFees(
          land
        )}원`}</div>
        <div className='flex gap-[12px] justify-center items-center'>
          <CButton type={'green'} onClick={handleTakeOver}>
            <div>예</div>
          </CButton>
          <CButton type={'red'} onClick={handleClose}>
            <div>아니오</div>
          </CButton>
        </div>
      </div>
    </CModal>
  );
};

export default TakeOverModal;
