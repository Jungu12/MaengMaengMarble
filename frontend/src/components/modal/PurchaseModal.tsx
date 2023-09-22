import CButton from '@components/common/CButton';
import CModal from '@components/common/CModal';
import useToastList from '@hooks/useToastList';
import { useCallback } from 'react';

type PurchaseModalProps = {
  isOpenPurchaseModal: boolean;
  handlePurchaseModalClose: () => void;
};

const PurchaseModal = ({
  isOpenPurchaseModal,
  handlePurchaseModalClose,
}: PurchaseModalProps) => {
  const { show } = useToastList();

  const onClickOkButton = useCallback(() => {
    handlePurchaseModalClose();
    show('success');
  }, [handlePurchaseModalClose, show]);

  return (
    <CModal isOpen={isOpenPurchaseModal} handleClose={handlePurchaseModalClose}>
      <div className='flex flex-col px-[20px] items-center'>
        <p className='text-[25px] font-black text-text-100 mt-[20px] mb-[60px]'>
          정말 구매하시겠습니까?
        </p>
        <div className='flex flex-row items-center justify-between w-[350px] h-[50px] mb-[20px]'>
          <CButton
            type='green'
            onClick={onClickOkButton}
            width={150}
            height={50}
            rounded={20}
          >
            <p className='text-[22px] font-black text-primary-100'>예</p>
          </CButton>
          <CButton
            type='red'
            onClick={handlePurchaseModalClose}
            width={150}
            height={50}
            rounded={20}
          >
            <p className='text-[22px] font-black text-primary-100'>아니오</p>
          </CButton>
        </div>
      </div>
    </CModal>
  );
};

export default PurchaseModal;
