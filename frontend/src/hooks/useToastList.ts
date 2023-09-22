import { useCallback } from 'react';
import { ToastList, ToastType } from '@atom/toastAtom';
import { useRecoilState } from 'recoil';

const useToastList = () => {
  const [toastList, setToastList] = useRecoilState<ToastType[]>(ToastList);

  const show = useCallback(
    (toastType: ToastType) => {
      setToastList((prev) => prev.concat(toastType));
    },
    [setToastList]
  );

  const close = useCallback(
    (toastType: ToastType) => {
      setToastList((prev) => prev.filter((t) => t !== toastType));
    },
    [setToastList]
  );

  const closeAll = () => {
    setToastList([]);
  };

  return {
    toastList,
    show,
    close,
    closeAll,
  };
};

export default useToastList;
