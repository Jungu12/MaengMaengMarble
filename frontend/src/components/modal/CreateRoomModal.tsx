import React, { useState } from 'react';
import { images } from '@constants/images';
import { motion } from 'framer-motion';
import PeopleRadioButton from '../lobby/PeopleRadioButton';
import CModal from '@components/common/CModal';
import { createRoom } from '@apis/lobbyApi';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userState } from '@atom/userAtom';
import useToastList from '@hooks/useToastList';
import { ToastMessageState } from '@atom/toastAtom';

type CreateRoomModalProps = {
  isOpenCreateRoomModal: boolean;
  handleCreateRoomModalClose: () => void;
};

const CreateRoomModal = ({
  isOpenCreateRoomModal,
  handleCreateRoomModalClose,
}: CreateRoomModalProps) => {
  const navigation = useNavigate();
  const user = useRecoilValue(userState);
  // 방 제목 입력 관리
  const [roomName, setRoomName] = useState('');
  // 인원수 체크 관리
  const [selectedOption, setSelectedOption] = useState('2');
  const { show } = useToastList();
  const setToastMessage = useSetRecoilState(ToastMessageState);

  const onChange = (event: React.FormEvent<HTMLInputElement>) => {
    const { currentTarget: roomName } = event;
    if (roomName.value.length <= 12) {
      setRoomName(roomName.value);
    }
  };
  const onSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (!user) return;

    if (roomName.length <= 0) {
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '방제목을 입력하세요.',
        };
      });
      show('error');
    } else {
      createRoom(
        {
          userId: user?.userId,
          nickname: user?.nickname,
          characterId: user?.avatarId,
        },
        roomName,
        selectedOption
      )
        .then((res) => {
          console.log(res);
          navigation(`/waiting-room/${res.roomCode}`);
        })
        .catch(() => {
          // 에러 토스트메시지 출력
        });
    }
    console.log(roomName);
  };

  const handleOptionChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSelectedOption(event.target.value);
    console.log(event.target.value);
  };

  const handleClose = () => {
    handleCreateRoomModalClose();
    setRoomName('');
    setSelectedOption('2');
  };

  return (
    <CModal isOpen={isOpenCreateRoomModal} handleClose={handleClose}>
      <form onSubmit={onSubmit}>
        <div className='flex flex-col'>
          <p className='text-4xl font-extrabold text-text-100 ml-auto mr-auto mt-4 mb-[70px]'>
            방 만들기
          </p>
          <div className='flex flex-row w-[562px] h-[80px] items-center bg-primary-light100 rounded-[20px] mx-10 mb-[60px] px-[25px]'>
            <img
              className='flex-none w-9 h-9 mr-[25px]'
              src={images.icon.room}
              alt='방 아이콘'
            />
            <div className='flex-1 text-text-100 text-2xl font-extrabold'>
              <input
                className='w-full outline-none placeholder:text-text-hint hover:border-transparent bg-primary-light100'
                value={roomName}
                onChange={onChange}
                type='text'
                placeholder='방 이름을 지어주세요! (최대 12자)'
              />
            </div>
          </div>
          <div className='flex flex-row relative w-[562px] h-[80px] items-center justify-between bg-primary-light100 rounded-[20px] mx-10 mb-[70px] px-[25px]'>
            <img
              className='w-9 h-9'
              src={images.icon.people}
              alt='사람 아이콘'
            />
            <PeopleRadioButton
              label='2인'
              value='2'
              checked={selectedOption === '2'}
              onChange={handleOptionChange}
            />
            <PeopleRadioButton
              label='3인'
              value='3'
              checked={selectedOption === '3'}
              onChange={handleOptionChange}
            />
            <PeopleRadioButton
              label='4인'
              value='4'
              checked={selectedOption === '4'}
              onChange={handleOptionChange}
            />
          </div>
          <motion.div
            className='ml-auto mr-auto mb-[20px]'
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            transition={{ type: 'spring', stiffness: 400, damping: 17 }}
          >
            <button type='submit'>
              <img
                className='w-[135px] mb-[20px]'
                src={images.button.create}
                alt='방 생성 버튼'
              />
            </button>
          </motion.div>
        </div>
      </form>
    </CModal>
  );
};

export default CreateRoomModal;
