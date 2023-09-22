import { images } from '@constants/images';
import { addComma } from '@utils/format';
import { useCallback, useState } from 'react';
import StoreCharacterCard from '@components/store/StoreCharacterCard';
import StoreOwnerView from '@components/store/StoreOwnerView';
import PurchaseModal from '@components/modal/PurchaseModal';
import CToastSuccess from '@components/common/CToastSuccess';
import CToastError from '@components/common/CToastError';
import useToastList from '@hooks/useToastList';

const Store = () => {
  const myMoney = 3000;
  const [isOpenPurchaseModal, setIsOpenMyPageModal] = useState(false);
  const { show } = useToastList();

  const handleMyPageModalOpen = useCallback(() => {
    setIsOpenMyPageModal((prev) => !prev);
  }, []);

  const handleMyPageModalClose = useCallback(() => {
    setIsOpenMyPageModal(false);
  }, []);

  const showToastError = useCallback(() => {
    show('error');
  }, [show]);

  return (
    <>
      <PurchaseModal
        isOpenPurchaseModal={isOpenPurchaseModal}
        handlePurchaseModalClose={handleMyPageModalClose}
      />
      <div
        className='flex flex-row w-full h-full justify-between relative'
        style={{
          backgroundImage: `url(${images.store.background})`,
          backgroundSize: 'cover',
        }}
      >
        <StoreOwnerView />

        <div className='basis-2/3 flex flex-col h-full items-end justify-between py-[45px] pr-[45px] relative'>
          <div className='flex flex-row items-center space-x-7 px-6 py-3 mb-[25px] bg-white bg-opacity-50 rounded-[40px]'>
            <img
              className='w-[40px] h-[40px] ml-auto mr-4'
              src={images.icon.point}
              alt='포인트 아이콘'
            />
            <p className='text-[30px] font-bold text-text-100'>
              {addComma(myMoney)}
            </p>
          </div>

          <div className='flex flex-col w-full h-full p-[25px] overflow-auto relative bg-primary-dark300 bg-opacity-70 rounded-[40px]'>
            {/* <div className='flex flex-col w-full h-full overflow-auto relative bg-white scrollbar'> */}
            <div className='grid grid-cols-4 gap-10 pr-[20px] w-full h-full relative scrollbar'>
              <StoreCharacterCard
                have={true}
                img={images.store.dog1}
                name='허스키'
                point={addComma(450)}
                onClick={myMoney < 450 ? showToastError : handleMyPageModalOpen}
              />
              <StoreCharacterCard
                have={true}
                img={images.store.dog2}
                name='시츄'
                point={addComma(450)}
                onClick={myMoney < 450 ? showToastError : handleMyPageModalOpen}
              />
              <StoreCharacterCard
                have={false}
                img={images.store.rabbit}
                name='토끼'
                point={addComma(1250)}
                onClick={
                  myMoney < 1250 ? showToastError : handleMyPageModalOpen
                }
              />
              <StoreCharacterCard
                have={false}
                img={images.store.hedgehog}
                name='고슴도치'
                point={addComma(1250)}
                onClick={
                  myMoney < 1250 ? showToastError : handleMyPageModalOpen
                }
              />
              <StoreCharacterCard
                have={true}
                img={images.store.panda}
                name='팬더'
                point={addComma(4800)}
                onClick={
                  myMoney < 4800 ? showToastError : handleMyPageModalOpen
                }
              />
              <StoreCharacterCard
                have={true}
                img={images.store.whale}
                name='고래'
                point={addComma(4800)}
                onClick={
                  myMoney < 4800 ? showToastError : handleMyPageModalOpen
                }
              />
              <StoreCharacterCard
                have={true}
                img={images.store.phoenix}
                name='불사조'
                point={addComma(6300)}
                onClick={
                  myMoney < 6300 ? showToastError : handleMyPageModalOpen
                }
              />
              <StoreCharacterCard
                have={false}
                img={images.store.squirrel}
                name='다람쥐'
                point={addComma(3150)}
                onClick={
                  myMoney < 3150 ? showToastError : handleMyPageModalOpen
                }
              />
              <StoreCharacterCard
                have={true}
                img={images.store.unicorn}
                name='유니콘'
                point={addComma(6300)}
                onClick={
                  myMoney < 6300 ? showToastError : handleMyPageModalOpen
                }
              />
            </div>
          </div>
        </div>
      </div>
      <CToastSuccess text='구매 완료' />
      <CToastError text='포인트가 부족합니다' />
    </>
  );
};

export default Store;
