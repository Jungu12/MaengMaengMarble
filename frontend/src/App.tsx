import CToastError from '@components/common/CToastError';
import CToastInfo from '@components/common/CToastInfo';
import CToastSuccess from '@components/common/CToastSuccess';
import CToastWarning from '@components/common/CToastWarning';
import RootRouter from '@routers/RootRouter';

function App() {
  return (
    <div className='h-screen w-screen min-w-[1340px]'>
      <RootRouter />
      <CToastError />
      <CToastSuccess />
      <CToastInfo />
      <CToastWarning />
    </div>
  );
}

export default App;
