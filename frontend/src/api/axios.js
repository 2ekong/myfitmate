import axios from 'axios';

// ✅ interceptor 안 타는 순수 요청용 인스턴스
const plainAxios = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true,
});

// ✅ interceptor + 토큰 자동 처리되는 인스턴스
const api = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true,
});

// ✅ 요청 시 accessToken 자동 부착
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => Promise.reject(error));

// ✅ 응답 시 accessToken 만료 처리 및 재시도
api.interceptors.response.use(
  response => response,
  async (error) => {
    const originalRequest = error.config;

    // refresh 요청은 반복 처리 X
    if (originalRequest.url.includes('/api/auth/refresh')) {
      return Promise.reject(error);
    }

    const refreshToken = localStorage.getItem('refreshToken');

    if ((error.response?.status === 401 || error.response?.status === 403) && refreshToken && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const res = await plainAxios.post(
          '/api/auth/refresh',
          {},
          {
            headers: {
              Authorization: `Bearer ${refreshToken}`,
            },
          }
        );

        const newAccessToken = res.data.accessToken;
        localStorage.setItem('accessToken', newAccessToken);
        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;

        return api(originalRequest);

      } catch (refreshError) {
        console.error('🔒 토큰 재발급 실패:', refreshError);
        localStorage.clear();

        // 👇 로그인 페이지로 이동하지 않고, 프론트에서 직접 판단하게 처리
        return Promise.reject({ ...refreshError, isAuthFailure: true });
      }
    }

    return Promise.reject(error);
  }
);

export default api;
