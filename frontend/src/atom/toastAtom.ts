import { atom } from 'recoil';

export const TOAST_TYPE = {
  success: 'success',
  error: 'error',
  warning: 'warning',
  info: 'info',
} as const;

export type ToastMessageType = {
  success: string;
  error: string;
  warning: string;
  info: string;
};

export type ToastType = keyof typeof TOAST_TYPE;

export const ToastMessageState = atom<ToastMessageType>({
  key: 'ToastMessage',
  default: {
    success: '',
    error: '',
    warning: '',
    info: '',
  },
});

export const ToastList = atom<ToastType[]>({
  key: 'ToastList',
  default: [],
});
