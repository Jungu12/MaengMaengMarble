// 천단위 콤마 함수
export const addComma = (num: number): string => {
  const regexp = /\B(?=(\d{3})+(?!\d))/g;
  return num.toString().replace(regexp, ',');
};
