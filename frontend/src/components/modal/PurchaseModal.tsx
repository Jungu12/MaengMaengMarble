import { TOAST_TYPE, ToastType } from '@atom/toastAtom';
import CModal from '@components/common/CModal';
import { images } from '@constants/images';
import useToastList from '@hooks/useToastList';
import { MouseEventHandler } from 'react';

type PurchaseModalProps = {
  isOpenPurchaseModal: boolean;
  handlePurchaseModalClose: () => void;
};

const PurchaseModal = ({
  isOpenPurchaseModal,
  handlePurchaseModalClose,
}: PurchaseModalProps) => {
  const { show } = useToastList();

  const handleClick: MouseEventHandler = (e) => {
    handlePurchaseModalClose();
    const { toastType } = (e.currentTarget as HTMLButtonElement).dataset as {
      toastType: ToastType;
    };
    show(toastType);
  };

  return (
    <CModal isOpen={isOpenPurchaseModal} handleClose={handlePurchaseModalClose}>
      <div className='flex flex-col px-[40px] items-center'>
        <p className='text-[25px] font-black text-text-100 mt-[20px] mb-[60px]'>
          정말 구매하시겠습니까?
        </p>
        <div className='flex flex-row space-x-20'>
          <button
            onClick={handleClick}
            data-toast-type={TOAST_TYPE.success}
            className='w-[150px] aspect-[194/61] bg-no-repeat items-center justify-center'
            style={{
              backgroundImage: `url(${images.button.buy})`,
              backgroundSize: 'contain',
            }}
          >
            <p className='text-[25px] font-black text-text-100'>예</p>
          </button>
          <button
            onClick={handlePurchaseModalClose}
            className='w-[150px] aspect-[194/61] bg-no-repeat items-center justify-center'
            style={{
              backgroundImage: `url(${images.button.buy})`,
              backgroundSize: 'contain',
            }}
          >
            <p className='text-[25px] font-black text-text-100'>아니오</p>
          </button>
        </div>
      </div>
    </CModal>
  );
};

export default PurchaseModal;
