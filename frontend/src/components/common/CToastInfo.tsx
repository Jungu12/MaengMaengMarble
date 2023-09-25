import { TOAST_TYPE, ToastMessageState } from '@atom/toastAtom';
import { images } from '@constants/images';
import CToast from './CToast';
import { useRecoilValue } from 'recoil';

const CToastInfo = () => {
  const text = useRecoilValue(ToastMessageState);

  return (
    <CToast toastType={TOAST_TYPE.info} bgColor='bg-[#CCE8F4]'>
      <div className='flex flex-row items-center'>
        <img
          className='w-8 h-8 mr-5'
          src={images.icon.info}
          alt='정보 아이콘'
        />
        <p className='text-[18px] font-semibold text-[#4780AB]'>{text.info}</p>
      </div>
    </CToast>
  );
};

export default CToastInfo;
