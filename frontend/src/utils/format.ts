// 천단위 콤마 함수
export const addComma = (num: number): string => {
  const regexp = /\B(?=(\d{3})+(?!\d))/g;
  return num.toString().replace(regexp, ',');
};

/**
 * 주어진 형식의 날짜 문자열을 Date 객체로 변환합니다.
 * @param {string} str - 날짜 형식의 문자열 (예: Wed Sep 27 14:50:29 KST 2023)
 * @returns {Date} 변환된 Date 객체
 */
export const formatStringToDate = (str: string) => {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [_day, month, date, time, _zone, year] = str.split(' ');
  const [hours, minutes, seconds] = time.split(':');
  const monthIndex = [
    'Jan',
    'Feb',
    'Mar',
    'Apr',
    'May',
    'Jun',
    'Jul',
    'Aug',
    'Sep',
    'Oct',
    'Nov',
    'Dec',
  ].indexOf(month);

  return new Date(
    `${year}-${(monthIndex + 1)
      .toString()
      .padStart(2, '0')}-${date}T${hours}:${minutes}:${seconds}Z`
  );
};
