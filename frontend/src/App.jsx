import { Outlet, useLocation } from 'react-router-dom';
import Header from './components/common/Header';
import MainLayout from './components/common/MainLayout';
import './styles/index.css';

function App() {
  const location = useLocation();
  const isTransparent = location.pathname === '/exercise/add'; // 이 페이지만 투명

  return (
    <>
      <Header />
      <MainLayout transparent={isTransparent}>
        <Outlet />
      </MainLayout>
    </>
  );
}

export default App;
