import { useState } from 'react';
import api from '../api/axios';
import KakaoLoginButton from '../components/auth/KakaoLoginButton';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);

  const handleLogin = async () => {
    try {
      const res = await api.post('/api/auth/login', { username, password });

      localStorage.setItem('accessToken', res.data.accessToken);
      localStorage.setItem('refreshToken', res.data.refreshToken);

      setMessage('로그인 성공!');
      setIsError(false);

      window.location.href = '/';
    } catch (err) {
      console.error('로그인 실패:', err);
      setMessage('로그인 실패');
      setIsError(true);
    }
  };

  return (
    <div style={{ padding: '24px' }}>
      <h2 style={{ textAlign: 'center', fontSize: '1.8rem', marginBottom: '24px' }}>로그인</h2>

      <div
        style={{
          maxWidth: '400px',
          margin: '0 auto',
          backgroundColor: '#fff',
          padding: '24px',
          borderRadius: '16px',
          boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
        }}
      >
        {message && (
          <div
            style={{
              color: isError ? 'red' : 'green',
              marginBottom: '12px',
              textAlign: 'center',
              fontWeight: 'bold',
            }}
          >
            {message}
          </div>
        )}

        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', marginBottom: '24px' }}>
          <input
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="아이디"
            type="text"
            style={{ padding: '10px', borderRadius: '6px', border: '1px solid #ccc' }}
          />
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="비밀번호"
            style={{ padding: '10px', borderRadius: '6px', border: '1px solid #ccc' }}
          />
          <button
            onClick={handleLogin}
            style={{
              padding: '12px',
              backgroundColor: 'var(--color-primary)',
              color: 'white',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: 'bold',
            }}
          >
            로그인
          </button>
        </div>
      </div>

      <div style={{ maxWidth: '400px', margin: '32px auto 0', textAlign: 'center' }}>
        <KakaoLoginButton />
      </div>
    </div>
  );
}

export default Login;
