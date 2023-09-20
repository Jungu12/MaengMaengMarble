import React from 'react';
import { images } from '@constants/images';

const Store = () => {
  return (
    <div
      className='flex flex-row w-full h-full justify-between relative'
      style={{
        backgroundImage: `url(${images.store.background})`,
        backgroundSize: 'cover',
      }}
    >
      <div className='basis-1/3 flex flex-col h-full items-center justify-end mr-[25px] relative'>
        <div
          className='flex w-[380px] h-[215px] items-center justify-center relative'
          style={{
            backgroundImage: `url(${images.store.bubble})`,
            backgroundSize: 'cover',
          }}
        >
          <p className='text-[25px] leading-normal font-black text-text-100 whitespace-pre-wrap pb-6'>
            {'상점은\n너굴맨이 지켰으니\n안심하라구~'}
          </p>
        </div>
        <img className='w-full' src={images.store.owner} alt='너굴맨' />
      </div>

      <div className='basis-2/3 flex flex-col h-full items-end justify-between py-[45px] pr-[45px] relative'>
        <div className='flex flex-row items-center space-x-7 px-6 py-3 mb-[25px] bg-white bg-opacity-50 rounded-[40px]'>
          <img
            className='w-[40px] h-[40px] ml-auto mr-4'
            src={images.icon.point}
            alt='포인트 아이콘'
          />
          <p className='text-[30px] font-bold text-text-100'>28,000</p>
        </div>

        <div className='flex flex-col w-full h-full p-[25px] relative bg-primary-dark300 bg-opacity-70 rounded-[40px]'>
          <div className='flex flex-col w-full h-full overflow-auto relative bg-white scrollbar'>
            {/* <div className='flex flex-col w-full h-full relative scrollbar'> */}
            <p className='text-[30px] font-bold text-text-100'>28,000</p>
            <p>캐릭터 창</p>
            <p className='text-[30px] font-bold text-text-100'>28,000</p>
            <p>캐릭터 창</p>
            <p className='text-[30px] font-bold text-text-100'>28,000</p>
            <p>캐릭터 창</p>
            <p className='text-[30px] font-bold text-text-100'>28,000</p>
            <p>캐릭터 창</p>
            <p className='text-[30px] font-bold text-text-100'>28,000</p>
            <p>캐릭터 창</p>
            <p className='text-[30px] font-bold text-text-100'>28,000</p>
            <p>캐릭터 창</p>
            <p className='text-[30px] font-bold text-text-100'>28,000</p>
            <p>캐릭터 창</p>
            <p className='text-[30px] font-bold text-text-100'>28,000</p>
            <p>캐릭터 창</p>
            <p className='text-[30px] font-bold text-text-100'>28,000</p>
            <p>캐릭터 창</p>
            <p className='text-[30px] font-bold text-text-100'>28,000</p>
            <p>캐릭터 창</p>
            <p className='text-[30px] font-bold text-text-100'>28,000</p>
            <p>캐릭터 창</p>
            <p className='text-[30px] font-bold text-text-100'>28,000</p>
            <p>캐릭터 창</p>
            <p className='text-[30px] font-bold text-text-100'>28,000</p>
            <p>캐릭터 창</p>
            <p className='text-[30px] font-bold text-text-100'>28,000</p>
            <p>캐릭터 창</p>
            {/* </div> */}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Store;
