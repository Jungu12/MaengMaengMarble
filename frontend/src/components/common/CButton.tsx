import React from 'react';

type Props = {
  width?: number;
  height?: number;
  rounded?: number;
  onClick?: () => void;
  children: React.ReactNode;
};

const CButton = ({ width, height, onClick, children, rounded }: Props) => {
  return (
    <div
      className={`w-${width ? '[' + width + 'px' + ']' : 'full'} h-${
        height ? '[' + height + 'px' + ']' : 'full'
      } relative flex justify-center items-center `}
    >
      <button
        onClick={onClick}
        className={`z-[1] w-full h-full bg-[#0BC7B9] absolute ${
          rounded && `rounded-[${rounded}px]`
        }`}
      >
        <div className='w-full h-full flex items-center justify-center'>
          {children}
        </div>
      </button>
      <div
        className={`w-${width ? '[' + width + 'px' + ']' : 'full'} h-${
          height ? '[' + height + 'px' + ']' : 'full'
        } bg-[#28B1A6] absolute bottom-[-4px] right-[-6px] z-[0] ${
          rounded && `rounded-[${rounded}px]`
        }`}
      ></div>
    </div>
  );
};

export default CButton;
