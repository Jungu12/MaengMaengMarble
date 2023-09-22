import React, { useState } from 'react';
import { images } from '@constants/images';
import { motion, AnimatePresence } from 'framer-motion';
import PeopleRadioButton from './PeopleRadioButton';

type NewRoomModalProps = {
  isOpenNewRoomModal: boolean;
  setIsOpenNewRoomModal: React.Dispatch<React.SetStateAction<boolean>>;
};

const NewRoomModal = ({
  isOpenNewRoomModal,
  setIsOpenNewRoomModal,
}: NewRoomModalProps) => {
  // 방 제목 입력 관리
  const [roomName, setRoomName] = useState('');
  const onChange = (event: React.FormEvent<HTMLInputElement>) => {
    const { currentTarget: roomName } = event;
    if (roomName.value.length <= 12) {
      setRoomName(roomName.value);
    }
    console.log(roomName);
  };
  const onSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log(roomName);
  };

  // 인원수 체크 관리
  const [selectedOption, setSelectedOption] = useState('2');

  const handleOptionChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSelectedOption(event.target.value);
    console.log(event.target.value);
  };

  return (
    <AnimatePresence>
      {isOpenNewRoomModal && (
        <div
          className='absolute flex w-full h-full bg-black bg-opacity-50 items-center justify-center'
          style={{
            zIndex: 3,
          }}
        >
          <form onSubmit={onSubmit}>
            <motion.div
              className='flex flex-col bg-primary-100 rounded-[40px] p-[20px]'
              initial={{
                opacity: 0,
                scale: 0.75,
              }}
              animate={{
                opacity: 1,
                scale: 1,
                transition: {
                  ease: 'easeOut',
                  duration: 0.15,
                },
              }}
              exit={{
                opacity: 0,
                scale: 0.75,
                transition: {
                  ease: 'easeIn',
                  duration: 0.15,
                },
              }}
            >
              <button
                className='ml-auto'
                onClick={() => {
                  setIsOpenNewRoomModal(false);
                  setRoomName('');
                  setSelectedOption('2');
                }}
              >
                <img
                  className='w-10 h-10'
                  src={images.button.close}
                  alt='닫기 버튼'
                />
              </button>
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
            </motion.div>
          </form>
        </div>
      )}
    </AnimatePresence>
  );
};

export default NewRoomModal;
