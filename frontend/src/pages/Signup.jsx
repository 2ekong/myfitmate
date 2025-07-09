import { useState } from 'react';
import api from '../api/axios';
import '../styles/pages/signup.css';

function Signup() {
  const [form, setForm] = useState({
    username: '',
    realName: '',
    nickname: '',
    email: '',
    password: '',
    gender: '',
    birthDate: '',
    heightCm: '',
    weightKg: '',
    goal: '',
  });

  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    try {
      await api.post('/api/auth/signup', form);
      setMessage('회원가입 성공!');
      setIsError(false);
    } catch (err) {
      console.error('회원가입 실패:', err);
      setMessage('회원가입 실패');
      setIsError(true);
    }
  };

  return (
    <div className="signup">
      <h2 className="signup-title">회원가입</h2>

      <div className="signup-card">
        {message && (
          <div className={`message ${isError ? 'message-error' : 'message-success'}`}>
            {message}
          </div>
        )}

        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleSubmit();
          }}
          className="signup-form"
        >
          <label>
            아이디:
            <input
              type="text"
              name="username"
              value={form.username}
              onChange={handleChange}
              required
              className="form-input"
            />
          </label>

          <label>
            이름:
            <input
              type="text"
              name="realName"
              value={form.realName}
              onChange={handleChange}
              required
              className="form-input"
            />
          </label>

          <label>
            닉네임:
            <input
              type="text"
              name="nickname"
              value={form.nickname}
              onChange={handleChange}
              required
              className="form-input"
            />
          </label>

          <label>
            이메일:
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              required
              className="form-input"
            />
          </label>

          <label>
            비밀번호:
            <input
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              required
              className="form-input"
            />
          </label>

          <label>
            성별:
            <select
              name="gender"
              value={form.gender}
              onChange={handleChange}
              required
              className="form-input"
            >
              <option value="">선택</option>
              <option value="MALE">남성</option>
              <option value="FEMALE">여성</option>
            </select>
          </label>

          <label>
            생년월일:
            <input
              type="date"
              name="birthDate"
              value={form.birthDate}
              onChange={handleChange}
              required
              className="form-input"
            />
          </label>

          <label>
            키 (cm):
            <input
              type="number"
              name="heightCm"
              value={form.heightCm}
              onChange={handleChange}
              required
              className="form-input"
            />
          </label>

          <label>
            몸무게 (kg):
            <input
              type="number"
              name="weightKg"
              value={form.weightKg}
              onChange={handleChange}
              required
              className="form-input"
            />
          </label>

          <label>
            목표:
            <select
              name="goal"
              value={form.goal}
              onChange={handleChange}
              required
              className="form-input"
            >
              <option value="">선택</option>
              <option value="maintain">유지</option>
              <option value="lose">감량</option>
              <option value="gain">증량</option>
            </select>
          </label>

          <button type="submit" className="signup-submit">
            가입하기
          </button>
        </form>
      </div>
    </div>
  );
}

export default Signup;
