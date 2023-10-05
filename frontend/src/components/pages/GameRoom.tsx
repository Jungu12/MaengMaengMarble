import { images } from '@constants/images';
// import { moveCharacter } from '@utils/game';
import {
  calcRank,
  effectNewsToString,
  formatAsset,
  getPlayerIndex,
  moveCharacter,
} from '@utils/game';
import { motion, useAnimation } from 'framer-motion';
import { useEffect, useRef, useState, useCallback, useMemo } from 'react';
import * as StompJs from '@stomp/stompjs';
import { activateClient, getClient } from '@utils/socket';
import { useLocation, useParams } from 'react-router-dom';
import { ParticipantsType, WSResponseType } from '@/types/common/common.type';
import {
  DiceResultType,
  FullGameDataType,
  SlotType,
  TurnEndResponseType,
  TurnListType,
} from '@/types/gameRoom/game.type';
import { useRecoilState, useRecoilValue } from 'recoil';
import { userState } from '@atom/userAtom';
import { currentParticipantsNum } from '@utils/lobby';
// import LandInfoModal from '@components/gameRoom/LandInfoModal';
// import ConstructionModal from '@components/gameRoom/ConstructionModal';
// import LoansModal from '@components/gameRoom/LoansModal';
import CardChoice from '@components/gameRoom/CardChoice';
import GameMap from '@components/gameRoom/GameMap';
import Dice from 'react-dice-roll';
import ConstructionModal from '@components/gameRoom/ConstructionModal';
import {
  currentPlayerState,
  landListState,
  newsState,
  playersState,
} from '@atom/gameAtom';
import SlotMachineModal from '@components/gameRoom/SlotMachineModal';

