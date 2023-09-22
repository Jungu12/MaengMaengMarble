import { TOAST_TYPE } from '@atom/toastAtom';
import { images } from '@constants/images';
import CToast from './CToast';

type WarningProps = {
  text: string;
};

const CToastWarning = ({ text }: WarningProps) => {
  return (
    <CToast toastType={TOAST_TYPE.warning} bgColor='bg-[#F8F3D6]'>
      <div className='flex flex-row items-center'>
        <img
          className='w-8 h-8 mr-5'
          src={images.icon.warning}
          alt='경고 아이콘'
        />
        <p className='text-[18px] font-semibold text-[#987032]'>{text}</p>
      </div>
    </CToast>
  );
};

export default CToastWarning;
