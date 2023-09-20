import React from 'react';
import { images } from '@constants/images';

type StoreCharacterInfoProps = {
  have: boolean;
  img: string;
  name: string;
  point: string;
};

const StoreCharacterCard = ({
  have,
  img,
  name,
  point,
}: StoreCharacterInfoProps) => {
  return (
    <div
      className={`flex flex-col w-fit h-fit items-center justify-center ${
        have ? 'bg-primary-500' : 'bg-primary-dark300'
      } p-[10px] rounded-[10px] relative`}
    >
      <div className='w-full relative mb-3'>
        <img className='w-full relative' src={img} alt='캐릭터 뷰' />
        <img
          className='w-full absolute top-0 left-0 bottom-0 right-0 z-10'
          src={have ? images.store.buygra : images.store.boughtgra}
          alt='그라데이션 뷰'
        />
        <p
          className={`w-full ${
            have ? 'text-primary-dark300' : 'text-primary-100 opacity-70'
          } z-20 absolute bottom-0 left-0 text-center font-semibold`}
        >
          {name}
        </p>
      </div>
      <button
        className='w-full flex aspect-[7/2.229]'
        style={{
          backgroundImage: `url(${
            have ? images.button.buy : images.button.bought
          })`,
          backgroundSize: 'contain',
        }}
      >
        {have && (
          <div className='flex w-full h-full flex-row items-center px-8 justify-between'>
            <img className='h-7' src={images.icon.point} alt='코인 아이콘' />
            <p className='text-[18px] font-black text-text-100'>{point}</p>
          </div>
        )}
      </button>
    </div>
  );
};

export default StoreCharacterCard;
