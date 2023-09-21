import CModal from '@components/common/CModal';
import {} from 'react';

type NewRoomModalProps = {
  isOpenNewRoomModal: boolean;
  handleMyPageModalClose: () => void;
};

const MyPageModal = ({
  isOpenNewRoomModal,
  handleMyPageModalClose,
}: NewRoomModalProps) => {
  return (
    <CModal isOpen={isOpenNewRoomModal} handleClose={handleMyPageModalClose}>
      <div></div>
    </CModal>
  );
};

export default MyPageModal;
