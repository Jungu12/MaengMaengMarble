import { TOAST_TYPE } from '@atom/toastAtom';
import { images } from '@constants/images';
import CToast from './CToast';

type InfoProps = {
  text: string;
};

const CToastInfo = ({ text }: InfoProps) => {
  return (
    <CToast toastType={TOAST_TYPE.info} bgColor='bg-[#CCE8F4]'>
      <div className='flex flex-row items-center'>
        <img
          className='w-8 h-8 mr-5'
          src={images.icon.info}
          alt='정보 아이콘'
        />
        <p className='text-[18px] font-semibold text-[#4780AB]'>{text}</p>
      </div>
    </CToast>
  );
};

export default CToastInfo;
