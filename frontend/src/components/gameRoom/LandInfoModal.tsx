import { motion, AnimatePresence } from 'framer-motion';
import { images } from '@constants/images';
import { useRef, useCallback, useEffect, useMemo } from 'react';
import {
  addAmountUnit,
  calCurrentFees,
  landColor,
  landLandMarksImage,
  landNationalFlag,
} from '@utils/game';
import { LandType } from '@/types/gameRoom/game.type';

type Props = {
  landInfo: LandType;
  isOpen: boolean;
  handleLandInfo: () => void;
};

const LandInfoModal = ({ landInfo, isOpen, handleLandInfo }: Props) => {
  // const landInfo = useMemo(() => lands[landId], [landId]);
  const fee = useMemo(() => calCurrentFees(landInfo), [landInfo]);

  const modalRef = useRef<HTMLDivElement>(null);

  const modalOutSideClick = (
    e: React.MouseEvent<HTMLDivElement, MouseEvent>
  ) => {
    if (modalRef.current === e.target) {
      handleLandInfo();
    }
  };

  const handleEscKey = useCallback(
    (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        // ESC 키가 눌렸을 때 실행할 동작을 여기에 추가
        handleLandInfo();
        // 예를 들어 모달을 닫는 함수를 호출하거나 다른 동작을 수행할 수 있습니다.
      }
    },
    [handleLandInfo]
  );

  // 컴포넌트가 마운트될 때 이벤트 리스너 추가
  useEffect(() => {
    if (isOpen) {
      window.addEventListener('keydown', handleEscKey);
    }

    // 컴포넌트가 언마운트될 때 이벤트 리스너 제거
    return () => {
      window.removeEventListener('keydown', handleEscKey);
    };
  }, [handleEscKey, isOpen]);

  return (
    <AnimatePresence>
      {isOpen && (
        <div
          className='absolute flex w-full h-full bg-black bg-opacity-50 items-center justify-center'
          style={{
            zIndex: 1000,
          }}
          ref={modalRef}
          onClick={(e) => modalOutSideClick(e)}
          aria-hidden='true'
        >
          <motion.div
            initial={{
              opacity: 0,
              scale: 0.75,
            }}
            animate={{
              opacity: 1,
              scale: 1,
              transition: {
                ease: 'easeOut',
                duration: 0.15,
              },
            }}
            exit={{
              opacity: 0,
              scale: 0.75,
              transition: {
                ease: 'easeIn',
                duration: 0.15,
              },
            }}
            className='flex flex-col w-fit h-fit bg-primary-100 rounded-[20px] relative'
          >
            <div className='flex flex-row relative items-center justify-between w-[600px] h-[90px] mr-[60px] my-[20px]'>
              <div
                className='w-full h-[70px] rounded-tr-[15px] rounded-br-[15px] absolute left-0 z-0'
                style={{
                  backgroundColor: `${landColor(landInfo.landId)}`,
                  boxShadow:
                    '0px -1.39534px 2.79068px 0px #68634F inset, 0px -1.39534px 2.79068px 0px #68634F inset, 1.39534px -1.39534px 6.97669px 0px #68634F inset',
                }}
              />
              <img
                className='w-[90px] h-[90px] absolute top-0 left-[15px] z-5'
                src={images.gameRoom.flagbackground}
                alt='국기 테두리'
              />
              <div className='flex flex-row w-full absolute items-center justify-between left-[15px] pr-[15px] z-10'>
                <img
                  className='w-[90px] h-[90px]'
                  src={landNationalFlag(landInfo.landId)}
                  alt='국기 사진'
                />
                <p className='text-primary-light100 font-bold text-[30px] '>
                  {landInfo.name}
                </p>
                <button onClick={handleLandInfo}>
                  <img
                    className='w-[60px] h-[60px] '
                    src={images.button.gameclose}
                    alt='닫기 버튼'
                  />
                </button>
              </div>
            </div>
            <div className='flex flex-row w-full items-center justify-between px-[30px] mb-[20px]'>
              <div className='w-[281px] h-[413px] relative mr-[20px]'>
                <img
                  className='w-full h-full'
                  src={landLandMarksImage(landInfo.landId)}
                  alt='랜드마크 이미지'
                />
                <div className='flex flex-row items-center absolute w-11/12 h-fit rounded-tr-[15px] rounded-br-[15px] bg-black bg-opacity-20 left-0 z-20 top-[10px]'>
                  <img
                    className='w-[50px] h-[50px] mx-[10px]'
                    src={images.icon.dice}
                    alt='주사위 아이콘'
                  />
                  <p className='text-primary-light100 font-bold text-[22px] py-[10px]'>
                    여기까지 4칸
                  </p>
                </div>
              </div>
              <div className='flex-1 flex-col h-[413px] bg-primary-light100 px-[30px] py-[10px]'>
                <table className='w-full text-[18px] text-text-100 font-bold mb-[20px]'>
                  <tbody>
                    <tr className='border-b-[1px] border-text-hint'>
                      <th className='text-center p-[10px]'>건설</th>
                      <th className='text-center p-[10px]'>가격</th>
                    </tr>
                    <tr className='border-b-[1px] border-text-hint'>
                      <td className='text-center p-[10px]'>땅</td>
                      <td className='text-right p-[10px]'>
                        {addAmountUnit(landInfo.currentLandPrice)}
                      </td>
                    </tr>
                    <tr className='border-b-[1px] border-text-hint'>
                      <td className='text-center p-[10px]'>별장</td>
                      <td className='text-right p-[10px]'>
                        {addAmountUnit(landInfo.currentBuildingPrices[0])}
                      </td>
                    </tr>
                    <tr className='border-b-[1px] border-text-hint'>
                      <td className='text-center p-[10px]'>빌딩</td>
                      <td className='text-right p-[10px]'>
                        {addAmountUnit(landInfo.currentBuildingPrices[1])}
                      </td>
                    </tr>
                    <tr className='border-b-[1px] border-text-hint'>
                      <td className='text-center p-[10px]'>호텔</td>
                      <td className='text-right p-[10px]'>
                        {addAmountUnit(landInfo.currentBuildingPrices[2])}
                      </td>
                    </tr>
                  </tbody>
                </table>
                <table className='w-full text-[18px] text-text-100 font-bold mb-[10px]'>
                  <tbody>
                    <tr className='border-y-[1px] border-text-hint bg-primary-100'>
                      <td className='text-center p-[10px]'>통행료</td>
                      <td className='text-right p-[10px]'>
                        {fee === 0 ? '없음' : addAmountUnit(fee)}
                      </td>
                    </tr>
                  </tbody>
                </table>
                <table className='w-full text-[18px] text-text-100 font-bold mb-[10px]'>
                  <tbody>
                    <tr className='border-y-[1px] border-text-hint bg-primary-100'>
                      <td className='text-center p-[10px]'>인수비용</td>
                      <td className='text-right p-[10px]'>
                        {fee === 0 ? '없음' : addAmountUnit(fee * 2)}
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <div className='flex flex-col w-full h-fit px-[30px] mb-[30px] relative'>
              <div className='flex flex-col bg-primary-200 w-full h-full py-[30px] items-center'>
                <p className='text-text-hint font-bold text-[22px]'>
                  적용중인 효과 없음
                </p>
              </div>
            </div>
          </motion.div>
        </div>
      )}
    </AnimatePresence>
  );
};

export default LandInfoModal;
