import { NewsType, PlayerType } from '@/types/gameRoom/game.type';
import { AnimationControls } from 'framer-motion';

/**
 * 캐릭터를 이동시킨다.
 * @param _playNum - 이동시킬 플레이어의 번호
 * @param count - 이동 시킬 횟수
 * @param cur - 현재 플레이어의 위치
 * @param controls - 캐릭터 이동 애니메이션
 * @returns 이동 후 캐릭터의 위치
 */
export const moveCharacter = async (
  _playNum: number,
  count: number,
  cur: number,
  controls: AnimationControls
) => {
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
};

/**
 * 플레이어의 총자산 순위를 계산한다.
 * @param players - 게임진행중인 플레이어들
 * @param index - 현재 플레이어의 번호
 * @returns 플레이어의 총 자산 순위
 */
export const calcRank = (players: (PlayerType | null)[], index: number) => {
  let rank = 1;
  const currentPlayer = players[index];

  if (currentPlayer === null || currentPlayer === undefined) {
    return -1;
  }

  for (let i = 0; i < players.length; i++) {
    const otherPlayer = players[i];

    if (otherPlayer === null || otherPlayer === undefined || i === index) {
      // 다른 플레이어가 null, undefined이거나 현재 플레이어와 같은 경우 건너뜁니다.
      continue;
    }

    if (otherPlayer.asset > currentPlayer.asset) {
      rank++;
    }
  }

  return rank;
};

/**
 * 현재 돈을 받아서 ..억 ..만원 단위로 변환시켜준다.
 * @param asset - 자산
 * @returns 억 만원 단위로 변환한 문자열
 */
export const formatAsset = (asset: number): string => {
  if (isNaN(asset) || asset < 0) {
    return '잘못된 입력';
  }

  const billion = Math.floor(asset / 1e8);
  const million = Math.floor((asset - billion * 1e8) / 1e4);

  if (billion > 0 && million > 0) {
    return `${billion}억 ${million}만원`;
  } else if (billion > 0) {
    return `${billion}억원`;
  } else if (million > 0) {
    return `${million}만원`;
  } else {
    return '0원';
  }
};

export const effectNewsToString = (curNews: NewsType[]): string => {
  let result = '';
  curNews.forEach((news) => {
    result += news.content + ' ';
  });

  return result;
};
