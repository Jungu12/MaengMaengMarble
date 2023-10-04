import { images } from '@constants/images';
import {
  calcRank,
  effectNewsToString,
  formatAsset,
  getPlayerIndex,
  moveCharacter,
} from '@utils/game';
import { motion, useAnimation } from 'framer-motion';
import { useEffect, useRef, useState, useCallback } from 'react';
import * as StompJs from '@stomp/stompjs';
import { activateClient, getClient } from '@utils/socket';
import { useLocation, useParams } from 'react-router-dom';
import { ParticipantsType, WSResponseType } from '@/types/common/common.type';
import {
  DiceResultType,
  FullGameDataType,
  LandType,
  NewsType,
  PlayerType,
  TurnListType,
} from '@/types/gameRoom/game.type';
import { useRecoilValue } from 'recoil';
import { userState } from '@atom/userAtom';
import { currentParticipantsNum } from '@utils/lobby';
import CardChoice from '@components/gameRoom/CardChoice';
import GameMap from '@components/gameRoom/GameMap';
import Dice from 'react-dice-roll';
import ConstructionModal from '@components/gameRoom/ConstructionModal';

const GameRoom = () => {
  const location = useLocation();
  const state = location.state as { userList: ParticipantsType[] };
  const user = useRecoilValue(userState);
  const client = useRef<StompJs.Client>();
  const gameSub = useRef<StompJs.StompSubscription>();
  const { gameId } = useParams();
  const [isGameStart, setIsGameStart] = useState(false);
  const [orderList, setOrderList] = useState<TurnListType[]>([]);
  const [playerList, setPlayerList] = useState<(PlayerType | null)[]>([]);
  const [landList, setLandList] = useState<LandType[]>([]);
  const [news, setNews] = useState<NewsType[]>([]);
  const [currentPlayer, setCurrentPlayer] = useState('');
  const [dice1, setDice1] = useState<1 | 2 | 3 | 4 | 5 | 6>(1);
  const [dice2, setDice2] = useState<1 | 2 | 3 | 4 | 5 | 6>(1);
  const [isDiceRollButtonClick, setIsDiceRollButtonClick] = useState(false);
  const [isDiceRoll, setIsDiceRoll] = useState(false);
  const [doubleCnt, setDoubleCnt] = useState(0); // 현재 더블이 몇번 나왔는지 확인용도
  const [reDice, setReDice] = useState(false); // 주사위를 더 던질 수 있는지 확인
  const controls1 = useAnimation();
  const controls2 = useAnimation();
  const controls3 = useAnimation();
  const controls4 = useAnimation();
  const [seletedLandId, setSeletedLandId] = useState(0);
  const [isOepnContrunction, setIsOepnContrunction] = useState(false);

  // useEffect(() => {
  //   const cur = moveCharacter(1, 32, position, controls).then((res) => {
  //     console.log(res);
  //     moveCharacter(1, 32, res, controls);
  //   });
  //   // 이동이 끝나면 position 저장
  //   // setPosition(cur);
  //   console.log('이동 끝', cur);
  //   // setPosition(cur);
  // }, [controls, position]);

  // useEffect(() => {
  //   // console.log('현위치 : ', position);

  //   if (position >= 0 && position < 8) {
  //     setX(position * -67);
  //     setY(0);
  //   }
  //   if (position >= 8 && position < 16) {
  //     setX(-67 * 8);
  //     setY((position - 8) * 67);
  //   }
  //   if (position >= 16 && position < 24) {
  //     setX((24 - position) * -67);
  //     setY(67 * 8);
  //   }
  //   if (position >= 24 && position < 32) {
  //     setX(0);
  //     setY((32 - position) * 67);
  //   }
  // }, [position]);

  // 임시 코드 (지워야함)
  // useEffect(() => {
  //   setIsGameStart(false);
  //   setPosition(7);
  // }, []);

  // 주사위 던지기
  const handleDiceRoll = useCallback(() => {
    if (isDiceRollButtonClick) return;
    client.current?.publish({
      destination: `/pub/game-rooms/roll/${gameId}`,
    });
  }, [gameId, isDiceRollButtonClick]);

  useEffect(() => {
    console.log(reDice);
  }, [reDice]);

  // 소켓 연결
  useEffect(() => {
    let subTemp: StompJs.StompSubscription;
    client.current = getClient();
    activateClient(client.current);
    client.current.onConnect = () => {
      // 방장인 경우 게임 시작 알리기
      if (!client.current) return;
      if (user?.userId === state.userList[0].userId) {
        client.current.publish({
          destination: `/pub/game-rooms/start/${gameId}`,
          body: JSON.stringify({
            cnt: currentParticipantsNum(state.userList).toString(),
          }),
        });
      }

      subTemp = client.current.subscribe(`/sub/game-rooms/${gameId}`, (res) => {
        const response: WSResponseType<unknown> = JSON.parse(res.body);
        if (response.type === '플레이순서') {
          const newOrderList = response as WSResponseType<{
            cards: TurnListType[];
          }>;
          console.log('[테스트]', newOrderList);
          setOrderList(newOrderList.data.cards);
        }
        if (response.type === '초기게임정보') {
          // 유저 배치 등 초기 세팅하기
          setIsGameStart(true);
          const temp = response as WSResponseType<FullGameDataType>;
          console.log('[게임시작데이터]', temp);
          setPlayerList(temp.data.players);
          setNews(temp.data.info.effectNews);
          setCurrentPlayer(temp.data.info.currentPlayer);
          setLandList(temp.data.lands);
        }
      });
    };

    return () => {
      subTemp.unsubscribe();
    };
  }, [gameId, state.userList, user?.userId]);

  // 구독
  useEffect(() => {
    if (!client.current) return;
    if (client.current.connected) {
      gameSub.current = client.current.subscribe(
        `/sub/game-rooms/${gameId}`,
        (res) => {
          const response: WSResponseType<unknown> = JSON.parse(res.body);

          if (
            response.type === '주사위' ||
            response.type === '거래정지칸도착'
          ) {
            const diceResult = response as WSResponseType<DiceResultType>;
            setIsDiceRollButtonClick(true);
            console.log('주사위 결과 나왔어요');
            setDice1(diceResult.data.dice1);
            setDice2(diceResult.data.dice2);
            const idx = getPlayerIndex(playerList, currentPlayer);
            setSeletedLandId(playerList[idx]!.currentLocation);
            // 더블이 나오는 경우 주사위 다시 던지기
            if (doubleCnt < diceResult.data.doubleCount) {
              setReDice(true);
              setDoubleCnt(diceResult.data.doubleCount);
            }
          }

          if (response.type === '땅구매') {
            console.log('맹맹맴맹맹');
          }
          console.log(JSON.parse(res.body));
        }
      );
    }

    return () => {
      gameSub.current?.unsubscribe();
    };
  }, [currentPlayer, doubleCnt, gameId, playerList]);

  useEffect(() => {
    if (isDiceRollButtonClick && !isDiceRoll) {
      const diceRef = document.querySelectorAll('._space3d');
      const clickEvent = new MouseEvent('click', {
        bubbles: true, // 이벤트가 버블링되도록 설정합니다.
        cancelable: true, // 이벤트가 취소 가능하도록 설정합니다.
        view: window, // 이벤트의 관련 뷰를 설정합니다.
      });

      console.log(diceRef);

      diceRef[0].dispatchEvent(clickEvent);
      setTimeout(() => {
        diceRef[1].dispatchEvent(clickEvent);
      }, 50);

      setTimeout(() => {
        setIsDiceRoll(true);
        const idx = getPlayerIndex(playerList, currentPlayer);
        // 캐릭터 이동 시작
        if (user) {
          let tempControls = controls1;
          if (idx === 1) {
            tempControls = controls2;
          }
          if (idx === 2) {
            tempControls = controls3;
          }
          if (idx === 3) {
            tempControls = controls4;
          }
          if (playerList[idx] !== null) {
            moveCharacter(
              dice1 + dice2,
              playerList[idx]!.currentLocation,
              tempControls
            ).then(
              // 도착 위치 호출
              () => {
                client.current?.publish({
                  destination: `/pub/game-rooms/after-move/${gameId}`,
                });
              }
            );
          }
        }
      }, 3000);
    }
  }, [
    controls1,
    controls2,
    controls3,
    controls4,
    currentPlayer,
    dice1,
    dice2,
    gameId,
    isDiceRoll,
    isDiceRollButtonClick,
    playerList,
    user,
  ]);

  if (!gameId || !client.current) return;

  if (!isGameStart) {
    return (
      <CardChoice
        gameId={gameId}
        client={client.current}
        orderList={orderList}
      />
    );
  }

  return (
    <>
      <ConstructionModal
        isOpen={isOepnContrunction}
        handleConstruction={() => {
          setIsOepnContrunction(false);
        }}
        land={landList[seletedLandId]}
      />
      <div
        className='flex flex-col w-full h-full min-h-[700px] overflow-hidden relative'
        style={{
          backgroundImage: `url(${images.gameRoom.background})`,
          backgroundSize: 'cover',
        }}
      >
        {/* 주사위 버튼*/}
        {!isDiceRoll && (
          <div className='absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 text-5xl text-white z-[10] text-[24px] font-bold flex flex-col justify-center items-center'>
            <div
              className='flex gap-[24px] mb-[80px]'
              style={{ pointerEvents: 'none' }}
            >
              <Dice
                cheatValue={dice1}
                size={100}
                faceBg='none'
                onRoll={(value) => console.log(value)}
              />
              <Dice
                cheatValue={dice2}
                size={100}
                faceBg='none'
                onRoll={(value) => console.log(value)}
              />
            </div>
            {currentPlayer === user?.nickname && (
              <button
                className='button-3d'
                onClick={() => {
                  handleDiceRoll();
                }}
                disabled={isDiceRoll}
              >
                주사위 굴리기
              </button>
            )}
          </div>
        )}
        {/* 유저 정보 */}
        <div className='flex flex-col w-full h-full relative'>
          <div className='flex justify-between'>
            {playerList[0] && (
              <div className='flex w-[360px] h-[160px] relative'>
                <div className='w-[120px] flex items-center justify-center overflow-hidden relative'>
                  <img
                    className='object-cover mt-[80px] z-[1]'
                    src={playerList[0].avatarImage}
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
                    {playerList[0].nickname}
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
                      {`${calcRank(playerList, 0)}등`}
                    </p>
                  </div>
                  <div className='flex text-white mt-[24px] pl-[12px] overflow-hidden relative'>
                    <span className='text-xl font-semibold'>총자산</span>
                    <div className='flex-1 text-[#7492FF] text-[18px] font-medium ml-[12px] whitespace-nowrap'>
                      <span>{`${formatAsset(playerList[0].asset)}`}</span>
                    </div>
                  </div>
                  <div className='text-white pl-[12px] mt-[16px] overflow-hidden relative'>
                    <span className='text-xl font-semibold'>보유현금</span>
                    <span className='text-[#FFF59D] text-[18px] font-medium ml-[12px] whitespace-nowrap'>
                      {`${formatAsset(playerList[0].money)}`}
                    </span>
                  </div>
                </div>
                {/* 보유 카드 정보 */}
                <div className='flex absolute bottom-[-36px] left-[26px]'>
                  {playerList[0].cards[0] && (
                    <img
                      className='w-[32px] h-[32px]'
                      src={images.gameRoom.angel}
                      alt='천사카드'
                    />
                  )}
                  {playerList[0].cards[1] && (
                    <img
                      className='w-[32px] h-[32px]'
                      src={images.gameRoom.mediaControl}
                      alt='언론통제카드'
                    />
                  )}
                </div>
              </div>
            )}
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
                  {news.length === 0
                    ? '현재 적용중인 뉴스가 없습니다.'
                    : effectNewsToString(news)}
                </motion.div>
              </div>
            </div>
            {playerList[1] && (
              <div className='flex w-[360px] h-[160px] relative'>
                <div className='flex flex-col w-[240px]'>
                  <div
                    className='text-white text-xl font-bold mt-[8px] pb-[8px] pr-[12px] text-right relative'
                    style={{
                      /* 테두리 스타일 설정 */
                      borderBottom: '3px solid transparent',
                      borderImage:
                        'linear-gradient(to left, #055872, transparent)',
                      borderImageSlice: '1',
                      borderImageWidth: '0 0 2px 0',
                      borderImageOutset: '0',
                      borderImageRepeat: 'stretch',
                    }}
                  >
                    {playerList[1].nickname}
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
                      {`${calcRank(playerList, 1)}등`}
                    </p>
                  </div>
                  <div className='flex text-white mt-[24px] pr-[12px] overflow-hidden relative'>
                    <span className='text-xl font-semibold mr-[12px]'>
                      총자산
                    </span>
                    <div className='flex-1 text-[#7492FF] text-[18px] font-medium whitespace-nowrap'>
                      <span>{formatAsset(playerList[1].asset)}</span>
                    </div>
                  </div>
                  <div className='flex text-white pr-[12px] mt-[16px] overflow-hidden relative'>
                    <span className='text-xl font-semibold mr-[12px]'>
                      보유현금
                    </span>
                    <span className='text-[#FFF59D] text-[18px] font-medium whitespace-nowrap text-right'>
                      {formatAsset(playerList[1].money)}
                    </span>
                  </div>
                </div>
                <div className='w-[120px] flex items-center justify-center overflow-hidden relative'>
                  <img
                    className='object-cover mt-[80px] z-[1]'
                    src={playerList[1].avatarImage}
                    alt='player4'
                  />
                  <img
                    className='w-full h-full absolute'
                    src={images.gameRoom.profileBgBlue}
                    alt='배경이미지'
                  />
                </div>
                {/* 보유 카드 정보 */}
                <div className='flex absolute bottom-[-36px] right-[26px]'>
                  {playerList[1].cards[0] && (
                    <img
                      className='w-[32px] h-[32px]'
                      src={images.gameRoom.angel}
                      alt='천사카드'
                    />
                  )}
                  {playerList[1].cards[1] && (
                    <img
                      className='w-[32px] h-[32px]'
                      src={images.gameRoom.mediaControl}
                      alt='언론통제카드'
                    />
                  )}
                </div>
              </div>
            )}
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
              <div className='w-full h-[56px] bg-white rounded-[8px] opacity-[0.9] flex items-center'>
                <p className='text-[18px] font-semibold mx-[12px]'>LG 화학</p>
                <p className='text-[16px] font-medium'>3억 12만원</p>
                <div className='flex flex-col ml-auto justify-center text-[#2F50FF] text-[14px] font-medium mr-[12px]'>
                  <p className='ml-auto'>-1억 215만원</p>
                  <p className='ml-auto'>-45%</p>
                </div>
              </div>
            </div>
          </div>
          <div className='flex justify-between mt-auto'>
            {playerList[2] && (
              <div className='flex w-[360px] h-[160px] relative'>
                <div className='w-[120px] flex items-center justify-center overflow-hidden relative'>
                  <img
                    className='object-cover mt-[80px] z-[1]'
                    src={playerList[2].avatarImage}
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
                    {playerList[2].nickname}
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
                      {`${calcRank(playerList, 2)}등`}
                    </p>
                  </div>
                  <div className='flex text-white mt-[24px] pl-[12px] overflow-hidden relative'>
                    <span className='text-xl font-semibold'>총자산</span>
                    <div className='flex-1 text-[#7492FF] text-[18px] font-medium ml-[12px] whitespace-nowrap'>
                      <span>{formatAsset(playerList[2].asset)}</span>
                    </div>
                  </div>
                  <div className='text-white pl-[12px] mt-[16px] overflow-hidden relative'>
                    <span className='text-xl font-semibold'>보유현금</span>
                    <span className='text-[#FFF59D] text-[18px] font-medium ml-[12px] whitespace-nowrap'>
                      {formatAsset(playerList[2].money)}
                    </span>
                  </div>
                </div>
                {/* 보유 카드 정보 */}
                <div className='flex absolute top-[-32px] left-[26px] '>
                  {playerList[2].cards[0] && (
                    <img
                      className='w-[32px] h-[32px]'
                      src={images.gameRoom.angel}
                      alt='천사카드'
                    />
                  )}
                  {playerList[2].cards[1] && (
                    <img
                      className='w-[32px] h-[32px]'
                      src={images.gameRoom.mediaControl}
                      alt='언론통제카드'
                    />
                  )}
                </div>
              </div>
            )}

            {playerList[3] && (
              <div className='flex w-[360px] h-[160px] relative'>
                <div className='flex flex-col w-[240px]'>
                  <div
                    className='text-white text-xl font-bold mt-[8px] pb-[8px] pr-[12px] text-right relative'
                    style={{
                      /* 테두리 스타일 설정 */
                      borderBottom: '3px solid transparent',
                      borderImage:
                        'linear-gradient(to left, #C7B0E3, transparent)',
                      borderImageSlice: '1',
                      borderImageWidth: '0 0 2px 0',
                      borderImageOutset: '0',
                      borderImageRepeat: 'stretch',
                    }}
                  >
                    {playerList[3].nickname}
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
                      {`${calcRank(playerList, 3)}등`}
                    </p>
                  </div>
                  <div className='flex text-white mt-[24px] pr-[12px] overflow-hidden relative'>
                    <span className='text-xl font-semibold mr-[12px]'>
                      총자산
                    </span>
                    <div className='flex-1 text-[#7492FF] text-[18px] font-medium whitespace-nowrap'>
                      <span>{formatAsset(playerList[3].asset)}</span>
                    </div>
                  </div>
                  <div className='flex text-white pr-[12px] mt-[16px] overflow-hidden relative'>
                    <span className='text-xl font-semibold mr-[12px]'>
                      보유현금
                    </span>
                    <span className='text-[#FFF59D] text-[18px] font-medium whitespace-nowrap text-right'>
                      {formatAsset(playerList[3].money)}
                    </span>
                  </div>
                </div>
                <div className='w-[120px] flex items-center justify-center overflow-hidden relative'>
                  <img
                    className='object-cover mt-[80px] z-[1]'
                    src={playerList[3].avatarImage}
                    alt='player4'
                  />
                  <img
                    className='w-full h-full absolute'
                    src={images.gameRoom.profileBgPurple}
                    alt='배경이미지'
                  />
                </div>
                {/* 보유 카드 정보 */}
                <div className='flex absolute top-[-32px] right-[26px]'>
                  {playerList[3].cards[0] && (
                    <img
                      className='w-[32px] h-[32px]'
                      src={images.gameRoom.angel}
                      alt='천사카드'
                    />
                  )}
                  {playerList[3].cards[1] && (
                    <img
                      className='w-[32px] h-[32px]'
                      src={images.gameRoom.mediaControl}
                      alt='언론통제카드'
                    />
                  )}
                </div>
              </div>
            )}
          </div>
        </div>
        {/* 게임맵 */}
        <GameMap
          playerList={playerList}
          onClickLand={() => console.log(1)}
          controls1={controls1}
          controls2={controls2}
          controls3={controls3}
          controls4={controls4}
        />
      </div>
    </>
  );
};

export default GameRoom;
