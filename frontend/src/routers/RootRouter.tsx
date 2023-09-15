import GameRoom from '@pages/GameRoom';
import HomePage from '@pages/HomePage';
import Lobby from '@pages/Lobby';
import LoginPage from '@pages/LoginPage';
import NotFound from '@pages/NotFound';
import WaitingRoom from '@pages/WaitingRoom';
import { AnimatePresence } from 'framer-motion';
import { BrowserRouter, Route, Routes } from 'react-router-dom';

const RootRouter = () => {
  return (
    <BrowserRouter>
      <AnimatePresence>
        <Routes>
          <Route path='/' element={<HomePage />}></Route>
          <Route path='/login' element={<LoginPage />}></Route>
          <Route path='/lobby' element={<Lobby />}></Route>
          <Route path='/waiting-room'>
            <Route index element={<NotFound />}></Route>
            <Route path=':roomId' element={<WaitingRoom />}></Route>
          </Route>
          <Route path='/game-room'>
            <Route index element={<NotFound />}></Route>
            <Route path=':gameId' element={<GameRoom />}></Route>
          </Route>
        </Routes>
      </AnimatePresence>
    </BrowserRouter>
  );
};

export default RootRouter;
