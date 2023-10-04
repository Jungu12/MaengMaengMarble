import CMovingCard from '@components/common/CMovingCard';
import { motion, AnimatePresence } from 'framer-motion';

const newscardData = [
  {
    cardImg:
      'https://maeng.s3.ap-northeast-2.amazonaws.com/images/newscard/dia/DIA-1.png',
    cardId: 1,
    event: 'COVID-19 대유행',
  },
  {
    cardImg:
      'https://maeng.s3.ap-northeast-2.amazonaws.com/images/newscard/dia/DIA-2.png',
    cardId: 2,
    event: '미 연준 금리인상',
  },
  {
    cardImg:
      'https://maeng.s3.ap-northeast-2.amazonaws.com/images/newscard/dia/DIA-3.png',
    cardId: 3,
    event: '우크라이나 전쟁',
  },
];

type Props = {
  isOpen: boolean;
  handleNews: () => void;
};

const NewsCardModal = ({ isOpen, handleNews }: Props) => {
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
            className='flex flex-row w-full h-full space-x-20 items-center justify-center'
          >
            {newscardData.map((news) => (
              <CMovingCard key={news.cardId}>
                <button
                  onClick={() => {
                    console.log(news.event);
                    handleNews();
                  }}
                  key={news.cardId}
                >
                  <img
                    className='w-[410px] h-[570px]'
                    src={news.cardImg}
                    alt='뉴스카드'
                  />
                </button>
              </CMovingCard>
            ))}
          </motion.div>
        </div>
      )}
    </AnimatePresence>
  );
};

export default NewsCardModal;
