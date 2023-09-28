import MapArea from '@components/gameRoom/MapArea';
import { images } from '@constants/images';
import { moveCharacter } from '@utils/game';
import { AnimatePresence, motion, useAnimation } from 'framer-motion';
import { useEffect, useRef, useState } from 'react';
import * as StompJs from '@stomp/stompjs';
import { activateClient, getClient } from '@utils/socket';
import { useLocation, useParams } from 'react-router-dom';
import { ParticipantsType, WSResponseType } from '@/types/common/common.type';
import { TurnListType } from '@/types/gameRoom/game.type';
import { useRecoilValue } from 'recoil';
import { userState } from '@atom/userAtom';
import { currentParticipantsNum } from '@utils/lobby';

const GameRoom = () => {
  const location = useLocation();
  const state = location.state as { userList: ParticipantsType[] };
  const user = useRecoilValue(userState);
  const client = useRef<StompJs.Client>();
  const gameSub = useRef<StompJs.StompSubscription>();
  const { gameId } = useParams();
  const [isGameStart, setIsGameStart] = useState(false);
  const [cardChoice, setCardChoice] = useState(false);
  const [orderList, setOrderList] = useState<TurnListType[]>([]);
  const [x, setX] = useState(0);
  const [y, setY] = useState(0);
  const [position, setPosition] = useState(7);
  const controls = useAnimation();

  const flipCard = (index: number) => {
    const updatedOrderList = [...orderList];
    if (updatedOrderList[index].selected || cardChoice) return;
    client.current?.publish({
      destination: `/pub/game-rooms/set-player/${gameId}`,
      body: JSON.stringify({
        userId: user?.userId,
        nickname: user?.nickname,
        characterId: user?.avatarId,
        playerCnt: index,
      }),
    });
    updatedOrderList[index].selected = !updatedOrderList[index].selected;
    setCardChoice(true);
    setOrderList(updatedOrderList);
  };

  useEffect(() => {
    const cur = moveCharacter(1, 32, position, controls).then((res) => {
      console.log(res);
      moveCharacter(1, 32, res, controls);
    });
    // 이동이 끝나면 position 저장
    // setPosition(cur);
    console.log('이동 끝', cur);
    // setPosition(cur);
  }, [controls, position]);

  useEffect(() => {
    console.log('x :', x);
  }, [x]);

  useEffect(() => {
    // console.log('현위치 : ', position);

    if (position >= 0 && position < 8) {
      setX(position * -67);
      setY(0);
    }
    if (position >= 8 && position < 16) {
      setX(-67 * 8);
      setY((position - 8) * 67);
    }
    if (position >= 16 && position < 24) {
      setX((24 - position) * -67);
      setY(67 * 8);
    }
    if (position >= 24 && position < 32) {
      setX(0);
      setY((32 - position) * 67);
    }
  }, [position]);

  // 임시 코드 (지워야함)
  useEffect(() => {
    setIsGameStart(false);
    setPosition(7);
  }, []);

  // 소켓 연결
  useEffect(() => {
    client.current = getClient();
    activateClient(client.current);
    client.current.onConnect = () => {
      gameSub.current = client.current?.subscribe(
        `/sub/game-rooms/${gameId}`,
        (res) => {
          const response: WSResponseType<unknown> = JSON.parse(res.body);
          if (response.type === '플레이순서') {
            const newOrderList = response as WSResponseType<{
              cards: TurnListType[];
            }>;
            console.log('[테스트]', newOrderList);
            setOrderList(newOrderList.data.cards);
          }
          console.log(JSON.parse(res.body));
        }
      );
      console.log('[참가 인원]', currentParticipantsNum(state.userList));

      // 방장인 경우 게임 시작 알리기
      if (user?.userId === state.userList[0].userId) {
        client.current?.publish({
          destination: `/pub/game-rooms/start/${gameId}`,
          body: JSON.stringify({
            cnt: currentParticipantsNum(state.userList).toString(),
          }),
        });
      }
    };
  }, [gameId, state.userList, user?.userId]);

  if (!isGameStart) {
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
              orderList.map((item, index) => (
                <motion.button
                  key={item.seq}
                  initial={{ opacity: 0, rotateY: 0 }}
                  animate={{ opacity: 1, rotateY: item.selected ? 0 : 180 }}
                  exit={{ opacity: 0, rotateY: 0 }}
                  onClick={() => flipCard(index)}
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
  }

  return (
    <div
      className='flex flex-col w-full h-full min-h-[700px] overflow-hidden relative'
      style={{
        backgroundImage: `url(${images.gameRoom.background})`,
        backgroundSize: 'cover',
      }}
    >
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
            bottom: `${30 + y}px`,
            right: `${20 - x}px`,
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

export default GameRoom;
