import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import api from '../api/axios';

function KakaoCallback() {
    const navigate = useNavigate();

    useEffect(() => {
        const code = new URL(window.location.href).searchParams.get('code');

        if (code) {
            api.post(`/api/auth/kakao?code=${code}`)
            .then(res => {
  const accessToken = res.data.accessToken;
  const refreshToken = res.data.refreshToken;

  console.log("accessToken:", accessToken);
  console.log("refreshToken:", refreshToken);

  localStorage.setItem('accessToken', accessToken);
  localStorage.setItem('refreshToken', refreshToken);

  alert('카카오 로그인 성공');
  navigate('/');
})
.catch(err => {
  console.error('카카오 로그인 실패:', err.response?.data || err.message || err);
  alert('카카오 로그인 실패');
  navigate('/login');
});

        }
    }, [navigate]);

    return <div>카카오 로그인 중입니다..</div>;
}

export default KakaoCallback;