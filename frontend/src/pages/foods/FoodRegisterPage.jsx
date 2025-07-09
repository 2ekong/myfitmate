import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import api from '../../api/axios';
import '../../styles/pages/foodRegister.css';

function FoodRegisterPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const csvFood = location.state?.csvFood;

  const [form, setForm] = useState({
    name: csvFood?.name || '',
    originCategory: csvFood?.originCategory || '',
    originSubCategory: csvFood?.originSubCategory || '',
    originDetailCategory: csvFood?.originDetailCategory || '',
    standardAmount: parseFloat(csvFood?.standardAmount) || '',
    calories: parseFloat(csvFood?.calories) || '',
    carbohydrate: parseFloat(csvFood?.carbohydrate) || '',
    protein: parseFloat(csvFood?.protein) || '',
    fat: parseFloat(csvFood?.fat) || '',
    sodium: parseFloat(csvFood?.sodium) || '',
    referenceBasis: 'CSV 기반 자동 등록',
  });

  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/api/foods', form);
      setMessage('음식 등록 완료');
      setIsError(false);
      setIsSuccess(true);
    } catch (err) {
      if (err.response?.data?.message === '이미 등록된 음식입니다.') {
        setMessage('이미 등록된 음식입니다.');
        setIsError(false);
      } else if (err.isAuthFailure) {
        setMessage('인증이 만료되었습니다. 다시 로그인해주세요.');
        setIsError(true);
      } else {
        setMessage('음식 등록 실패');
        setIsError(true);
      }
      console.error('등록 실패:', err);
    }
  };

  return (
    <div className="food-register">
      <section className="meal-title">
        <h2>음식 등록</h2>
        <p>새로운 음식을 등록해보세요. CSV 데이터도 자동으로 반영됩니다.</p>
      </section>

      <form onSubmit={handleSubmit} className="food-register-form">
        <div className="card">
          <label>음식명</label>
          <input name="name" value={form.name} onChange={handleChange} required />
        </div>

        <div className="card">
          <label>대분류</label>
          <input name="originCategory" value={form.originCategory} onChange={handleChange} />
        </div>

        <div className="card">
          <label>중분류</label>
          <input name="originSubCategory" value={form.originSubCategory} onChange={handleChange} />
        </div>

        <div className="card">
          <label>소분류</label>
          <input name="originDetailCategory" value={form.originDetailCategory} onChange={handleChange} />
        </div>

        <div className="card">
          <label>1회 제공량 (g)</label>
          <input name="standardAmount" type="number" step="0.1" value={form.standardAmount} onChange={handleChange} required />
        </div>

        <div className="card">
          <label>칼로리 (kcal)</label>
          <input name="calories" type="number" step="0.1" value={form.calories} onChange={handleChange} required />
        </div>

        <div className="card">
          <label>탄수화물 (g)</label>
          <input name="carbohydrate" type="number" step="0.1" value={form.carbohydrate} onChange={handleChange} />
        </div>

        <div className="card">
          <label>단백질 (g)</label>
          <input name="protein" type="number" step="0.1" value={form.protein} onChange={handleChange} />
        </div>

        <div className="card">
          <label>지방 (g)</label>
          <input name="fat" type="number" step="0.1" value={form.fat} onChange={handleChange} />
        </div>

        <div className="card">
          <label>나트륨 (mg)</label>
          <input name="sodium" type="number" step="0.1" value={form.sodium} onChange={handleChange} />
        </div>

        <div className="submit-section">
          <button type="submit" className="common-action-btn">+ 음식 등록하기</button>

          {isSuccess && (
            <button type="button" className="go-list-btn" onClick={() => navigate('/foods')}>
              👉 음식 목록 보러가기
            </button>
          )}
        </div>
      </form>

      {message && (
        <div className={`message ${isError ? 'error' : 'success'}`}>{message}</div>
      )}
    </div>
  );
}

export default FoodRegisterPage;
