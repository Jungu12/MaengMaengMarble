import { images } from '@constants/images';

type CharacterInfoProps = {
  name: string;
  img: string;
  point: number;
  handleMyPageModal: () => void;
};

const LobbyCharacterView = ({
  name,
  img,
  point,
  handleMyPageModal,
}: CharacterInfoProps) => {
  return (
    <div className='flex flex-col w-fit h-full justify-between mr-10 p-8 bg-white bg-opacity-50 rounded-[40px]'>
      <div className='flex flex-row items-center justify-between'>
        <p className='text-3xl font-extrabold text-text-100'>{name}</p>
        <button onClick={handleMyPageModal}>
          <img
            className='w-8 h-8'
            src={images.icon.setting}
            alt='캐릭터 설정 버튼'
          />
        </button>
      </div>
      <img
        // src={images.default.character}
        src={img}
        alt='캐릭터 뷰'
        width={'600px'}
        height={'600px'}
      />
      <div className='flex flex-row items-center'>
        <img
          className='w-8 h-8 ml-auto mr-4'
          src={images.icon.point}
          alt='포인트 아이콘'
        />
        <p className='text-xl font-bold text-text-100'>{point}</p>
      </div>
    </div>
  );
};

export default LobbyCharacterView;
