import CModal from '@components/common/CModal';
import { useCallback, useState } from 'react';
import { images } from '@constants/images';
import { motion } from 'framer-motion';
import CButton from '@components/common/CButton';
import useToastList from '@hooks/useToastList';
import { useSetRecoilState } from 'recoil';
import { ToastMessageState } from '@atom/toastAtom';

type InviteModalProps = {
  isOpenInviteModal: boolean;
  handleInviteModalClose: () => void;
};

const InviteModal = ({
  isOpenInviteModal,
  handleInviteModalClose,
}: InviteModalProps) => {
  const inviteCodeConfirm = false;
  const [inviteCode, setInviteCode] = useState('');
  const { show } = useToastList();
  const setToastMessage = useSetRecoilState(ToastMessageState);

  const onChange = (event: React.FormEvent<HTMLInputElement>) => {
    const { currentTarget: inviteCode } = event;
    setInviteCode(inviteCode.value);
  };

  const handleClose = useCallback(() => {
    handleInviteModalClose();
    setInviteCode('');
  }, [handleInviteModalClose]);

  const onClickEnterRoom = useCallback(() => {
    console.log(inviteCode);
    handleClose();
    if (inviteCodeConfirm) {
      show('success');
    } else {
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '존재하지 않는 초대코드입니다.',
        };
      });
      show('error');
    }
  }, [inviteCode, handleClose, inviteCodeConfirm, show, setToastMessage]);

  return (
    <CModal isOpen={isOpenInviteModal} handleClose={handleClose}>
      <div className='flex flex-col items-center'>
        <p className='text-[30px] font-black text-text-100 mt-[20px] mb-[40px]'>
          초대코드를 입력하세요
        </p>
        <div className='flex flex-row w-[562px] h-[80px] items-center bg-primary-light100 rounded-[20px] mx-10 mb-[40px] px-[25px]'>
          <img
            className='flex-none w-9 h-9 mr-[25px]'
            src={images.icon.letter}
            alt='초대 아이콘'
          />
          <div className='flex-1 text-text-100 text-2xl font-extrabold'>
            <input
              className='w-full outline-none placeholder:text-text-hint hover:border-transparent bg-primary-light100'
              value={inviteCode}
              onChange={onChange}
              type='text'
              placeholder='전달받은 초대코드를 입력해주세요!'
            />
          </div>
        </div>
        <motion.div
          className='ml-auto mr-auto mb-[20px]'
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.9 }}
          transition={{ type: 'spring', stiffness: 400, damping: 17 }}
        >
          <CButton
            type='green'
            onClick={onClickEnterRoom}
            width={150}
            height={50}
            rounded={20}
          >
            <p className='text-[22px] font-black text-primary-100'>입장하기</p>
          </CButton>
        </motion.div>
      </div>
    </CModal>
  );
};

export default InviteModal;
