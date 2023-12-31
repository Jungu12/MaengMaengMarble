import React from 'react';

type Props = {
  type: 'green' | 'red';
  width?: number;
  height?: number;
  rounded?: number;
  onClick?: () => void;
  isDisable?: boolean;
  children: React.ReactNode;
};

const CButton = ({
  type,
  width,
  height,
  onClick,
  isDisable,
  children,
  rounded,
}: Props) => {
  const bgColor = type === 'green' ? '#0BC7B9' : '#DC2C2C';
  const borderColor = type === 'green' ? '#28B1A6' : '#B32727';

  return (
    <div
      className='relative flex justify-center items-center'
      style={{
        width: width ? width : '100%',
        height: height ? height : '100%',
      }}
    >
      <button
        disabled={isDisable ? true : false}
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
