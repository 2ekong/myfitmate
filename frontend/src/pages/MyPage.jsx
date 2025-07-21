import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import '../styles/pages/myPage.css';

function MyPage() {
  const [me, setMe] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMe = async () => {
      try {
        const res = await api.get('/api/user/me');
        setMe(res.data);
      } catch (err) {
        console.error('토큰 유효하지 않음:', err);
        navigate('/login');
      }
    };
    fetchMe();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    navigate('/login');
  };

  const goToEdit = () => {
    navigate('/mypage/edit');
  };

  return (
    <div className="mypage">
      <h2 className="mypage-title">내 정보</h2>

      {me ? (
        <div className="mypage-content">
          <table className="mypage-table">
            <tbody>
              <tr><td><strong>아이디</strong></td><td>{me.username}</td></tr>
              <tr><td><strong>이름</strong></td><td>{me.realName}</td></tr>
              <tr><td><strong>이메일</strong></td><td>{me.email}</td></tr>
              <tr><td><strong>닉네임</strong></td><td>{me.nickname}</td></tr>
              <tr><td><strong>성별</strong></td><td>{me.gender}</td></tr>
              <tr><td><strong>생년월일</strong></td><td>{me.birthDate}</td></tr>
              <tr><td><strong>키</strong></td><td>{me.heightCm} cm</td></tr>
              <tr><td><strong>몸무게</strong></td><td>{me.weightKg} kg</td></tr>
              <tr><td><strong>목표</strong></td><td>{me.goal}</td></tr>
            </tbody>
          </table>

          <div className="mypage-actions">
            <button onClick={goToEdit} className="btn btn-primary">정보 수정</button>
            <button onClick={handleLogout} className="btn btn-logout">로그아웃</button>
          </div>
        </div>
      ) : (
        <p>불러오는 중...</p>
      )}
    </div>
  );
}

export default MyPage;
