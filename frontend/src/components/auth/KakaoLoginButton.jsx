function KakaoLoginButton() {
  const kakaoClientId = '27fae1b368178a29c6205f15379f946c';
  const redirectUri = 'http://localhost:5173/kakao/callback';
  const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${kakaoClientId}&redirect_uri=${redirectUri}`;

  return (
    <button
      onClick={() => (window.location.href = kakaoAuthUrl)}
      style={{
        backgroundColor: '#FEE500',
        color: '#000000de',
        border: 'none',
        borderRadius: '6px',
        fontWeight: 'bold',
        fontSize: '15px',
        padding: '12px',
        width: '100%',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        gap: '8px',
        cursor: 'pointer',
        boxShadow: '0 1px 1px rgba(0, 0, 0, 0.1)',
      }}
    >
      <img
        src="/images/KakaoTalk_logo.svg"
        alt="kakao logo"
        style={{ width: '20px', height: '20px' }}
      />
      <span>카카오로 로그인하기</span>
    </button>
  );
}

export default KakaoLoginButton;
