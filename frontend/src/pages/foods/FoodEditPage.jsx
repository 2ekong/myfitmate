import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../../api/axios';
import '../../styles/pages/foodEdit.css';

function FoodEditPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: '',
    originCategory: '',
    originSubCategory: '',
    originDetailCategory: '',
    standardAmount: '',
    calories: '',
    carbohydrate: '',
    protein: '',
    fat: '',
    sodium: '',
    referenceBasis: '',
  });

  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);

  useEffect(() => {
    const fetchFood = async () => {
      try {
        const res = await api.get(`/api/foods/${id}`);
        const data = res.data;
        setForm({
          name: data.name || '',
          originCategory: data.originCategory || '',
          originSubCategory: data.originSubCategory || '',
          originDetailCategory: data.originDetailCategory || '',
          standardAmount: data.standardAmount || '',
          calories: data.calories || '',
          carbohydrate: data.carbohydrate || '',
          protein: data.protein || '',
          fat: data.fat || '',
          sodium: data.sodium || '',
          referenceBasis: data.referenceBasis || '',
        });
      } catch (err) {
        console.error('음식 정보 불러오기 실패:', err);
        setMessage('음식 정보를 불러오지 못했습니다.');
        setIsError(true);
      }
    };

    fetchFood();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.put(`/api/foods/${id}`, form);
      setMessage('음식 수정 완료');
      setIsError(false);
      setIsSuccess(true);
    } catch (err) {
      if (err.isAuthFailure) {
        setMessage('인증이 만료되었습니다. 다시 로그인해주세요.');
      } else {
        setMessage('음식 수정 실패');
      }
      console.error('수정 실패:', err);
      setIsError(true);
      setIsSuccess(false);
    }
  };

  return (
    <div className="food-edit">
      <section className="meal-title">
        <h2>음식 수정</h2>
        <p>선택한 음식 정보를 수정하세요.</p>
      </section>

      <form onSubmit={handleSubmit} className="food-edit-form">
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
          <button type="submit" className="common-action-btn">수정 완료</button>
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

export default FoodEditPage;
