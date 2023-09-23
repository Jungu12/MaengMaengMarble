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
  const [isOpenPurchaseModal, setIsOpenPurchaseModal] = useState(false);
  const { show } = useToastList();

  const handlePurchaseModalOpen = useCallback(() => {
    setIsOpenPurchaseModal((prev) => !prev);
  }, []);

  const handlePurchaseModalClose = useCallback(() => {
    setIsOpenPurchaseModal(false);
  }, []);

  const showToastError = useCallback(() => {
    show('error');
  }, [show]);

  return (
    <>
      <PurchaseModal
        isOpenPurchaseModal={isOpenPurchaseModal}
        handlePurchaseModalClose={handlePurchaseModalClose}
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
            <div className='grid grid-cols-4 gap-10 pr-[20px] w-full h-full relative scrollbar'>
              <StoreCharacterCard
                have={true}
                img={images.store.dog1}
                name='허스키'
                point={450}
                onClick={
                  myMoney < 450 ? showToastError : handlePurchaseModalOpen
                }
              />
              <StoreCharacterCard
                have={true}
                img={images.store.dog2}
                name='시츄'
                point={450}
                onClick={
                  myMoney < 450 ? showToastError : handlePurchaseModalOpen
                }
              />
              <StoreCharacterCard
                have={false}
                img={images.store.rabbit}
                name='토끼'
                point={1250}
                onClick={
                  myMoney < 1250 ? showToastError : handlePurchaseModalOpen
                }
              />
              <StoreCharacterCard
                have={false}
                img={images.store.hedgehog}
                name='고슴도치'
                point={1250}
                onClick={
                  myMoney < 1250 ? showToastError : handlePurchaseModalOpen
                }
              />
              <StoreCharacterCard
                have={true}
                img={images.store.panda}
                name='팬더'
                point={4800}
                onClick={
                  myMoney < 4800 ? showToastError : handlePurchaseModalOpen
                }
              />
              <StoreCharacterCard
                have={true}
                img={images.store.whale}
                name='고래'
                point={4800}
                onClick={
                  myMoney < 4800 ? showToastError : handlePurchaseModalOpen
                }
              />
              <StoreCharacterCard
                have={true}
                img={images.store.phoenix}
                name='불사조'
                point={6300}
                onClick={
                  myMoney < 6300 ? showToastError : handlePurchaseModalOpen
                }
              />
              <StoreCharacterCard
                have={false}
                img={images.store.squirrel}
                name='다람쥐'
                point={3150}
                onClick={
                  myMoney < 3150 ? showToastError : handlePurchaseModalOpen
                }
              />
              <StoreCharacterCard
                have={true}
                img={images.store.unicorn}
                name='유니콘'
                point={6300}
                onClick={
                  myMoney < 6300 ? showToastError : handlePurchaseModalOpen
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
