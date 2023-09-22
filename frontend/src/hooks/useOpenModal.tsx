import { useState } from 'react';

const useOpenModal = () => {
  const [isOpenModal, setIsOpenModal] = useState(false);
  const clickModal = () => {
    setIsOpenModal(true);
    document.body.style.overflow = 'hidden';
  };
  const closeModal = () => {
    setIsOpenModal(false);
    document.body.style.overflow = 'unset';
  };
  return { isOpenModal, clickModal, closeModal };
};

export default useOpenModal;
