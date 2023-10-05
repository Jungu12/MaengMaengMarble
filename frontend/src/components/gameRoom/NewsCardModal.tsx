import { NewsType } from '@/types/gameRoom/game.type';
import CMovingCard from '@components/common/CMovingCard';
import { motion, AnimatePresence } from 'framer-motion';
import * as StompJs from '@stomp/stompjs';

type Props = {
  type: string;
  client: StompJs.Client;
  gameId: string;
  isMyTurn: boolean;
  newsList: NewsType[];
  isOpen: boolean;
  handleNews: () => void;
};

const NewsCardModal = ({
  type,
  client,
  gameId,
  isMyTurn,
  newsList,
  isOpen,
  handleNews,
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
            className='flex flex-row w-full h-full space-x-20 items-center justify-center'
          >
            {newsList.map((news) => (
              <CMovingCard key={news.newsId}>
                <button
                  onClick={() => {
                    client?.publish({
                      destination: `/pub/game-rooms/news/${gameId}`,
                      body: JSON.stringify({ news: news, type: type }),
                    });
                    console.log(news.content);
                    handleNews();
                  }}
                  key={news.newsId}
                  disabled={isMyTurn ? false : true}
                >
                  <img
                    className='w-[410px] h-[570px]'
                    src={news.imageUrl}
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
