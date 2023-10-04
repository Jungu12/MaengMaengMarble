import { AnimatePresence, motion } from 'framer-motion';
import { images } from '@constants/images';
import { useCallback, useMemo, useState } from 'react';
import CButton from '@components/common/CButton';
import useToastList from '@hooks/useToastList';
import { ToastMessageState } from '@atom/toastAtom';
import { useSetRecoilState } from 'recoil';
import { addAmountUnit } from '@utils/game';
import * as StompJs from '@stomp/stompjs';
import { WSResponseType } from '@/types/common/common.type';
import { SlotType } from '@/types/gameRoom/game.type';

type Props = {
  client: StompJs.Client;
  gameId: string;
  isOpen: boolean;
  handleSlot: () => void;
};

const SlotMachineModal = ({ client, gameId, isOpen, handleSlot }: Props) => {
  const indexes = useMemo(() => [0, 0, 0], []);
  // const [slotResult, setSlotResult] = useState([0, 0, 0]);
  const [bettingMoney, setBettingMoney] = useState(30000000);
  const [isClickBetting, setIsClickBetting] = useState(false);
  const { show } = useToastList();
  const setToastMessage = useSetRecoilState(ToastMessageState);

  const increaseMoney = useCallback(() => {
    if (bettingMoney === 30000000) {
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '최대 3000만원까지 베팅 가능',
        };
      });
      show('error');
    } else {
      setBettingMoney((prev) => prev + 5000000);
    }
  }, [bettingMoney, setToastMessage, show]);

  const decreaseMoney = useCallback(() => {
    if (bettingMoney === 5000000) {
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '최소 500만원부터 베팅 가능',
        };
      });
      show('error');
    } else {
      setBettingMoney((prev) => prev - 5000000);
    }
  }, [bettingMoney, setToastMessage, show]);

  const roll = useCallback(
    (slot: HTMLElement, offset: number = 0): Promise<number> => {
      const delta: number = (offset + 2) * 10 + offset;

      return new Promise<number>((resolve) => {
        const style = getComputedStyle(slot);
        const backgroundPositionY = parseFloat(
          style.getPropertyValue('background-position-y')
        );
        const targetBackgroundPositionY = backgroundPositionY + delta * 33;
        const normTargetBackgroundPositionY =
          targetBackgroundPositionY % (10 * 33);
        setTimeout(() => {
          slot.style.transition = `background-position-y ${
            (8 + 1 * delta) * 100
          }ms cubic-bezier(.41,-0.01,.63,1.09)`;
          slot.style.backgroundPositionY = `${
            backgroundPositionY + delta * 33
          }px`;
        }, offset * 150);

        setTimeout(
          () => {
            slot.style.transition = 'none';
            slot.style.backgroundPositionY = `${normTargetBackgroundPositionY}px`;
            resolve(delta % 10); // 배열로 값을 감싸서 반환
          },
          (8 + 1 * delta) * 100 + offset * 150
        );
      });
    },
    []
  );

  const rollSlot = useCallback(async () => {
    setIsClickBetting(true);

    client?.publish({
      destination: `/pub/game-rooms/parkjinho/${gameId}`,
      body: JSON.stringify({ bettingMoney: bettingMoney }),
    });

    client?.subscribe(`/sub/game-rooms/${gameId}`, async (res) => {
      const response: WSResponseType<unknown> = JSON.parse(res.body);
      if (response.type === '박진호 끝') {
        const parkResult = response as WSResponseType<SlotType>;
        console.log('[박진호데이터]', parkResult);
        // setSlotResult([
        //   parkResult.data.num[0],
        //   parkResult.data.num[1],
        //   parkResult.data.num[2],
        // ]);
        // slotResult[0] = parkResult.data.num[0];
        // slotResult[1] = parkResult.data.num[1];
        // slotResult[2] = parkResult.data.num[2];
        console.log('[박진호 배열]', parkResult.data.num);
        // console.log('[slotResult 결과]', slotResult);

        // 슬롯 시작
        const slotList = document.querySelectorAll(
          '.slot'
        ) as NodeListOf<HTMLElement>;
        await Promise.all<number>(
          [...slotList].map((slot, i) => roll(slot, parkResult.data.num[i]))
        ).then((deltas) => {
          deltas.forEach(
            (delta, i) => (indexes[i] = (indexes[i] + delta) % 10) // 배열의 첫 번째 값 사용
          );
          console.log(indexes);
          if (
            indexes[0] == indexes[1] ||
            indexes[1] == indexes[2] ||
            indexes[0] == indexes[2]
          ) {
            setToastMessage((prev) => {
              return {
                ...prev,
                success: '베팅금액의 2배 획득',
              };
            });
            show('success');
          }
          if (indexes[0] == indexes[1] && indexes[1] == indexes[2]) {
            setToastMessage((prev) => {
              return {
                ...prev,
                success: '베팅금액의 50배 획득',
              };
            });
            show('success');
          }
          if (
            indexes[0] == indexes[1] &&
            indexes[1] == indexes[2] &&
            indexes[0] == 7
          ) {
            setToastMessage((prev) => {
              return {
                ...prev,
                success: '777로 게임에서 승리하였습니다.',
              };
            });
            show('success');
          }
        });
        handleSlot();
        //
      }
    });
  }, [
    bettingMoney,
    client,
    gameId,
    handleSlot,
    indexes,
    roll,
    setToastMessage,
    show,
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
            className='flex flex-col items-center justify-between w-[469px] h-[650px] rounded-[20px] relative'
            style={{
              backgroundImage: `url(${images.gameRoom.slotmachine})`,
              backgroundSize: 'cover',
            }}
          >
            <button
              className='ml-auto mr-[20px] mt-[20px] w-[56px] h-[56px] cursor-pointer'
              onClick={handleSlot}
            >
              <img
                className='w-[50px] h-[50px]'
                src={images.waitingRoom.exit}
                alt='나가기'
              />
            </button>

            <div className='absolute left-[174px] top-[400px] z-10 flex flex-row justify-between w-[109px] h-[84px]'>
              <div
                className='slot w-[33px] h-[99px] rounded-[5px]'
                style={{
                  backgroundImage: `url(${images.gameRoom.sloticon})`,
                  backgroundSize: 'cover',
                  backgroundPosition: '0 0',
                  backgroundRepeat: 'repeat-y',
                  boxShadow: '0px 0px 5px 4px rgba(0, 0, 0, 0.25) inset',
                }}
              ></div>
              <div
                className='slot w-[33px] h-[99px] rounded-[5px]'
                style={{
                  backgroundImage: `url(${images.gameRoom.sloticon})`,
                  backgroundSize: 'cover',
                  backgroundPosition: '0 0',
                  backgroundRepeat: 'repeat-y',
                  boxShadow: '0px 0px 5px 4px rgba(0, 0, 0, 0.25) inset',
                }}
              ></div>
              <div
                className='slot w-[33px] h-[99px] rounded-[5px]'
                style={{
                  backgroundImage: `url(${images.gameRoom.sloticon})`,
                  backgroundSize: 'cover',
                  backgroundPosition: '0 0',
                  backgroundRepeat: 'repeat-y',
                  boxShadow: '0px 0px 5px 4px rgba(0, 0, 0, 0.25) inset',
                }}
              ></div>
            </div>

            <div className='flex flex-row w-full px-[80px] mb-[20px] items-center justify-between'>
              <div className='flex flex-row flex-1 items-center justify-between py-[8px] px-[30px] bg-primary-light100 rounded-[15px]'>
                <p className='text-[20px] font-extrabold text-text-100'>
                  {addAmountUnit(bettingMoney)}
                </p>
                <div className='flex flex-col'>
                  <button
                    disabled={isClickBetting ? true : false}
                    className='w-[15px] h-[15px]'
                    onClick={increaseMoney}
                  >
                    <img src={images.icon.up} alt='플러스 버튼' />
                  </button>
                  <button
                    disabled={isClickBetting ? true : false}
                    className='w-[15px] h-[15px]'
                    onClick={decreaseMoney}
                  >
                    <img src={images.icon.down} alt='마이너스 버튼' />
                  </button>
                </div>
              </div>
              <motion.div
                className='ml-[30px]'
                whileHover={isClickBetting ? {} : { scale: 1.1 }}
                whileTap={isClickBetting ? {} : { scale: 0.9 }}
                transition={
                  isClickBetting
                    ? {}
                    : { type: 'spring', stiffness: 400, damping: 17 }
                }
              >
                <CButton
                  type='green'
                  isDisable={isClickBetting ? true : false}
                  onClick={rollSlot}
                  width={90}
                  height={40}
                  rounded={10}
                >
                  <p className='text-[18px] font-black text-primary-100'>
                    베팅하기
                  </p>
                </CButton>
              </motion.div>
            </div>
          </motion.div>
        </div>
      )}
    </AnimatePresence>
  );
};

export default SlotMachineModal;
