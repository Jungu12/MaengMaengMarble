import GameRoom from '@pages/GameRoom';
import HomePage from '@pages/HomePage';
import Lobby from '@pages/Lobby';
import Store from '@pages/Store';
import LoginCallBackPage from '@pages/LoginCallBackPage';
import LoginPage from '@pages/LoginPage';
import NotFound from '@pages/NotFound';
import WaitingRoom from '@pages/WaitingRoom';
import { AnimatePresence } from 'framer-motion';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import PrivateRoutes from './PrivateRouter';
import ModalTest from '@pages/ModalTest';

const RootRouter = () => {
  return (
    <BrowserRouter>
      <AnimatePresence>
        <Routes>
          <Route path='/' element={<HomePage />}></Route>
          <Route path='/test' element={<ModalTest />}></Route>
          <Route path='/login' element={<LoginPage />}></Route>
          <Route element={<PrivateRoutes />}>
            <Route
              path='/login/oauth/naver/callback'
              element={<LoginCallBackPage />}
            ></Route>
            <Route path='/lobby' element={<Lobby />}></Route>
            <Route path='/store' element={<Store />}></Route>
            <Route path='/waiting-room'>
              <Route index element={<NotFound />}></Route>
              <Route path=':roomId' element={<WaitingRoom />}></Route>
            </Route>
            <Route path='/game-room'>
              <Route index element={<NotFound />}></Route>
              <Route path=':gameId' element={<GameRoom />}></Route>
            </Route>
          </Route>
        </Routes>
      </AnimatePresence>
    </BrowserRouter>
  );
};

export default RootRouter;
