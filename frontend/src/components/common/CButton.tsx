import React from 'react';

type Props = {
  type: 'green' | 'red';
  width?: number;
  height?: number;
  rounded?: number;
  onClick?: () => void;
  children: React.ReactNode;
};

const CButton = ({
  type,
  width,
  height,
  onClick,
  children,
  rounded,
}: Props) => {
  const bgColor = type === 'green' ? '#0BC7B9' : '#DC2C2C';
  const borderColor = type === 'green' ? '#28B1A6' : '#B32727';
  console.log(borderColor);

  return (
    <div
      className={`w-${width ? '[' + width + 'px' + ']' : 'full'} h-${
        height ? '[' + height + 'px' + ']' : 'full'
      } relative flex justify-center items-center `}
    >
      <button
        onClick={onClick}
        className={`z-[1] w-full h-full absolute ${
          rounded && `rounded-[${rounded}px]`
        }`}
        style={{
          backgroundColor: `${bgColor}`,
        }}
      >
        <div className='w-full h-full flex items-center justify-center'>
          {children}
        </div>
      </button>
      <div
        className={`w-full h-full absolute bottom-[-4px] right-[-6px] z-[0] ${
          rounded && `rounded-[${rounded}px]`
        }`}
        style={{
          backgroundColor: `${borderColor}`,
        }}
      ></div>
    </div>
  );
};

export default CButton;
