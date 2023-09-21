import CButton from '@components/common/CButton';
import CModal from '@components/common/CModal';
import MyPageCharacterCard from '@components/mypage/MyPageCharacterCard';
import { images } from '@constants/images';
import {} from 'react';

type NewRoomModalProps = {
  isOpenNewRoomModal: boolean;
  handleMyPageModalClose: () => void;
};

const MyPageModal = ({
  isOpenNewRoomModal,
  handleMyPageModalClose,
}: NewRoomModalProps) => {
  return (
    <CModal isOpen={isOpenNewRoomModal} handleClose={handleMyPageModalClose}>
      <div className='flex p-[16px] min-w-[960px]'>
        <div className='min-w-[600px] w-[600px] px-[24px] py-[12px] h-[600px] flex-1 flex flex-col overflow-y-scroll overflow-x-hidden scrollbar gap-[70px]'>
          <div className='w-[600px] flex gap-[16px]'>
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter1}
              alt=''
              name='푸바오'
              status='choice'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter2}
              alt=''
              name='깜찍이'
              status='lock'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter3}
              alt=''
              name='맹티즈'
              status='possession'
            />
          </div>

          <div className='min-w-[600px] flex gap-[16px]'>
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter1}
              alt=''
              name='푸바오'
              status='choice'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter2}
              alt=''
              name='깜찍이'
              status='lock'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter3}
              alt=''
              name='맹티즈'
              status='possession'
            />
          </div>

          <div className='min-w-[600px] flex gap-[16px]'>
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter1}
              alt=''
              name='푸바오'
              status='choice'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter2}
              alt=''
              name='깜찍이'
              status='lock'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter3}
              alt=''
              name='맹티즈'
              status='possession'
            />
          </div>

          <div className='min-w-[600px] flex gap-[16px]'>
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter1}
              alt=''
              name='푸바오'
              status='choice'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter2}
              alt=''
              name='깜찍이'
              status='lock'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter3}
              alt=''
              name='맹티즈'
              status='possession'
            />
          </div>

          <div className='min-w-[600px] flex gap-[16px]'>
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter1}
              alt=''
              name='푸바오'
              status='choice'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter2}
              alt=''
              name='깜찍이'
              status='lock'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter3}
              alt=''
              name='맹티즈'
              status='possession'
            />
          </div>
        </div>
        <div className='min-w-[450px] flex flex-col relative ml-[32px] items-center'>
          <img
            className='h-[450px] w-full object-cover rounded-[12px]'
            src={images.dummy.dummyCharacter1}
            alt='내 캐릭터'
          />
          {true && (
            <div className='w-full flex justify-end mt-[12px] items-center px-[12px]'>
              <img
                className='w-[16px] h-[16px] mr-[4px]'
                src={images.icon.error}
                alt='에러'
              />
              <p className='text-[#E30909] text-sm'>에러발생~</p>
            </div>
          )}
          <div className='w-full h-[54px] mt-[4px] relative flex justify-center'>
            <input
              className='flex text-center w-full h-full rounded-[20px] font-semibold text-xl text-[#5F594B] outline-none'
              value={'개멋있는 사람'}
              readOnly
            />
            <button
              onClick={() => {
                console.log('수정 모드~');
              }}
            >
              <img
                className='absolute h-[32px] w-[32px] right-[16px] bottom-[12px] cursor-pointer'
                src={images.icon.edit}
                alt='수정하기'
              />
            </button>
          </div>
          <div className='w-full h-[48px] mt-[16px] relative'>
            <div className='w-full h-full'>
              <CButton rounded={20}>
                <p className={`text-xl font-semibold text-[#FFFDF2]`}>
                  저장하기
                </p>
              </CButton>
            </div>
          </div>
        </div>
      </div>
    </CModal>
  );
};

export default MyPageModal;
