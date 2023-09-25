import { TOAST_TYPE } from '@atom/toastAtom';
import { images } from '@constants/images';
import CToast from './CToast';

type ErrorProps = {
  text: string;
};

const CToastError = ({ text }: ErrorProps) => {
  return (
    <CToast toastType={TOAST_TYPE.error} bgColor='bg-[#EBC8C4]'>
      <div className='flex flex-row items-center'>
        <img
          className='w-8 h-8 mr-5'
          src={images.icon.error}
          alt='에러 아이콘'
        />
        <p className='text-[18px] font-semibold text-[#B22838]'>{text}</p>
      </div>
    </CToast>
  );
};

export default CToastError;
