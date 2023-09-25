import { StoreInfoType } from '@/types/store/store.type';
import { http } from '@utils/http';

export async function getStoreInfo(): Promise<StoreInfoType> {
  return http.get(`user-serivce/shop/list/nwhjin`);
}

export async function purchaseCharacter(cid: number): Promise<StoreInfoType> {
  return http.post(`user-serivce/shop/characters/${cid}/nwhjin`);
}
