import CButton from '@components/common/CButton';
import CMovingCard from '@components/common/CMovingCard';
// import * as StompJs from '@stomp/stompjs';
import { motion, AnimatePresence } from 'framer-motion';

type Props = {
  // client: StompJs.Client;
  // gameId: string;
  cardImg: string;
  isOpen: boolean;
  handleGoldenKey: () => void;
};

const GoldenKeyModal = ({
  // client,
  // gameId,
  cardImg,
  isOpen,
  handleGoldenKey,
}: Props) => {
  return (
    <AnimatePresence>
      {isOpen && (
        <div
          className='absolute flex w-full h-full bg-black bg-opacity-70 items-center justify-center'
          style={{
            zIndex: 100,
          }}
        >
          <motion.div
            exit={{
              opacity: 0,
              scale: 0.75,
              transition: {
                ease: 'easeIn',
                duration: 0.15,
              },
            }}
            className='flex flex-col w-full h-full space-y-[30px] items-center justify-center'
          >
            <CMovingCard>
              <img
                className='w-[410px] h-[570px]'
                src={cardImg}
                alt='뉴스카드'
              />
            </CMovingCard>

            <CButton
              type='green'
              onClick={handleGoldenKey}
              width={150}
              height={50}
              rounded={20}
            >
              <p className='text-[22px] font-black text-primary-100'>확인</p>
            </CButton>
          </motion.div>
        </div>
      )}
    </AnimatePresence>
  );
};

export default GoldenKeyModal;
