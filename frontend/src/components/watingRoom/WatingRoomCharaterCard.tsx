import { images } from '@constants/images';
import { motion } from 'framer-motion';

type Props = {
  name: string;
  avaterUrl: string;
  isReady: boolean;
  isManager: boolean;
  isClose: boolean;
};

const WatingRoomCharaterCard = ({
  name,
  avaterUrl,
  isReady,
  isManager,
  isClose,
}: Props) => {
  const backgroundImageStyle = isClose
    ? 'linear-gradient(180deg, rgba(0, 0, 0, 0.70) 25.52%, rgba(0, 0, 0, 0.43) 82.73%, rgba(0, 0, 0, 0.00) 100%)'
    : 'linear-gradient(180deg, rgba(255, 255, 255, 0.70) 25.52%, rgba(255, 255, 255, 0.43) 82.73%, rgba(255, 255, 255, 0.00) 100%)';

  return (
    <motion.div
      className='flex flex-col'
      // animate={{ x: 100 }}
      // transition={{ type: 'spring', duration: 0.8 }}
      drag
      dragTransition={{
        power: 0,
        // Snap calculated target to nearest 50 pixels
        modifyTarget: (target) => Math.round(target / 50) * 50,
      }}
    >
      <div
        className='w-[320px] flex flex-col items-center relative'
        style={{
          height: 'calc(100% - 100px)',
          backgroundImage: backgroundImageStyle,
        }}
      >
        {isClose ? (
          <div className='flex items-center justify-center w-full h-full'>
            <img
              className='w-[180px] h-[180px]'
              src={images.waitingRoom.closeWhite}
              alt='none'
            />
          </div>
        ) : (
          <>
            <div className='flex justify-between w-full px-[16px] pt-[12px]'>
              <img
                className='w-8 h-8 cursor-pointer'
                src={images.waitingRoom.info}
                alt='info'
              />
              <img
                className='w-8 h-8 cursor-pointer'
                src={images.waitingRoom.emit}
                alt='강퇴'
              />
            </div>
            {isManager && (
              <img
                src={images.waitingRoom.crown}
                alt='manager'
                className='absolute h-[48px] w-[48px] mt-[4px]'
              />
            )}
            <p className={`text-3xl font-bold mt-[12px]`}>{name}</p>
            {avaterUrl && (
              <img
                className='mt-auto mb-[12px] max-h-[400px]'
                src={avaterUrl}
                alt='character'
              />
            )}
            {isReady && (
              <div
                className='flex items-center pl-[66px] text-white text-[40px] font-bold top absolute z-10 
          rounded-br-[50px] rounded-tr-[50px] w-[260px] h-[64px]
          bg-primary-300 left-0 bottom-[80px] shadow-100'
              >
                <span
                  style={{
                    textShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
                  }}
                >
                  준비완료
                </span>
              </div>
            )}
          </>
        )}
      </div>
      {isClose ? (
        <svg
          xmlns='http://www.w3.org/2000/svg'
          width='321'
          height='81'
          viewBox='0 0 321 81'
          fill='none'
        >
          <path
            d='M320.333 0.333249H0.333364L160.333 80.3333'
            fill='url(#paint0_linear_609_104)'
          />
          <defs>
            <linearGradient
              id='paint0_linear_609_104'
              x1='160.333'
              y1='80.3333'
              x2='160.333'
              y2='0.333252'
              gradientUnits='userSpaceOnUse'
            >
              <stop stopOpacity='0.35' />
              <stop offset='1' stopOpacity='0' />
            </linearGradient>
          </defs>
        </svg>
      ) : (
        <svg
          xmlns='http://www.w3.org/2000/svg'
          width='320'
          height='80'
          viewBox='0 0 320 80'
          fill='none'
        >
          <path
            d='M320 -2.86102e-06H-9.53674e-06L160 80'
            fill='url(#paint0_linear_609_108)'
          />
          <defs>
            <linearGradient
              id='paint0_linear_609_108'
              x1='160'
              y1='80'
              x2='160'
              y2='0'
              gradientUnits='userSpaceOnUse'
            >
              <stop stopColor='white' stopOpacity='0.5' />
              <stop offset='1' stopColor='white' stopOpacity='0' />
            </linearGradient>
          </defs>
        </svg>
      )}
    </motion.div>
  );
};

export default WatingRoomCharaterCard;
