import { motion, useMotionValue, useTransform } from 'framer-motion';
import { colors } from '@constants/colors';

const tickVariants = {
  pressed: (isChecked: boolean) => ({ pathLength: isChecked ? 0.85 : 0.2 }),
  checked: { pathLength: 1 },
  unchecked: { pathLength: 0 },
};

type Props = {
  isChecked: boolean;
  handleChecked: () => void;
};

const CCheckBox = ({ isChecked, handleChecked }: Props) => {
  const pathLength = useMotionValue(0);
  const opacity = useTransform(pathLength, [0.05, 0.15], [0, 1]);

  return (
    <motion.div
      className='bg-primary-200 rounded-[10px]'
      style={{
        boxShadow: '4px 4px 4px 0px rgba(0, 0, 0, 0.25) inset',
      }}
    >
      <motion.svg
        initial={false}
        animate={isChecked ? 'checked' : 'unchecked'}
        whileHover='hover'
        whileTap='pressed'
        width='44'
        height='44'
        onClick={handleChecked}
      >
        <motion.path
          d='M 7.2 13.6 C 7.2 10.0654 10.0654 7.2 13.6 7.2 L 30.4 7.2 C 33.9346 7.2 36.8 10.0654 36.8 13.06 L 36.8 30.4 C 36.8 33.9346 33.9346 36.8 30.4 36.8 L 13.6 36.8 C 10.0654 36.8 7.2 33.9346 7.2 30.4 Z'
          fill='transparent'
        />
        <motion.path
          d='M 0 12.8666 L 12.8658 25.7373 L 34.1808 0'
          transform='translate(5.4917 6.8947) rotate(-0.4 17.0904 12.8687)'
          fill='transparent'
          strokeWidth='10'
          stroke={colors.third[100]}
          strokeLinecap='round'
          strokeLinejoin='round'
          variants={tickVariants}
          style={{ pathLength, opacity }}
          custom={isChecked}
        />
      </motion.svg>
    </motion.div>
  );
};

export default CCheckBox;
