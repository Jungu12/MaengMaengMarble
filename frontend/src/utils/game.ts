import { NewsType, PlayerType } from '@/types/gameRoom/game.type';
import { AnimationControls } from 'framer-motion';
import { images } from '@constants/images';
import { LandType } from '@/types/gameRoom/game.type';

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

export const BUILDING_TYPE = {
  땅값: '땅값',
  별장: '별장',
  빌딩: '빌딩',
  호텔: '호텔',
} as const;

export type BuildingType = keyof typeof BUILDING_TYPE;

/**
 * number타입에 현금단위를 더해 반환한다.
 * @param money - 자산
 * @returns 단위를 추가한 자산 string
 */
export const addAmountUnit = (money: number): string => {
  let unit = '';
  if (money >= 1000000000000) {
    unit = `${Math.trunc(money / 1000000000000)}조`;
    money = money % 1000000000000;
  }
  if (money >= 100000000) {
    unit = `${unit} ${Math.trunc(money / 100000000)}억`;
    money = money % 100000000;
  }
  if (money >= 10000) {
    unit = `${unit} ${Math.trunc(money / 10000)}만`;
    money = money % 10000;
  }

  return unit;
};
/**
 * 해당 땅의 전체 통행료를 계산한다.
 * @param landInfo - 해당 땅의 정보
 * @returns 해당 땅의 통행료
 */
export const calCurrentFees = (landInfo: LandType): number => {
  let fee = 0;

  for (let i = 0; i < 4; i++) {
    if (landInfo.buildings[i]) fee += landInfo.currentFees[i];
  }

  return fee;
};
/**
 * 땅 색상을 반환한다.
 * @param landId - 땅 id
 * @returns 해당 땅의 색상
 */
export const landColor = (landId: number): string => {
  if (landId <= 5) return '#82AC40';
  else if (landId <= 7) return '#33A44A';
  else if (landId <= 11) return '#31B2C7';
  else if (landId <= 15) return '#2A81C4';
  else if (landId <= 19) return '#E85984';
  else if (landId <= 23) return '#7B61AA';
  else if (landId <= 26) return '#F47A2D';
  else return '#ED3B37';
};
/**
 * 땅 국기 이미지를 반환한다.
 * @param landId - 땅 id
 * @returns 해당 땅의 국기 이미지
 */
export const landNationalFlag = (landId: number): string => {
  switch (landId) {
    case 1:
      return images.flag.vietnam;
    case 3:
      return images.flag.thailand;
    case 5:
      return images.flag.singapore;
    case 6:
      return images.flag.egypt;
    case 7:
      return images.flag.southAfrica;
    case 9:
      return images.flag.argentina;
    case 11:
      return images.flag.brazil;
    case 13:
      return images.flag.catarrh;
    case 14:
      return images.flag.iran;
    case 15:
      return images.flag.saudiArabia;
    case 17:
      return images.flag.czech;
    case 19:
      return images.flag.russia;
    case 21:
      return images.flag.france;
    case 22:
      return images.flag.germany;
    case 23:
      return images.flag.uk;
    case 25:
      return images.flag.usa;
    case 26:
      return images.flag.canada;
    case 27:
      return images.flag.India;
    case 29:
      return images.flag.china;
    case 30:
      return images.flag.japan;
    case 32:
      return images.flag.korea;
    default:
      return images.flag.korea;
  }
};
/**
 * 땅 랜드마크 이미지 을 반환한다.
 * @param landId - 땅 id
 * @returns 해당 땅의 랜드마크 이미지
 */
export const landLandMarksImage = (landId: number): string => {
  switch (landId) {
    case 1:
      return images.land.vietnam;
    case 3:
      return images.land.thailand;
    case 5:
      return images.land.singapore;
    case 6:
      return images.land.egypt;
    case 7:
      return images.land.southAfrica;
    case 9:
      return images.land.argentina;
    case 11:
      return images.land.brazil;
    case 13:
      return images.land.catarrh;
    case 14:
      return images.land.iran;
    case 15:
      return images.land.saudiArabia;
    case 17:
      return images.land.czech;
    case 19:
      return images.land.russia;
    case 21:
      return images.land.france;
    case 22:
      return images.land.germany;
    case 23:
      return images.land.uk;
    case 25:
      return images.land.usa;
    case 26:
      return images.land.canada;
    case 27:
      return images.land.India;
    case 29:
      return images.land.china;
    case 30:
      return images.land.japan;
    case 32:
      return images.land.korea;
    default:
      return images.land.korea;
  }
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
