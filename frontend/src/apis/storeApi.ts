import { StoreInfoType } from '@/types/store/store.type';
import { authHttp } from '@utils/http';

export async function getStoreInfo(): Promise<StoreInfoType> {
  return authHttp.get(`user-serivce/shop/list`);
}

export async function purchaseCharacter(cid: number): Promise<StoreInfoType> {
  return authHttp.post(`user-serivce/shop/characters`, {
    id: cid,
  });
}
