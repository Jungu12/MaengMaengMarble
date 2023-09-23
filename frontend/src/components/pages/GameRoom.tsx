import MapArea from '@components/gameRoom/MapArea';
import { images } from '@constants/images';
import { motion, useAnimation } from 'framer-motion';
import { useEffect, useState, useCallback } from 'react';

const GameRoom = () => {
  const [x, setX] = useState(0);
  const [y, setY] = useState(0);
  const [position, setPosition] = useState(7);
  const controls = useAnimation();

  const moveCharacter = useCallback(
    async (_playNum: number, count: number, cur: number) => {
      const moveList = [];
      let curX = 0;
      let curY = 0;

      // 이동 배열 만들기
      for (let i = 0; i < count; i++) {
        if (cur === 32) cur = 0;
        // 왼쪽으로 이동
        if (cur >= 0 && cur < 8) {
          moveList.push(['left', curX - 67]);
          curX -= 67;
        }
        // 위쪽으로 이동
        if (cur >= 8 && cur < 16) {
          moveList.push(['up', curY - 67]);
          curY -= 67;
        }
        // 오른쪽으로 이동
        if (cur >= 16 && cur < 24) {
          moveList.push(['right', curX + 67]);
          curX += 67;
        }
        // 아래쪽으로 이동
        if (cur >= 24 && cur < 32) {
          moveList.push(['down', curY + 67]);
          curY += 67;
        }
        cur++;
      }

      // setPosition(cur);
      console.log(moveList);

      // 이동 시키기
      for (let idx = 0; idx < moveList.length; idx++) {
        const m = moveList[idx];
        if (m[0] === 'left' || m[0] === 'right') {
          await new Promise((resolve) => setTimeout(resolve, 200)); // 딜레이 시간 (i * 2ms)
          controls.start(
            {
              x: m[1],
              marginBottom: '0px',
            },
            { duration: 0.2 }
          );
        } else if (m[0] === 'up' || m[0] === 'down') {
          await new Promise((resolve) => setTimeout(resolve, 200)); // 딜레이 시간 (i * 400ms)
          controls.start(
            {
              y: m[1],
              marginBottom: '0px',
            },
            { duration: 0.2 }
          );
        }
      }
      return cur;
    },
    [controls]
  );

  useEffect(() => {
    const cur = moveCharacter(1, 32, position).then((res) => {
      console.log(res);
      moveCharacter(1, 32, res);
    });
    // 이동이 끝나면 position 저장
    // setPosition(cur);
    console.log('이동 끝', cur);
    // setPosition(cur);
  }, [moveCharacter, position]);

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
    setPosition(7);
  }, []);

  return (
    <div
      className='flex flex-col w-full h-full min-h-[700px] overflow-hidden relative'
      style={{
        backgroundImage: `url(${images.gameRoom.background})`,
        backgroundSize: 'cover',
      }}
    >
      {/* 게임맵 */}
      <div className='absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 mt-[40px] w-[640px] h-[640px] ] bg-slate-500'>
        {/* 필드 타일 */}
        {/* 아랫줄 */}
        <div className='absolute bottom-0 flex justify-between w-full'>
          <MapArea
            src={images.map.transactionSuspended}
            alt='거래정지'
            value={8}
            width={90}
            // height={90}
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
