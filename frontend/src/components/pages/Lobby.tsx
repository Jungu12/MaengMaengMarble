import RoomInfoCard from '@components/lobby/RoomInfoCard';
import { images } from '@constants/images';

const Lobby = () => {
  return (
    <div
      className='flex flex-col w-full h-full overflow-hidden relative p-[45px]'
      style={{
        backgroundImage: `url(${images.lobby.background})`,
        backgroundSize: 'cover',
      }}
    >
      <div className='flex flex-row w-full items-center justify-between'>
        <h1 className='px-[30px] text-left text-5xl font-inooariduri text-white'>
          맹맹마블
        </h1>
        <div className='flex items-center space-x-7 px-6 py-3 bg-white bg-opacity-50 rounded-[40px] shadow'>
          <button>
            <img className='w-10 h-10' src={images.icon.volume_on} alt='' />
          </button>
          <button>
            <img className='w-10 h-10' src={images.icon.shop} alt='' />
          </button>
          <button>
            <img className='w-10 h-10' src={images.icon.friend} alt='' />
          </button>
        </div>
      </div>

      <div className='flex flex-row w-full h-full items-center justify-between mt-5'>
        <div className='flex flex-col w-fit h-full justify-between mr-10 p-8 bg-white bg-opacity-50 rounded-[40px]'>
          <div className='flex flex-row items-center justify-between'>
            <p className='text-3xl font-extrabold text-text-100'>상근시치</p>
            <button>
              <img className='w-8 h-8' src={images.icon.setting} alt='' />
            </button>
          </div>
          <img
            src={images.default.character}
            alt=''
            width={'600px'}
            height={'600px'}
          />
          <div className='flex flex-row items-center'>
            <img
              className='w-8 h-8 ml-auto mr-4'
              src={images.icon.point}
              alt=''
            />
            <p className='text-xl font-bold text-text-100'>18,000</p>
          </div>
        </div>
        <div className='flex flex-col w-full h-full p-12 justify-between bg-white bg-opacity-50 rounded-[40px]'>
          <div className='grid grid-cols-2 gap-10 place-content-between place-items-stretch'>
            <RoomInfoCard title='맹맹 시치 모여라~' currentCnt='3' />
            <RoomInfoCard title='모마말고 맹맹' currentCnt='1' />
            <RoomInfoCard title='맹맹맹맹' currentCnt='2' />
            <RoomInfoCard title='야호' currentCnt='4' />
          </div>
          <br></br>
          <div className='flex flex-row items-center justify-center'>
            <img className='h-10 mr-8' src={images.icon.left} alt='' />
            <img className='h-10 ml-8' src={images.icon.right} alt='' />
          </div>
          <div className='flex flex-row items-center justify-between'>
            <button className='flex flex-row px-5 py-3 items-center justify-between bg-primary-dark100 rounded-[15px]'>
              <img className='h-[18px] mr-3' src={images.icon.invite} alt='' />
              <p className='text-[18px] font-bold text-white'>초대 코드 입력</p>
            </button>
            <button className='flex flex-row px-5 py-3 items-center justify-between bg-primary-dark100 rounded-[15px]'>
              <img className='h-[18px] mr-3' src={images.icon.plus} alt='' />
              <p className='text-[18px] font-bold text-white'>새로운 방 생성</p>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Lobby;
