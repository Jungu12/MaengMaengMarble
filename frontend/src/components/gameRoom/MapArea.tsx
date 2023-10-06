import { landListState } from '@atom/gameAtom';
import { images } from '@constants/images';
import { sortStyle } from '@utils/game';
import { AnimatePresence, motion } from 'framer-motion';
import { useCallback, useMemo } from 'react';
import { useRecoilValue } from 'recoil';

type Props = {
  height?: number;
  width?: number;
  src: string;
  alt: string;
  value: number;
  onClickArea?: (landId: number) => void;
};

const MapArea = ({ height, width, src, alt, value, onClickArea }: Props) => {
  const landList = useRecoilValue(landListState);
  const styleType = useMemo(() => {
    const type = sortStyle(value);
    if (type === 0) return '';
    if (type === 1) return 'top-[-20px] left-0';
    if (type === 2) return 'bottom-[16px] right-[-20px] rotate-90';
    if (type === 3) return 'bottom-[-20px] left-0 scale-y-[-1]';
    return 'bottom-[16px] left-[-20px] rotate-[-90deg]';
  }, [value]);
  const currentLand = useMemo(() => landList[value], [landList, value]);
  const ground = useMemo(() => {
    const landOwner = landList[value].owner;
    if (landOwner === -1) return '';
    if (landOwner === 0) return images.building.redground;
    if (landOwner === 1) return images.building.blueground;
    if (landOwner === 2) return images.building.greenground;
    return images.building.purpleground;
  }, [landList, value]);
  const pension = useMemo(() => {
    const landOwner = landList[value].owner;
    if (landOwner === -1) return '';
    if (landOwner === 0) return images.building.redpension;
    if (landOwner === 1) return images.building.bluepension;
    if (landOwner === 2) return images.building.greenpension;
    return images.building.purplepension;
  }, [landList, value]);
  const building = useMemo(() => {
    const landOwner = landList[value].owner;
    if (landOwner === -1) return '';
    if (landOwner === 0) return images.building.redbuilding;
    if (landOwner === 1) return images.building.bluebuilding;
    if (landOwner === 2) return images.building.greenbuilding;
    return images.building.purplebuilding;
  }, [landList, value]);
  const hotel = useMemo(() => {
    const landOwner = landList[value].owner;
    if (landOwner === -1) return '';
    if (landOwner === 0) return images.building.redhotel;
    if (landOwner === 1) return images.building.bluehotel;
    if (landOwner === 2) return images.building.greenhotel;
    return images.building.purplehotel;
  }, [landList, value]);
  const onlyGround = useMemo(() => {
    let buildingExist = false;
    currentLand.buildings.forEach((building) => {
      if (building) buildingExist = true;
    });

    if (buildingExist) return false;
    if (currentLand.owner === -1) return false;

    return true;
  }, [currentLand.buildings, currentLand.owner]);

  const onClick = useCallback(() => {
    onClickArea ? onClickArea(value) : {};
    console.log(value);
  }, [onClickArea, value]);

  return (
    <button onClick={onClick} className='relative'>
      <img
        src={src}
        alt={alt}
        style={{
          width: `${width}px`,
          height: `${height}px`,
        }}
      />
      {styleType && (
        <motion.div className={`absolute ${styleType} flex `}>
          {onlyGround ? (
            <AnimatePresence>
              <motion.img
                className='h-[32px]'
                src={ground}
                alt='땅'
                initial={{ scale: 10.0 }}
                animate={{ scale: 1.0 }}
                exit={{ opacity: 0 }}
                transition={{
                  scale: { duration: 1 },
                  opacity: { duration: 0.5 },
                }}
              />
            </AnimatePresence>
          ) : (
            <>
              <AnimatePresence>
                {currentLand.buildings[0] && (
                  <motion.img
                    className='h-[32px] ml-[2px]'
                    src={pension}
                    alt='펜션'
                    initial={{ scale: 10.0 }}
                    animate={{ scale: 1.0 }}
                    exit={{ opacity: 0 }}
                    transition={{
                      scale: { duration: 1 },
                      opacity: { duration: 0.5 },
                    }}
                  />
                )}
              </AnimatePresence>
              <AnimatePresence>
                {currentLand.buildings[1] && (
                  <motion.img
                    className='h-[32px] ml-[-8px]'
                    src={building}
                    alt='빌딩'
                    initial={{ scale: 10.0 }}
                    animate={{ scale: 1.0 }}
                    exit={{ opacity: 0 }}
                    transition={{
                      scale: { duration: 1 },
                      opacity: { duration: 0.5 },
                    }}
                  />
                )}
              </AnimatePresence>
              <AnimatePresence>
                {currentLand.buildings[2] && (
                  <motion.img
                    className='h-[32px] ml-[-8px]'
                    src={hotel}
                    alt='호텔'
                    initial={{ scale: 10.0 }}
                    animate={{ scale: 1.0 }}
                    exit={{ opacity: 0 }}
                    transition={{
                      scale: { duration: 1 },
                      opacity: { duration: 0.5 },
                    }}
                  />
                )}
              </AnimatePresence>
            </>
          )}
        </motion.div>
      )}
    </button>
  );
};

export default MapArea;
