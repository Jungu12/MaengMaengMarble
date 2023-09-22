import React from 'react';
import { images } from '@constants/images';
import { motion } from 'framer-motion';

type PeopleRadioButtonProps = {
  label: string;
  value: string;
  checked: boolean;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
};

const PeopleRadioButton = ({
  label,
  value,
  checked,
  onChange,
}: PeopleRadioButtonProps) => {
  return (
    <motion.div
      className='inline-flex items-center'
      whileHover={{ scale: 1.1 }}
      whileTap={{ scale: 0.9 }}
      transition={{ type: 'spring', stiffness: 400, damping: 17 }}
    >
      <label className='inline-flex items-center'>
        <input
          type='radio'
          className='absolute w-0 h-0 p-0 m-n1 overflow-hidden clip-[0 0 0 0] whitespace-nowrap border-0'
          style={{
            background: `url(${images.button.checked})`,
          }}
          value={value}
          checked={checked}
          onChange={onChange}
        />
        <span className='flex flex-row ml-5 text-text-100 text-2xl font-extrabold'>
          <img
            className='w-8 h-8 mr-5'
            src={checked ? images.button.checked : images.button.unchecked}
            alt='라디오 버튼'
          />
          {label}
        </span>
      </label>
    </motion.div>
  );
};

export default PeopleRadioButton;
