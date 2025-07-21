import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import '../styles/pages/editProfile.css';

function EditProfile() {
  const [form, setForm] = useState({
    nickname: '',
    heightCm: '',
    weightKg: '',
    goal: '',
  });

  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const res = await api.get('/api/user/me');
        setForm({
          nickname: res.data.nickname || '',
          heightCm: res.data.heightCm || '',
          weightKg: res.data.weightKg || '',
          goal: res.data.goal?.toLowerCase() || '',
        });
      } catch (err) {
        console.error('유저 정보 조회 실패:', err);
      }
    };

    fetchUserInfo();
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.patch('/api/user/me', form);
      setMessage('정보 수정 완료!');
      setIsError(false);
      navigate('/mypage');
    } catch (err) {
      console.error('정보 수정 실패:', err);
      setMessage('정보 수정 실패');
      setIsError(true);
    }
  };

  return (
    <div className="edit-profile">
      <h2 className="edit-profile-title">정보 수정</h2>

      {message && (
        <div className={`message ${isError ? 'message-error' : 'message-success'}`}>
          {message}
        </div>
      )}

      <form onSubmit={handleSubmit} className="edit-profile-form">
        <label>
          닉네임:
          <input
            name="nickname"
            value={form.nickname}
            onChange={handleChange}
            placeholder="닉네임"
            type="text"
            className="form-input"
          />
        </label>

        <label>
          키 (cm):
          <input
            name="heightCm"
            type="number"
            value={form.heightCm}
            onChange={handleChange}
            placeholder="키"
            className="form-input"
          />
        </label>

        <label>
          몸무게 (kg):
          <input
            name="weightKg"
            type="number"
            value={form.weightKg}
            onChange={handleChange}
            placeholder="몸무게"
            className="form-input"
          />
        </label>

        <label>
          목표:
          <select
            name="goal"
            value={form.goal}
            onChange={handleChange}
            className="form-input"
          >
            <option value="">목표 선택</option>
            <option value="maintain">유지</option>
            <option value="lose">감량</option>
            <option value="gain">증량</option>
          </select>
        </label>

        <button type="submit" className="btn btn-primary edit-profile-submit">
          수정 완료
        </button>
      </form>
    </div>
  );
}

export default EditProfile;
