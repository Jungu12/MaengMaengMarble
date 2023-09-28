import { motion, AnimatePresence } from 'framer-motion';
import { useCallback, useEffect, useRef } from 'react';
import { images } from '@constants/images';

type Props = {
  isOpen: boolean;
  handleClose: () => void;
  children: React.ReactNode;
};

const CSideBar = ({ isOpen, handleClose, children }: Props) => {
  const sideBarRef = useRef<HTMLDivElement>(null);

  const sideBarOutSideClick = (
    e: React.MouseEvent<HTMLDivElement, MouseEvent>
  ) => {
    if (sideBarRef.current === e.target) {
      handleClose();
    }
  };

  const handleEscKey = useCallback(
    (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        // ESC 키가 눌렸을 때 실행할 동작을 여기에 추가
        handleClose();
        // 예를 들어 모달을 닫는 함수를 호출하거나 다른 동작을 수행할 수 있습니다.
      }
    },
    [handleClose]
  );

  // 컴포넌트가 마운트될 때 이벤트 리스너 추가
  useEffect(() => {
    if (isOpen) {
      window.addEventListener('keydown', handleEscKey);
    }

    // 컴포넌트가 언마운트될 때 이벤트 리스너 제거
    return () => {
      window.removeEventListener('keydown', handleEscKey);
    };
  }, [handleEscKey, isOpen]);

  return (
    <AnimatePresence>
      {isOpen && (
        <div
          className='absolute flex w-full h-full bg-black bg-opacity-50 items-center justify-end overflow-auto'
          style={{
            zIndex: 100,
          }}
          ref={sideBarRef}
          onClick={(e) => sideBarOutSideClick(e)}
          aria-hidden='true'
        >
          <motion.div
            className='flex flex-col h-full w-fit bg-primary-100 p-[20px]'
            initial={{ width: 0 }}
            animate={{
              width: 'fit-content',
            }}
            exit={{
              width: 0,
              transition: { duration: 0.2 },
            }}
          >
            <motion.div
              className='w-full h-full flex flex-col overflow-auto'
              initial='closed'
              animate='open'
              exit='closed'
              style={{ overflowX: 'hidden' }}
            >
              <button
                className='ml-auto'
                onClick={() => {
                  handleClose();
                }}
              >
                <img
                  className='w-10 h-10'
                  src={images.button.close}
                  alt='닫기 버튼'
                />
              </button>
              {children}
            </motion.div>
          </motion.div>
        </div>
      )}
    </AnimatePresence>
  );
};

export default CSideBar;
