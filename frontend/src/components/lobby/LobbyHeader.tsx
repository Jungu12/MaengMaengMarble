import React from 'react';
import { images } from '@constants/images';
import { useNavigate } from 'react-router-dom';

const LobbyHeader = () => {
  const navigation = useNavigate();

  return (
    <div className='flex flex-row w-full items-center justify-between'>
      <h1 className='px-[30px] text-left text-5xl font-inooariduri text-white'>
        맹맹마블
      </h1>
      <div className='flex items-center space-x-7 px-6 py-3 bg-white bg-opacity-50 rounded-[40px] shadow'>
        <button>
          <img
            className='w-10 h-10'
            src={images.icon.volume_on}
            alt='볼륨 버튼'
          />
        </button>
        <button onClick={() => navigation('/store')}>
          <img className='w-10 h-10' src={images.icon.shop} alt='상점 버튼' />
        </button>
        <button>
          <img
            className='w-10 h-10'
            src={images.icon.friend}
            alt='친구 기능 버튼'
          />
        </button>
      </div>
    </div>
  );
};

export default LobbyHeader;
