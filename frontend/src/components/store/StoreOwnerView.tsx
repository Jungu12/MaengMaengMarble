import React from 'react';
import { images } from '@constants/images';

const StoreOwnerView = () => {
  return (
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
  );
};

export default StoreOwnerView;
