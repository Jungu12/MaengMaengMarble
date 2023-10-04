import { PlayerType } from '@/types/gameRoom/game.type';
import MapArea from './MapArea';
import { images } from '@constants/images';
import { AnimationControls, motion } from 'framer-motion';

type Props = {
  playerList: (PlayerType | null)[];
  onClickLand: (landId: number) => void;
  controls1: AnimationControls;
  controls2: AnimationControls;
  controls3: AnimationControls;
  controls4: AnimationControls;
};

const GameMap = ({
  playerList,
  onClickLand,
  controls1,
  controls2,
  controls3,
  controls4,
}: Props) => {
  return (
    <div className='absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 mt-[40px] w-[640px] h-[640px] ]'>
      {/* 필드 타일 */}
      {/* 아랫줄 */}
      <div className='absolute bottom-0 flex justify-between gap-[6px]'>
        <MapArea
          src={images.map.transactionSuspended}
          alt='거래정지'
          value={8}
          width={90}
          height={90}
        />
        <MapArea
          src={images.map.southAfrica}
          alt='남아공'
          value={7}
          width={60}
          height={90}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.egypt}
          alt='이집트'
          value={6}
          width={60}
          height={90}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.singapore}
          alt='싱가포르'
          value={5}
          width={60}
          height={90}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.goldKey1}
          alt='황금열쇠'
          value={4}
          width={60}
          height={90}
        />
        <MapArea
          src={images.map.thailand}
          alt='태국'
          value={3}
          width={60}
          height={90}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.revenue}
          alt='국세청'
          value={2}
          width={60}
          height={90}
        />
        <MapArea
          src={images.map.vietnam}
          alt='베트남'
          value={1}
          width={60}
          height={90}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.start}
          alt='시작점'
          value={0}
          width={90}
          // height={90}
        />
      </div>
      {/* 왼쪽줄 */}
      <div className='absolute left-0 bottom-[96px] flex flex-col justify-between gap-[5px]'>
        <MapArea
          src={images.map.saudiArabia}
          alt='사우디아라비아'
          value={15}
          width={90}
          height={60}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.iran}
          alt='이란'
          value={14}
          width={90}
          height={60}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.catarrh}
          alt='카타르'
          value={13}
          width={90}
          height={60}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.goldKey2}
          alt='황금열쇠'
          value={12}
          width={90}
          height={60}
        />
        <MapArea
          src={images.map.brazil}
          alt='브라질'
          value={11}
          width={90}
          height={60}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.macau}
          alt='마카오'
          value={10}
          width={90}
          height={60}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.argentina}
          alt='아르헨티나'
          value={9}
          width={90}
          height={60}
          onClickArea={onClickLand}
        />
      </div>
      {/* 윗줄 */}
      <div className='absolute top-0 flex justify-between gap-[6px]'>
        <MapArea
          src={images.map.stockMarket}
          alt='주식장'
          value={16}
          width={90}
          height={90}
        />
        <MapArea
          src={images.map.czech}
          alt='체코'
          value={17}
          width={60}
          height={90}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.rushAndCash}
          alt='대출'
          value={18}
          width={60}
          height={90}
        />
        <MapArea
          src={images.map.russia}
          alt='러시아'
          value={19}
          width={60}
          height={90}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.goldKey3}
          alt='황금열쇠3'
          value={20}
          width={60}
          height={90}
        />
        <MapArea
          src={images.map.france}
          alt='프랑스'
          value={21}
          width={60}
          height={90}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.germany}
          alt='독일'
          value={22}
          width={60}
          height={90}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.uk}
          alt='영국'
          value={23}
          width={60}
          height={90}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.anywhere}
          alt='어디로든 문'
          value={24}
          width={90}
          height={90}
        />
      </div>
      {/* 오른쪽줄 */}
      <div className='absolute right-0 top-[96px] bottom-[96px] flex flex-col justify-between gap-[5px]'>
        <MapArea
          src={images.map.usa}
          alt='미국'
          value={25}
          width={90}
          height={60}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.canada}
          alt='캐나다'
          value={26}
          width={90}
          height={60}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.India}
          alt='인도'
          value={27}
          width={90}
          height={60}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.goldKey4}
          alt='황금열쇠'
          value={28}
          width={90}
          height={60}
        />
        <MapArea
          src={images.map.china}
          alt='중국'
          value={29}
          width={90}
          height={60}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.japan}
          alt='일본'
          value={30}
          width={90}
          height={60}
          onClickArea={onClickLand}
        />
        <MapArea
          src={images.map.korea}
          alt='한국'
          value={31}
          width={90}
          height={60}
          onClickArea={onClickLand}
        />
      </div>
      {/* 캐릭터 */}
      {playerList[0] && (
        <motion.div
          animate={controls1}
          className={`absolute`}
          style={{
            bottom: `20px`,
            right: `20px`,
          }}
        >
          <img
            className='h-[64px]'
            src={playerList[0].avatarImage}
            alt='캐릭터'
            style={{
              filter: 'drop-shadow(1px 1px 1px #000)',
            }}
          />
        </motion.div>
      )}
      {playerList[1] && (
        <motion.div
          animate={controls2}
          className={`absolute`}
          style={{
            bottom: `20px`,
            right: `20px`,
          }}
        >
          <img
            className='h-[64px]'
            src={playerList[1].avatarImage}
            alt='캐릭터'
            style={{
              filter: 'drop-shadow(1px 1px 1px #000)',
            }}
          />
        </motion.div>
      )}
      {playerList[2] && (
        <motion.div
          animate={controls3}
          className={`absolute`}
          style={{
            bottom: `20px`,
            right: `20px`,
          }}
        >
          <img
            className='h-[64px]'
            src={playerList[2].avatarImage}
            alt='캐릭터'
            style={{
              filter: 'drop-shadow(1px 1px 1px #000)',
            }}
          />
        </motion.div>
      )}
      {playerList[3] && (
        <motion.div
          animate={controls4}
          className={`absolute`}
          style={{
            bottom: `20px`,
            right: `20px`,
          }}
        >
          <img
            className='h-[64px]'
            src={playerList[3].avatarImage}
            alt='캐릭터'
            style={{
              filter: 'drop-shadow(1px 1px 1px #000)',
            }}
          />
        </motion.div>
      )}
    </div>
  );
};

export default GameMap;
