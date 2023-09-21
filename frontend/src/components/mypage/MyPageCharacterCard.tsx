import { images } from '@constants/images';
import {} from 'react';

type Props = {
  src: string;
  alt: string;
  name: string;
  status: 'choice' | 'lock' | 'possession';
};

const MyPageCharacterCard = ({ src, alt, name, status }: Props) => {
  return (
    <div className='w-[160px] h-[160px] flex flex-col items-center relative'>
      <img
        className='w-[156xp] h-[156px] rounded-[6px] mb-[20px] z-[1]'
        src={src}
        alt={alt}
      />
      {status === 'choice' && (
        <>
          <img
            className='absolute w-[32px] h-[32px] bottom-[-16px] z-[3]'
            src={images.mypage.choice}
            alt=''
          />
          <div
            className='w-[164px] h-[164px] absolute bottom-[0px]'
            style={{
              borderRadius: '10px',
              border: '4px solid transparent',
              backgroundImage:
                'linear-gradient(#fff, #fff), linear-gradient(to right, #f9d339 0%,  #f5e5a6 100%)',
              backgroundOrigin: 'border-box',
              backgroundClip: 'content-box, border-box',
            }}
          ></div>
        </>
      )}
      {status === 'lock' && (
        <>
          <img
            className='absolute w-[32px] h-[32px] bottom-[-16px] z-[3]'
            src={images.mypage.lock}
            alt=''
          />
          <div
            className='w-[164px] h-[164px] absolute bottom-[0px] z-[2]'
            style={{
              borderRadius: '10px',
              border: '4px solid #3e3e3e',
              background: 'rgba(0, 0, 0, 0.70)',
            }}
          ></div>
        </>
      )}
      {status === 'possession' && (
        <div
          className='w-[164px] h-[164px] absolute bottom-[0px] z-[2]'
          style={{
            borderRadius: '10px',
            border: '4px solid #68634F',
            background:
              'linear-gradient(180deg, rgba(46, 31, 2, 0.00) 47.92%, rgba(46, 31, 2, 0.54) 79.69%, #2E1F02 93.75%)',
          }}
        ></div>
      )}
      <p className='text-xl font-semibold'>{name}</p>
    </div>
  );
};

export default MyPageCharacterCard;
