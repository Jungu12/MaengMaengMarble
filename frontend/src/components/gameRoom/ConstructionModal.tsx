import { AnimatePresence, motion } from 'framer-motion';
import { useMemo, useState, useCallback } from 'react';
import { addAmountUnit, landColor, landNationalFlag } from '@utils/game';
import { images } from '@constants/images';
import BuildingCard from './BuildingCard';
import CButton from '@components/common/CButton';
import { LandType, PlayerType } from '@/types/gameRoom/game.type';
import useToastList from '@hooks/useToastList';
import { useSetRecoilState } from 'recoil';
import { ToastMessageState } from '@atom/toastAtom';

type Props = {
  player: PlayerType | null;
  land: LandType;
  isOpen: boolean;
  handleConstruction: (purchase: boolean[]) => void;
  handleClose: () => void;
};

const ConstructionModal = ({
  player,
  land,
  isOpen,
  handleConstruction,
  handleClose,
}: Props) => {
  const noMore =
    land.owner != -1 &&
    land.buildings[0] &&
    (land.buildings[1] || (player ? 1 - player.currentLap > 0 : true)) &&
    (land.buildings[2] || (player ? 2 - player.currentLap > 0 : true));
  const { show } = useToastList();
  const setToastMessage = useSetRecoilState(ToastMessageState);
  const [isCheckedPension, setIsCheckedPension] = useState(land.buildings[0]);
  const [isCheckedBuilding, setIsCheckedBuilding] = useState(land.buildings[1]);
  const [isCheckedHotel, setIsCheckedHotel] = useState(land.buildings[2]);
  const totalPurchasePrice = useMemo(
    () =>
      land.owner != -1
        ? 0
        : land.currentLandPrice +
          (land.buildings[0]
            ? 0
            : isCheckedPension
            ? land.currentBuildingPrices[0]
            : 0) +
          (land.buildings[1]
            ? 0
            : isCheckedBuilding
            ? land.currentBuildingPrices[1]
            : 0) +
          (land.buildings[2]
            ? 0
            : isCheckedHotel
            ? land.currentBuildingPrices[2]
            : 0),
    [
      isCheckedBuilding,
      isCheckedHotel,
      isCheckedPension,
      land.buildings,
      land.currentBuildingPrices,
      land.currentLandPrice,
      land.owner,
    ]
  );

  const handleCheckPension = useCallback(() => {
    setIsCheckedPension((prev) => !prev);
  }, []);

  const handleCheckBuilding = useCallback(() => {
    setIsCheckedBuilding((prev) => !prev);
  }, []);

  const handleCheckHotel = useCallback(() => {
    setIsCheckedHotel((prev) => !prev);
  }, []);

  const onClickPurchase = useCallback(() => {
    if (totalPurchasePrice == 0) {
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '구매할 건물을 선택하세요.',
        };
      });
      show('error');
    } else if (player && player.money >= totalPurchasePrice) {
      handleConstruction([
        land.buildings[0] ? false : isCheckedPension,
        land.buildings[1] ? false : isCheckedBuilding,
        land.buildings[2] ? false : isCheckedHotel,
      ]);
      setIsCheckedPension(false);
      setIsCheckedBuilding(false);
      setIsCheckedHotel(false);
      console.log(totalPurchasePrice);
    } else {
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '보유 현금이 부족합니다.',
        };
      });
      show('error');
    }
  }, [
    handleConstruction,
    isCheckedBuilding,
    isCheckedHotel,
    isCheckedPension,
    land.buildings,
    player,
    setToastMessage,
    show,
    totalPurchasePrice,
  ]);

  return (
    <AnimatePresence>
      {isOpen && (
        <div
          className='absolute flex w-full h-full bg-black bg-opacity-50 items-center justify-center'
          style={{
            zIndex: 100,
          }}
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
                  backgroundColor: `${landColor(land.landId)}`,
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
                  src={landNationalFlag(land.landId)}
                  alt='국기 사진'
                />
                <p className='text-primary-light100 font-bold text-[30px] '>
                  {land.name}
                </p>
                <button onClick={handleClose}>
                  <img
                    className='w-[60px] h-[60px] '
                    src={images.button.gameclose}
                    alt='닫기 버튼'
                  />
                </button>
              </div>
            </div>
            <div className='flex flex-row w-full space-x-[15px] items-center justify-between px-[30px] mb-[20px]'>
              <BuildingCard
                type='땅값'
                price={land.currentLandPrice}
                width={150}
                height={190}
              />
              <BuildingCard
                type='별장'
                price={land.currentBuildingPrices[0]}
                width={150}
                height={190}
                isChecked={land.buildings[0] ? null : isCheckedPension}
                handleCheck={land.buildings[0] ? null : handleCheckPension}
              />
              <BuildingCard
                type='빌딩'
                price={land.currentBuildingPrices[1]}
                width={150}
                height={190}
                isChecked={land.buildings[1] ? null : isCheckedBuilding}
                handleCheck={land.buildings[1] ? null : handleCheckBuilding}
                leftTurn={player ? 1 - player.currentLap : 0}
              />
              <BuildingCard
                type='호텔'
                price={land.currentBuildingPrices[2]}
                width={150}
                height={190}
                isChecked={land.buildings[2] ? null : isCheckedHotel}
                handleCheck={land.buildings[2] ? null : handleCheckHotel}
                leftTurn={player ? 2 - player.currentLap : 0}
              />
            </div>

            {noMore && (
              <div className='flex flex-col w-full h-fit px-[30px]  relative'>
                <div className='flex flex-row justify-center bg-primary-200 w-full h-full py-[15px] items-center my-[20px] rounded-[20px]'>
                  <p className='text-text-100 font-bold text-[22px] mx-[20px]'>
                    더이상 지을 건물이 없습니다.
                  </p>
                </div>
              </div>
            )}
            {!noMore && (
              <div className='flex flex-col w-full h-fit px-[30px]  relative'>
                <div className='flex flex-row justify-center bg-primary-200 w-full h-full py-[15px] items-center my-[20px] rounded-[20px]'>
                  <p className='text-text-100 font-bold text-[22px]'>
                    건설 비용
                  </p>
                  <p className='text-text-100 font-bold text-[22px] mx-[20px]'>
                    →
                  </p>
                  <p className='text-text-100 font-bold text-[22px]'>
                    {addAmountUnit(totalPurchasePrice)}
                  </p>
                </div>
              </div>
            )}

            {noMore && (
              <motion.div
                className='w-full flex flex-row justify-center items-center mb-[40px]'
                whileHover={{ scale: 1.1 }}
                whileTap={{ scale: 0.9 }}
                transition={{ type: 'spring', stiffness: 400, damping: 17 }}
              >
                <CButton
                  type='green'
                  onClick={onClickPurchase}
                  width={150}
                  height={50}
                  rounded={20}
                >
                  <p className='text-[22px] font-black text-primary-100'>
                    구매하기
                  </p>
                </CButton>
              </motion.div>
            )}
          </motion.div>
        </div>
      )}
    </AnimatePresence>
  );
};

export default ConstructionModal;
