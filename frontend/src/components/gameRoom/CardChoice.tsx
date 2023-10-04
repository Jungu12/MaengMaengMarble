import { TurnListType } from '@/types/gameRoom/game.type';
import { images } from '@constants/images';
import { AnimatePresence, motion } from 'framer-motion';
import * as StompJs from '@stomp/stompjs';
import { useState } from 'react';
import { useRecoilValue } from 'recoil';
import { userState } from '@atom/userAtom';

type Props = {
  gameId: string;
  client: StompJs.Client;
  orderList: TurnListType[];
};

const CardChoice = ({ gameId, orderList, client }: Props) => {
  const user = useRecoilValue(userState);
  const [cardChoice, setCardChoice] = useState(false);

  const flipCard = (index: number) => {
    const updatedOrderList = [...orderList];
    if (updatedOrderList[index].selected || cardChoice) return;
    client.publish({
      destination: `/pub/game-rooms/set-player/${gameId}`,
      body: JSON.stringify({
        userId: user?.userId,
        nickname: user?.nickname,
        characterId: user?.avatarId,
        playerCnt: index,
      }),
    });
    // updatedOrderList[index].selected = !updatedOrderList[index].selected;
    setCardChoice(true);
    // setOrderList(updatedOrderList);
  };

  return (
    <div
      className='flex flex-col w-full h-full min-h-[700px] overflow-hidden relative items-center'
      style={{
        backgroundImage: `url(${images.gameRoom.background})`,
        backgroundSize: 'cover',
      }}
    >
      <div className='text-[white] text-3xl mt-[120px]'>
        카드를 선택해주세요.
      </div>
      <div className='flex w-full items-center justify-around mb-auto mt-[100px]'>
        <AnimatePresence>
          {orderList &&
            orderList.map((item) => (
              <motion.button
                key={item.seq}
                initial={{ opacity: 0, rotateY: 0 }}
                animate={{ opacity: 1, rotateY: item.selected ? 0 : 180 }}
                exit={{ opacity: 0, rotateY: 0 }}
                onClick={() => flipCard(item.seq)}
              >
                <motion.div
                  className={`h-[340px] w-[220px] rounded-[8px] flex justify-center items-center ${
                    item.selected ? 'bg-[#e6e6e6] text-4xl font-bold' : ''
                  }`}
                  style={{
                    backgroundImage: item.selected
                      ? 'none'
                      : `url(${images.gameRoom.cardBack})`,
                    backgroundSize: 'cover',
                    filter: 'drop-shadow(5px 5px 5px #000)',
                  }}
                  whileHover={{ scale: item.selected ? 1.0 : 1.1 }}
                >
                  {item.selected ? `${item.seq} 등` : ''}
                </motion.div>
              </motion.button>
            ))}
        </AnimatePresence>
      </div>
    </div>
  );
};

export default CardChoice;
