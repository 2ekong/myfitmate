import { Link, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import '../../styles/components/header.css';

function Header() {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    const accessToken = localStorage.getItem('accessToken');
    setIsLoggedIn(!!accessToken);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    setIsLoggedIn(false);
    navigate('/login');
  };

  return (
    <header className="app-header">
      <div className="header-left">
        <button onClick={() => navigate('/')} className="logo">MyFitMate</button>
      </div>

      <nav className="header-center">
        <Link to="/exercise/add" className="nav-icon-link">
          <span className="icon">➕</span> 운동추가
        </Link>
        <Link to="/exercise/log" className="nav-icon-link">
          <span className="icon">📅</span> 운동기록
        </Link>
        <Link to="/meals/add" className="nav-icon-link">
          <span className="icon">🍽️</span> 식단등록
        </Link>
        <Link to="/meals" className="nav-icon-link">
          <span className="icon">📋</span> 식단목록
        </Link>
      </nav>

      <div className="nav-links">
        {!isLoggedIn ? (
          <>
            <Link to="/login"><button className="btn">로그인</button></Link>
            <Link to="/signup"><button className="btn btn-primary">회원가입</button></Link>
          </>
        ) : (
          <>
            <Link to="/mypage"><button className="btn">마이페이지</button></Link>
            <button onClick={handleLogout} className="btn btn-logout">로그아웃</button>
          </>
        )}
      </div>
    </header>
  );
}

export default Header;
