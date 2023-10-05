import MapArea from '@components/gameRoom/MapArea';
import { images } from '@constants/images';
import { motion, useAnimation } from 'framer-motion';

const Test = () => {
  const controls = useAnimation();

  return (
    <div
      className='flex flex-col w-full h-full min-h-[700px] overflow-hidden relative'
      style={{
        backgroundImage: `url(${images.gameRoom.background})`,
        backgroundSize: 'cover',
      }}
    >
      {/* 주사위 버튼*/}
      <div
        className='absolute bottom-[20%] left-[50%] text-5xl text-white z-[10] text-[24px] font-bold'
        style={{
          transform: 'translate(-50%, -50%)',
        }}
      >
        <button
          className='button-3d'
          onClick={() => {
            console.log('버튼 클릭');
          }}
        >
          주사위 굴리기
        </button>
      </div>

      {/* 유저 정보 */}
      <div className='flex flex-col w-full h-full relative'>
        <div className='flex justify-between'>
          <div className='flex w-[360px] h-[160px]'>
            <div className='w-[120px] flex items-center justify-center overflow-hidden relative'>
              <img
                className='object-cover mt-[80px] z-[1]'
                src={images.dummy.dummy1}
                alt='player4'
              />
              <img
                className='w-full h-full absolute object-fill'
                src={images.gameRoom.profileBgRed}
                alt='배경이미지'
              />
            </div>
            <div className='flex flex-col w-[240px]'>
              <div
                className='text-white text-xl font-bold mt-[8px] pb-[8px] pl-[12px] relative'
                style={{
                  /* 테두리 스타일 설정 */
                  borderBottom: '3px solid transparent',
                  borderImage:
                    'linear-gradient(to right, #790317, transparent)',
                  borderImageSlice: '1',
                  borderImageWidth: '0 0 2px 0',
                  borderImageOutset: '0',
                  borderImageRepeat: 'stretch',
                }}
              >
                세상에서가장긴닉
                <img
                  src={images.gameRoom.rankRed}
                  alt='등수'
                  className='absolute right-[16px] top-[4px] w-[56px] h-[56px]'
                />
                <p
                  className='absolute right-[32px] top-[16px]'
                  style={{
                    textShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
                  }}
                >
                  1등
                </p>
              </div>
              <div className='flex text-white mt-[24px] pl-[12px] overflow-hidden relative'>
                <span className='text-xl font-semibold'>총자산</span>
                <div className='flex-1 text-[#7492FF] text-[18px] font-medium ml-[12px] whitespace-nowrap'>
                  <span>3200억 2000만원</span>
                </div>
              </div>
              <div className='text-white pl-[12px] mt-[16px] overflow-hidden relative'>
                <span className='text-xl font-semibold'>보유자산</span>
                <span className='text-[#FFF59D] text-[18px] font-medium ml-[12px] whitespace-nowrap'>
                  1200억 3000만원
                </span>
              </div>
            </div>
          </div>
          <div
            className='flex flex-1 text-white h-[48px] w-[500px] items-center mx-[80px] mt-[24px] overflow-hidden'
            style={{
              background: 'rgba(255, 255, 255, 0.20)',
            }}
          >
            <div
              className='h-full flex items-center justify-center px-[8px] text-[18px] font-semibold text-primary-light100'
              style={{
                background: 'rgba(255, 255, 255, 0.30)',
              }}
            >
              NEWS
            </div>
            <div className='scroll-container overflow-hidden flex-1 font-medium text-[16px]'>
              <motion.div
                className='ml-[8px]'
                initial={{ x: '100%' }} // 시작 위치 - 화면 오른쪽 밖
                animate={{ x: '-100%' }} // 최종 위치 - 화면 왼쪽 밖
                transition={{
                  duration: 10, // 애니메이션 지속 시간 (조절 가능)
                  repeat: Infinity, // 무한 반복
                  repeatType: 'loop', // 반복 유형 설정
                  ease: 'linear', // 선형 이동
                }}
              >
                Your scrolling text goes here. Your scrolling text goes here.
              </motion.div>
            </div>
          </div>
          <div className='flex w-[360px] h-[160px]'>
            <div className='flex flex-col w-[240px]'>
              <div
                className='text-white text-xl font-bold mt-[8px] pb-[8px] pr-[12px] text-right relative'
                style={{
                  /* 테두리 스타일 설정 */
                  borderBottom: '3px solid transparent',
                  borderImage: 'linear-gradient(to left, #055872, transparent)',
                  borderImageSlice: '1',
                  borderImageWidth: '0 0 2px 0',
                  borderImageOutset: '0',
                  borderImageRepeat: 'stretch',
                }}
              >
                세상에서가장긴닉
                <img
                  src={images.gameRoom.rankBlue}
                  alt='등수'
                  className='absolute left-[16px] top-[4px] w-[56px] h-[56px]'
                />
                <p
                  className='absolute left-[30px] top-[16px]'
                  style={{
                    textShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
                  }}
                >
                  2등
                </p>
              </div>
              <div className='flex text-white mt-[24px] pr-[12px] overflow-hidden relative'>
                <span className='text-xl font-semibold mr-[12px]'>총자산</span>
                <div className='flex-1 text-[#7492FF] text-[18px] font-medium whitespace-nowrap'>
                  <span>3200억 2000만원</span>
                </div>
              </div>
              <div className='flex text-white pr-[12px] mt-[16px] overflow-hidden relative'>
                <span className='text-xl font-semibold mr-[12px]'>
                  보유자산
                </span>
                <span className='text-[#FFF59D] text-[18px] font-medium whitespace-nowrap text-right'>
                  1200억 3000만원
                </span>
              </div>
            </div>
            <div className='w-[120px] flex items-center justify-center overflow-hidden relative'>
              <img
                className='object-cover mt-[80px] z-[1]'
                src={images.dummy.dummy1}
                alt='player4'
              />
              <img
                className='w-full h-full absolute'
                src={images.gameRoom.profileBgBlue}
                alt='배경이미지'
              />
            </div>
          </div>
        </div>
        <div className='flex flex-1 justify-between my-[40px] mx-[20px]'>
          {/* 채팅창 */}
          <div className='flex flex-col h-full w-[320px]'>
            <div
              className='flex-1 mb-[12px] text-white p-[12px]'
              style={{
                borderRadius: '16px',
                border: '1px solid rgba(0, 0, 0, 0.30)',
                background: 'rgba(0, 0, 0, 0.25)',
              }}
            >
              {/* 채팅 내용 */}
            </div>
            <input
              className='h-[36px] px-[16px] text-white outline-none font-medium'
              style={{
                borderRadius: '16px',
                border: '1px solid rgba(0, 0, 0, 0.30)',
                background: 'rgba(0, 0, 0, 0.25)',
              }}
            />
          </div>

          {/* 보유주식정보창 */}
          <div
            className='bg-red-100 h-full w-[320px] p-[12px]'
            style={{
              borderRadius: '16px',
              border: '1px solid rgba(255, 255, 255, 0.30)',
              background: 'rgba(255, 255, 255, 0.25)',
            }}
          >
            {
              <div className='w-full h-[56px] bg-white rounded-[8px] opacity-[0.9] flex items-center'>
                <p className='text-[18px] font-semibold mx-[12px]'>LG 화학</p>
                <p className='text-[16px] font-medium'>3억 12만원</p>
                <div className='flex flex-col ml-auto justify-center text-[#2F50FF] text-[14px] font-medium mr-[12px]'>
                  <p className='ml-auto'>-1억 215만원</p>
                  <p className='ml-auto'>-45%</p>
                </div>
              </div>
            }
          </div>
        </div>
        <div className='flex justify-between mt-auto'>
          <div className='flex w-[360px] h-[160px]'>
            <div className='w-[120px] flex items-center justify-center overflow-hidden relative'>
              <img
                className='object-cover mt-[80px] z-[1]'
                src={images.dummy.dummy1}
                alt='player4'
              />
              <img
                className='w-full h-full absolute'
                src={images.gameRoom.profileBgGreen}
                alt='배경이미지'
              />
            </div>
            <div className='flex flex-col w-[240px]'>
              <div
                className='text-white text-xl font-bold mt-[8px] pb-[8px] pl-[12px] relative'
                style={{
                  /* 테두리 스타일 설정 */
                  borderBottom: '3px solid transparent',
                  borderImage:
                    'linear-gradient(to right, #7DAA98, transparent)',
                  borderImageSlice: '1',
                  borderImageWidth: '0 0 2px 0',
                  borderImageOutset: '0',
                  borderImageRepeat: 'stretch',
                }}
              >
                세상에서가장긴닉
                <img
                  src={images.gameRoom.rankGreen}
                  alt='등수'
                  className='absolute right-[16px] top-[4px] w-[56px] h-[56px]'
                />
                <p
                  className='absolute right-[30px] top-[16px]'
                  style={{
                    textShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
                  }}
                >
                  3등
                </p>
              </div>
              <div className='flex text-white mt-[24px] pl-[12px] overflow-hidden relative'>
                <span className='text-xl font-semibold'>총자산</span>
                <div className='flex-1 text-[#7492FF] text-[18px] font-medium ml-[12px] whitespace-nowrap'>
                  <span>3200억 2000만원</span>
                </div>
              </div>
              <div className='text-white pl-[12px] mt-[16px] overflow-hidden relative'>
                <span className='text-xl font-semibold'>보유자산</span>
                <span className='text-[#FFF59D] text-[18px] font-medium ml-[12px] whitespace-nowrap'>
                  1200억 3000만원
                </span>
              </div>
            </div>
          </div>

          <div className='flex w-[360px] h-[160px]'>
            <div className='flex flex-col w-[240px]'>
              <div
                className='text-white text-xl font-bold mt-[8px] pb-[8px] pr-[12px] text-right relative'
                style={{
                  /* 테두리 스타일 설정 */
                  borderBottom: '3px solid transparent',
                  borderImage: 'linear-gradient(to left, #C7B0E3, transparent)',
                  borderImageSlice: '1',
                  borderImageWidth: '0 0 2px 0',
                  borderImageOutset: '0',
                  borderImageRepeat: 'stretch',
                }}
              >
                세상에서가장긴닉
                <img
                  src={images.gameRoom.rankPurple}
                  alt='등수'
                  className='absolute left-[16px] top-[4px] w-[56px] h-[56px]'
                />
                <p
                  className='absolute left-[28px] top-[16px]'
                  style={{
                    textShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
                  }}
                >
                  4등
                </p>
              </div>
              <div className='flex text-white mt-[24px] pr-[12px] overflow-hidden relative'>
                <span className='text-xl font-semibold mr-[12px]'>총자산</span>
                <div className='flex-1 text-[#7492FF] text-[18px] font-medium whitespace-nowrap'>
                  <span>3200억 2000만원</span>
                </div>
              </div>
              <div className='flex text-white pr-[12px] mt-[16px] overflow-hidden relative'>
                <span className='text-xl font-semibold mr-[12px]'>
                  보유자산
                </span>
                <span className='text-[#FFF59D] text-[18px] font-medium whitespace-nowrap text-right'>
                  1200억 3000만원
                </span>
              </div>
            </div>
            <div className='w-[120px] flex items-center justify-center overflow-hidden relative'>
              <img
                className='object-cover mt-[80px] z-[1]'
                src={images.dummy.dummy1}
                alt='player4'
              />
              <img
                className='w-full h-full absolute'
                src={images.gameRoom.profileBgPurple}
                alt='배경이미지'
              />
            </div>
          </div>
        </div>
        <div className='flex'></div>
      </div>
      {/* 게임맵 */}
      <div className='absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 mt-[40px] w-[640px] h-[640px] ]'>
        {/* 필드 타일 */}
        {/* 아랫줄 */}
        <div className='absolute bottom-0 flex justify-between gap-[6px]'>
          <MapArea
            src={images.map.transactionSuspended}
            alt='거래정지'
            value={8}
            width={90}
            height={90}
          />
          <MapArea
            src={images.map.southAfrica}
            alt='남아공'
            value={7}
            width={60}
            height={90}
          />
          <MapArea
            src={images.map.egypt}
            alt='이집트'
            value={6}
            width={60}
            height={90}
          />
          <MapArea
            src={images.map.singapore}
            alt='싱가포르'
            value={5}
            width={60}
            height={90}
          />
          <MapArea
            src={images.map.goldKey1}
            alt='황금열쇠'
            value={4}
            width={60}
            height={90}
          />
          <MapArea
            src={images.map.thailand}
            alt='태국'
            value={3}
            width={60}
            height={90}
          />
          <MapArea
            src={images.map.revenue}
            alt='국세청'
            value={2}
            width={60}
            height={90}
          />
          <MapArea
            src={images.map.vietnam}
            alt='베트남'
            value={1}
            width={60}
            height={90}
          />
          <MapArea
            src={images.map.start}
            alt='시작점'
            value={0}
            width={90}
            // height={90}
          />
        </div>
        {/* 왼쪽줄 */}
        <div className='absolute left-0 bottom-[96px] flex flex-col justify-between gap-[5px]'>
          <MapArea
            src={images.map.saudiArabia}
            alt='사우디아라비아'
            value={15}
            width={90}
            height={60}
          />
          <MapArea
            src={images.map.iran}
            alt='이란'
            value={14}
            width={90}
            height={60}
          />
          <MapArea
            src={images.map.catarrh}
            alt='카타르'
            value={13}
            width={90}
            height={60}
          />
          <MapArea
            src={images.map.goldKey2}
            alt='황금열쇠'
            value={12}
            width={90}
            height={60}
          />
          <MapArea
            src={images.map.brazil}
            alt='브라질'
            value={11}
            width={90}
            height={60}
          />
          <MapArea
            src={images.map.macau}
            alt='마카오'
            value={10}
            width={90}
            height={60}
          />
          <MapArea
            src={images.map.argentina}
            alt='아르헨티나'
            value={9}
            width={90}
            height={60}
          />
        </div>
        {/* 윗줄 */}
        <div className='absolute top-0 flex justify-between gap-[6px]'>
          <MapArea
            src={images.map.stockMarket}
            alt='주식장'
            value={16}
            width={90}
            height={90}
          />
          <MapArea
            src={images.map.czech}
            alt='체코'
            value={17}
            width={60}
            height={90}
          />
          <MapArea
            src={images.map.rushAndCash}
            alt='대출'
            value={18}
            width={60}
            height={90}
          />
          <MapArea
            src={images.map.russia}
            alt='러시아'
            value={19}
            width={60}
            height={90}
          />
          <MapArea
            src={images.map.goldKey3}
            alt='황금열쇠3'
            value={20}
            width={60}
            height={90}
          />
          <MapArea
            src={images.map.france}
            alt='프랑스'
            value={21}
            width={60}
            height={90}
          />
          <MapArea
            src={images.map.germany}
            alt='독일'
            value={22}
            width={60}
            height={90}
          />
          <MapArea
            src={images.map.uk}
            alt='영국'
            value={23}
            width={60}
            height={90}
          />
          <MapArea
            src={images.map.anywhere}
            alt='어디로든 문'
            value={24}
            width={90}
            height={90}
          />
        </div>
        {/* 오른쪽줄 */}
        <div className='absolute right-0 top-[96px] bottom-[96px] flex flex-col justify-between gap-[5px]'>
          <MapArea
            src={images.map.usa}
            alt='미국'
            value={25}
            width={90}
            height={60}
          />
          <MapArea
            src={images.map.canada}
            alt='캐나다'
            value={26}
            width={90}
            height={60}
          />
          <MapArea
            src={images.map.India}
            alt='인도'
            value={27}
            width={90}
            height={60}
          />
          <MapArea
            src={images.map.goldKey4}
            alt='황금열쇠'
            value={28}
            width={90}
            height={60}
          />
          <MapArea
            src={images.map.china}
            alt='중국'
            value={29}
            width={90}
            height={60}
          />
          <MapArea
            src={images.map.japan}
            alt='일본'
            value={30}
            width={90}
            height={60}
          />
          <MapArea
            src={images.map.korea}
            alt='한국'
            value={31}
            width={90}
            height={60}
          />
        </div>
        {/* 캐릭터 */}
        <motion.div
          animate={controls}
          className={`absolute`}
          style={{
            bottom: `30px`,
            right: `20px`,
          }}
        >
          <img
            className='w-[56px] h-[56px]'
            src={images.default.character}
            alt='캐릭터'
          />
        </motion.div>
      </div>
    </div>
  );
};

export default Test;
