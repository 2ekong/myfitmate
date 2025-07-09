import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../../api/axios';
import '../../styles/pages/foodDetail.css';

function FoodDetailPage() {
  const { id } = useParams();
  const [food, setFood] = useState(null);
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);

  useEffect(() => {
    const fetchFood = async () => {
      try {
        const res = await api.get(`/api/foods/${id}`);
        setFood(res.data);
      } catch (err) {
        console.error('음식 정보 조회 실패:', err);
        setMessage('음식 정보를 불러오지 못했습니다.');
        setIsError(true);
      }
    };

    fetchFood();
  }, [id]);

  if (!food) return <p className="food-detail-loading">로딩 중...</p>;

  return (
    <div className="food-detail">
      <section className="meal-title">
        <h2>음식 상세 정보</h2>
        <p>선택한 음식의 영양 정보를 확인하세요.</p>
      </section>

      {message && (
        <div className={`message ${isError ? 'error' : 'success'}`}>{message}</div>
      )}

      <div className="card food-detail-card">
        <p><strong>음식명:</strong> {food.name}</p>
        <p><strong>제공량:</strong> {food.standardAmount} g</p>
        <p><strong>열량:</strong> {food.calories} kcal</p>
        <p><strong>탄수화물:</strong> {food.carbohydrate} g</p>
        <p><strong>단백질:</strong> {food.protein} g</p>
        <p><strong>지방:</strong> {food.fat} g</p>
        <p><strong>나트륨:</strong> {food.sodium} mg</p>
        <p><strong>출처:</strong> {food.referenceBasis}</p>
      </div>

      <div className="submit-section">
        <Link to="/foods">
          <button className="common-action-btn light">목록으로</button>
        </Link>
        <Link to={`/foods/edit/${food.id}`}>
          <button className="common-action-btn">수정하기</button>
        </Link>
      </div>
    </div>
  );
}

export default FoodDetailPage;
