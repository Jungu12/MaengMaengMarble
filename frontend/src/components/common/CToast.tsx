import useToastList from '@hooks/useToastList';
import { useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { createPortal } from 'react-dom';
import { ToastType } from '@atom/toastAtom';
import { images } from '@constants/images';

type Props = {
  toastType: ToastType;
  bgColor: string;
  config?: {
    duration: number;
  };
  children: React.ReactNode;
};

const CToast = ({
  toastType,
  bgColor,
  config = { duration: 3000 },
  children,
}: Props) => {
  const ref = document.querySelector('#toast-root');
  const { duration } = config;
  const { toastList, close } = useToastList();

  const isShown = toastList.includes(toastType);

  useEffect(() => {
    if (!isShown) {
      return undefined;
    }

    const timer = setTimeout(() => {
      close(toastType);
    }, duration);

    return () => {
      clearTimeout(timer);
    };
  }, [close, duration, isShown, toastType]);

  if (!ref) {
    return null;
  }

  return createPortal(
    <AnimatePresence>
      {isShown && (
        <motion.div
          className={`flex flex-row items-center w-[400px] h-[70px] px-[30px] rounded-[10px] ${bgColor} drop-shadow-lg`}
          key={toastType}
          initial={{ opacity: 0.5, y: 24 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0.5, y: 12 }}
          transition={{ ease: 'easeOut' }}
        >
          {children}
          <button className='w-6 h-6 ml-auto' onClick={() => close(toastType)}>
            <img className='w-6 h-6' src={images.icon.close} alt='닫기 버튼' />
          </button>
        </motion.div>
      )}
    </AnimatePresence>,
    ref
  );
};

export default CToast;