const GameRoom = () => {
  const location = useLocation();
  const state = location.state as { userList: ParticipantsType[] };
  const user = useRecoilValue(userState);
  const client = useRef<StompJs.Client>();
  const gameSub = useRef<StompJs.StompSubscription>();
  const { gameId } = useParams();
  const [playerList, setPlayerList] = useRecoilState(playersState);
  const [currentPlayer, setCurrentPlayer] = useRecoilState(currentPlayerState);
  const [landList, setLandList] = useRecoilState(landListState);
  const [news, setNews] = useRecoilState(newsState);
  const [isGameStart, setIsGameStart] = useState(false);
  const [orderList, setOrderList] = useState<TurnListType[]>([]);
  const [isTurnEnd, setIsTurnEnd] = useState(false);
  const [isStopTrade, setIsStopTrade] = useState(false); // 거래 정지 칸에 위치하는지
  const [이동중, set이동중] = useState(false);

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
  const [isOpenSlot, setIsOpenSlot] = useState(false);
  // const slotResult = useMemo(() => [0, 0, 0], []);
  const nowPalyerIdx = useMemo(
    () => getPlayerIndex(playerList, currentPlayer),
    [currentPlayer, playerList]
  );
  const myTurn = useMemo(() => {
    if (currentPlayer === user?.nickname) {
      return true;
    }
    return false;
  }, [currentPlayer, user?.nickname]);

  // 주사위 던지기
  const handleDiceRoll = useCallback(() => {
    if (isDiceRollButtonClick) return;
    client.current?.publish({
      destination: `/pub/game-rooms/roll/${gameId}`,
    });
  }, [gameId, isDiceRollButtonClick]);

  // 거래정지칸 주사위 던지기
  const handleStopTradeDiceRoll = useCallback(() => {}, []);

  // 데이터 최신화
  const updateInfo = useCallback(
    (data: FullGameDataType | TurnEndResponseType) => {
      setPlayerList(data.players);
      setNews(data.info.effectNews);
      setLandList(data.lands);
      // TODO: 주식도 최신화 해야함
    },
    [setLandList, setNews, setPlayerList]
  );

  useEffect(() => {
    console.log(reDice);
  }, [reDice]);

  // 턴 종료
  const handleTurnEnd = useCallback(() => {
    client.current?.publish({
      destination: `/pub/game-rooms/end-turn/${gameId}`,
    });
  }, [gameId]);

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
          setCurrentPlayer(temp.data.info.currentPlayer);
          updateInfo(temp.data);
        }
        if (response.type === '박진호 끝') {
          const parkResult = response as WSResponseType<SlotType>;
          console.log('[박진호데이터]', parkResult);
          setPlayerList(parkResult.data.players);
        }
      });
    };

    return () => {
      subTemp.unsubscribe();
    };
  }, [
    gameId,
    setCurrentPlayer,
    setPlayerList,
    state.userList,
    updateInfo,
    user?.userId,
  ]);

  // 구독
  useEffect(() => {
    if (!client.current) return;
    if (client.current.connected) {
      gameSub.current = client.current.subscribe(
        `/sub/game-rooms/${gameId}`,
        (res) => {
          const response: WSResponseType<unknown> = JSON.parse(res.body);

          if (response.type === '주사위이동후로직') {
            const diceResult = response as WSResponseType<DiceResultType>;
            setIsDiceRollButtonClick(true);
            console.log('주사위 결과 나왔어요');
            setDice1(diceResult.data.dice1);
            setDice2(diceResult.data.dice2);
            const idx = getPlayerIndex(playerList, currentPlayer);
            setSeletedLandId(diceResult.data.players[idx]!.currentLocation);
            // 더블이 나오는 경우 주사위 다시 던지기
            if (doubleCnt < diceResult.data.doubleCount) {
              setReDice(true);
              setDoubleCnt(diceResult.data.doubleCount);
            }
          }

          // 거래 정지칸에 도착한 경우
          if (response.type === '이동후턴종료') {
            if (myTurn) {
              handleTurnEnd();
            }
          }

          if (response.type === '거래정지이동후로직') {
            const diceResult = response as WSResponseType<DiceResultType>;
            setDice1(diceResult.data.dice1);
            setDice2(diceResult.data.dice2);
            const idx = getPlayerIndex(playerList, currentPlayer);
            setSeletedLandId(diceResult.data.players[idx]!.currentLocation);
            if (myTurn) {
              setIsStopTrade(false);
            }
          }

          if (response.type === '거래정지턴종료') {
            handleTurnEnd();
          }

          // 더블이 3번 나온 경우
          if (response.type === '주사위턴종료') {
            if (myTurn) {
              console.log('거래정지요~');
              handleTurnEnd();
            }
          }

          if (response.type === '땅구매') {
            console.log('[플레이어 정보]', playerList);

            // 현재 플레이어만 보이게
            if (currentPlayer === user?.nickname) {
              console.log(seletedLandId);
              setIsOepnContrunction(true);
            }
          }

          if (response.type === '박진호') {
            console.log('박진호가 왔어요~~');

            // 현재 플레이어만 보이게
            if (currentPlayer === user?.nickname) {
              setIsOpenSlot(true);
            }
          }

          if (response.type === '자유') {
            const temp = response as WSResponseType<FullGameDataType>;
            updateInfo(temp.data);
            // 주사위에서 더블이 나온 경우
            if (myTurn) {
              if (reDice) {
                setIsDiceRoll(false);
                setIsDiceRollButtonClick(false);
              } else {
                setIsTurnEnd(true);
              }
            }

            console.log('자유시간~~~');
          }

          if (response.type === '턴종료끝') {
            const temp = response as WSResponseType<TurnEndResponseType>;
            const players = temp.data.players;
            const nextPlayerIndex = getPlayerIndex(
              players,
              temp.data.info.currentPlayer
            );
            const nextPlayerLocation =
              players[nextPlayerIndex]!.currentLocation;
            updateInfo(temp.data);
            setCurrentPlayer(temp.data.info.currentPlayer);
            setIsTurnEnd(false);
            // 다음 플레이의 땅 위치에 따라 다른 로직 수행
            // 1. 문단속 효과 발동중인 경우에 어디로든 문인 경우
            if (nextPlayerLocation === 24 && temp.data.info.doorCheck > 0) {
              console.log('문단속 중인 어디로든 문');
              // 서버에 랜덤 이동 위치 요청하기
            }
            // 2.어디로든 문인 경우
            else if (nextPlayerLocation === 24) {
              console.log('어디로든 문');
              // 이동 위치 선택 화면 보여주기
            }
            // 3. 거래정지칸인 경우
            else if (nextPlayerLocation === 8) {
              if (
                temp.data.info.currentPlayer === user?.nickname &&
                players[nextPlayerIndex]!.stopTradeCount > 3
              ) {
                setIsStopTrade(false);
              } else if (temp.data.info.currentPlayer === user?.nickname) {
                setIsStopTrade(true);
              }
              console.log('거래정지');
              setIsDiceRoll(false);
              setIsDiceRollButtonClick(false);
              setDoubleCnt(0);
              setReDice(false);
              // 거래정지칸 특별 주사위 던지기 요청
            }
            // 4. 나머지 경우 주사위 던지기
            else {
              console.log('나머지');
              setIsDiceRoll(false);
              setIsDiceRollButtonClick(false);
              setDoubleCnt(0);
              setReDice(false);
            }
          }

          console.log(JSON.parse(res.body));
        }
      );
    }

    return () => {
      gameSub.current?.unsubscribe();
    };
  }, [
    currentPlayer,
    doubleCnt,
    gameId,
    handleTurnEnd,
    myTurn,
    playerList,
    reDice,
    seletedLandId,
    setCurrentPlayer,
    updateInfo,
    user?.nickname,
  ]);

  useEffect(() => {
    if (이동중) return;
    if (isDiceRollButtonClick && !isDiceRoll) {
      const diceRef = document.querySelectorAll('._space3d');
      const clickEvent = new MouseEvent('click', {
        bubbles: true, // 이벤트가 버블링되도록 설정합니다.
        cancelable: true, // 이벤트가 취소 가능하도록 설정합니다.
        view: window, // 이벤트의 관련 뷰를 설정합니다.
      });
      set이동중(true);

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
                set이동중(false);
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
    이동중,
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

  // 건물 구매
  const handleConstruction = (purchase: boolean[]) => {
    console.log(purchase);

    client.current?.publish({
      destination: `/pub/game-rooms/purchase/${gameId}`,
      body: JSON.stringify(purchase),
    });
    setIsOepnContrunction(false);
  };
  return (
    <>
      <ConstructionModal
        isOpen={isOepnContrunction}
        handleConstruction={handleConstruction}
        handleClose={() => {
          setIsOepnContrunction(false);
        }}
        land={landList[seletedLandId]}
        player={playerList[nowPalyerIdx]}
      />
      <SlotMachineModal
        client={client.current}
        gameId={gameId}
        isOpen={isOpenSlot}
        handleSlot={() => {
          setIsOpenSlot(false);
        }}
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
            {myTurn && (
              <button
                className='button-3d'
                onClick={isStopTrade ? handleStopTradeDiceRoll : handleDiceRoll}
                disabled={isDiceRoll}
              >
                주사위 굴리기
              </button>
            )}
          </div>
        )}
        {/* 턴종료 버튼 */}
        <div className='absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 text-5xl text-white z-[10] text-[24px] font-bold flex flex-col justify-center items-center'>
          {myTurn && isTurnEnd && (
            <button className='button-3d' onClick={handleTurnEnd}>
              턴 종료
            </button>
          )}
        </div>
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
