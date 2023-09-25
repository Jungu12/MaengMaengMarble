import { TOAST_TYPE, ToastMessageState } from '@atom/toastAtom';
import { images } from '@constants/images';
import CToast from './CToast';
import { useRecoilValue } from 'recoil';

const CToastWarning = () => {
  const text = useRecoilValue(ToastMessageState);

  return (
    <CToast toastType={TOAST_TYPE.warning} bgColor='bg-[#F8F3D6]'>
      <div className='flex flex-row items-center'>
        <img
          className='w-8 h-8 mr-5'
          src={images.icon.warning}
          alt='경고 아이콘'
        />
        <p className='text-[18px] font-semibold text-[#987032]'>
          {text.warning}
        </p>
      </div>
    </CToast>
  );
};

export default CToastWarning;
