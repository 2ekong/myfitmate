import { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import api from '../../api/axios';
import '../../styles/pages/mealDetail.css';

function MealDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [meal, setMeal] = useState(null);
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);

  // 식사 종류 매핑
  const mealTypeMap = {
    breakfast: '아침',
    lunch: '점심',
    dinner: '저녁',
    snack: '간식',
  };

  // 날짜 포맷 변환 함수
  const formatDateTime = (datetimeStr) => {
    const date = new Date(datetimeStr);
    return date.toLocaleString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  useEffect(() => {
    const fetchMeal = async () => {
      try {
        const res = await api.get(`/api/meals/${id}`);
        setMeal(res.data);
      } catch (err) {
        console.error('식단 상세 조회 실패:', err);
        setMessage('식단 상세 조회 실패');
        setIsError(true);
        navigate('/meals');
      }
    };

    fetchMeal();
  }, [id, navigate]);

  const handleDelete = async () => {
    if (!window.confirm('삭제하시겠습니까?')) return;
    try {
      await api.delete(`/api/meals/${id}`);
      setMessage('삭제 완료');
      setIsError(false);
      navigate('/meals');
    } catch (err) {
      console.error('삭제 실패:', err);
      setMessage('삭제 실패');
      setIsError(true);
    }
  };

  if (!meal) return <div className="meal-detail-loading">로딩 중...</div>;

  const { mealType, eatTime, imageUrl, foodList } = meal;
  const imageBaseUrl = 'http://localhost:8080';

  const calculatedTotalCalories = foodList?.reduce((sum, item) => {
    return sum + (item.calories * item.quantity);
  }, 0);

  const nutrition = foodList?.reduce((acc, item) => {
    acc.carbs += item.carbohydrate * item.quantity;
    acc.protein += item.protein * item.quantity;
    acc.fat += item.fat * item.quantity;
    return acc;
  }, { carbs: 0, protein: 0, fat: 0 });

  return (
    <div className="meal-detail">
      <section className="meal-title card">
        <h2>식단 상세 보기</h2>
        <p>등록된 식단의 세부 내용을 확인해보세요.</p>
      </section>

      {message && (
        <div className={`message ${isError ? 'error' : 'success'}`}>{message}</div>
      )}

      <div className="card">
        <label>식사 시간</label>
        <p>{formatDateTime(eatTime)}</p>
      </div>

      <div className="card">
        <label>식사 종류</label>
        <p>{mealTypeMap[mealType] || mealType}</p>
      </div>

      <div className="card">
        <label>총 칼로리</label>
        <p>{calculatedTotalCalories?.toFixed(2)} kcal</p>
      </div>

      <div className="card">
        <label>탄단지 요약</label>
        <div className="nutrient-cards">
          <NutrientCard label="탄수화물" value={nutrition.carbs} />
          <NutrientCard label="단백질" value={nutrition.protein} />
          <NutrientCard label="지방" value={nutrition.fat} />
        </div>
      </div>

      {imageUrl && (
        <div className="card meal-detail-image">
          <img src={imageBaseUrl + imageUrl} alt="식단 이미지" />
        </div>
      )}

      <div className="card">
        <h4>음식 목록</h4>
        <ul className="meal-detail-food-list">
          {foodList.map((item, idx) => (
            <li key={idx}>
              <strong>{item.foodName}</strong> - 수량: {item.quantity}, 칼로리: {item.calories} kcal
            </li>
          ))}
        </ul>
        <p className="meal-detail-help">
          음식 정보가 잘못됐나요? <Link to="/foods">음식 수정하러 가기</Link>
        </p>
      </div>

      <div className="meal-detail-actions">
        <button className="common-action-btn" onClick={() => navigate(`/meals/edit/${id}`)}>수정</button>
        <button className="common-action-btn light" onClick={handleDelete}>삭제</button>
        <button className="go-list-btn" onClick={() => navigate('/meals')}>목록으로</button>
      </div>
    </div>
  );
}

function NutrientCard({ label, value }) {
  return (
    <div className="nutrient-card">
      <div className="label">{label}</div>
      <div className="value">{value.toFixed(2)} g</div>
    </div>
  );
}

export default MealDetailPage;
