import { CharacterType } from '@/types/common/common.type';
import { images } from '@constants/images';
import { useEffect } from 'react';

type Props = {
  status: 'choice' | 'lock' | 'possession';
  character: CharacterType;
  selectCharater: (id: number) => void;
};

const MyPageCharacterCard = ({ status, character, selectCharater }: Props) => {
  useEffect(() => {
    console.log(character);
  }, [character]);

  return (
    <div className='w-[160px] h-[160px] flex flex-col items-center relative mb-[40px]'>
      <img
        className='w-[156px] h-[156px] rounded-[6px] mb-[20px] z-[1] object-cover'
        src={character.avatarImageBg}
        alt={character.avatarName}
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
        <button
          className='w-[164px] h-[164px] absolute bottom-[0px] z-[2] cursor-pointer'
          onClick={() => selectCharater(character.avatarId)}
        >
          <div
            className='w-[164px] h-[164px] absolute bottom-[0px] z-[2] cursor-pointer'
            style={{
              borderRadius: '10px',
              border: '4px solid #969696',
              background:
                'linear-gradient(180deg, rgba(46, 31, 2, 0.00) 47.92%, rgba(46, 31, 2, 0.54) 79.69%, #2E1F02 93.75%)',
            }}
          ></div>
        </button>
      )}
      <p className='text-xl font-semibold'>{character.avatarName}</p>
    </div>
  );
};

export default MyPageCharacterCard;
