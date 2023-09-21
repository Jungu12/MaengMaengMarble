import { images } from '@constants/images';
import { motion, AnimatePresence } from 'framer-motion';
import { useCallback, useEffect, useRef } from 'react';

type Props = {
  isOpen: boolean;
  outSideCloseBlock?: boolean; // true인 경우만 안닫기도록 설정
  handleClose: () => void;
  children: React.ReactNode;
};

const CModal = ({
  isOpen,
  handleClose,
  outSideCloseBlock,
  children,
}: Props) => {
  const modalRef = useRef<HTMLDivElement>(null);

  const modalOutSideClick = (
    e: React.MouseEvent<HTMLDivElement, MouseEvent>
  ) => {
    if (outSideCloseBlock) return;
    if (modalRef.current === e.target) {
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
          className='absolute flex w-full h-full bg-black bg-opacity-50 items-center justify-center'
          style={{
            zIndex: 3,
          }}
          ref={modalRef}
          onClick={(e) => modalOutSideClick(e)}
          aria-hidden='true'
        >
          <motion.div
            className='flex flex-col bg-primary-100 rounded-[40px] p-[20px]'
            initial={{
              opacity: 0,
              scale: 0.75,
            }}
            animate={{
              opacity: 1,
              scale: 1,
              transition: {
                ease: 'easeOut',
                duration: 0.15,
              },
            }}
            exit={{
              opacity: 0,
              scale: 0.75,
              transition: {
                ease: 'easeIn',
                duration: 0.15,
              },
            }}
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
        </div>
      )}
    </AnimatePresence>
  );
};

export default CModal;
