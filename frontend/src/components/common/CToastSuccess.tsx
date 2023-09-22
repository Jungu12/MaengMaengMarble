import { TOAST_TYPE } from '@atom/toastAtom';
import { images } from '@constants/images';
import CToast from './CToast';

type SuccessProps = {
  text: string;
};

const CToastSuccess = ({ text }: SuccessProps) => {
  return (
    <CToast toastType={TOAST_TYPE.success} bgColor='bg-[#DEF2D6]'>
      <div className='flex flex-row items-center'>
        <img
          className='w-8 h-8 mr-5'
          src={images.icon.success}
          alt='성공 아이콘'
        />
        <p className='text-[18px] font-semibold text-[#586C50]'>{text}</p>
      </div>
    </CToast>
  );
};

export default CToastSuccess;
