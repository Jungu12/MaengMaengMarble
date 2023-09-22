import { atom } from 'recoil';

export const TOAST_TYPE = {
  success: 'success',
  error: 'error',
  warning: 'warning',
  info: 'info',
} as const;

export type ToastType = keyof typeof TOAST_TYPE;

export const ToastList = atom<ToastType[]>({
  key: 'ToastList',
  default: [],
});
