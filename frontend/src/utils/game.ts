import { AnimationControls } from 'framer-motion';

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
