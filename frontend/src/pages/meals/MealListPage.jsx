import { useEffect, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../../api/axios';
import '../../styles/pages/mealList.css';

function MealListPage() {
  const [meals, setMeals] = useState([]);
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);
  const [selectedDate, setSelectedDate] = useState(() => {
    const today = new Date();
    return today.toISOString().split('T')[0];
  });

  const navigate = useNavigate();

  useEffect(() => {
    const fetchMeals = async () => {
      try {
        const res = await api.get('/api/meals', {
          params: { date: selectedDate }
        });
        setMeals(res.data);
        setMessage('');
      } catch (err) {
        console.error('식단 목록 조회 실패:', err);
        setMessage('식단 목록 조회 실패');
        setIsError(true);
      }
    };

    fetchMeals();
  }, [selectedDate]);

  const handleDelete = async (mealId) => {
    if (!window.confirm('삭제하시겠습니까?')) return;

    try {
      await api.delete(`/api/meals/${mealId}`);
      setMeals(meals.filter((meal) => meal.id !== mealId));
      setMessage('삭제 완료');
      setIsError(false);
    } catch (err) {
      console.error('삭제 실패:', err);
      setMessage('삭제 실패');
      setIsError(true);
    }
  };

  const formatTime = (isoTime) => {
    const date = new Date(isoTime);
    const options = {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: true,
    };
    return new Intl.DateTimeFormat('ko-KR', options).format(date);
  };

  const translateMealType = (type) => {
    switch (type) {
      case 'breakfast':
        return '아침';
      case 'lunch':
        return '점심';
      case 'dinner':
        return '저녁';
      case 'snack':
        return '간식';
      default:
        return type;
    }
  };

  const calculateNutrition = (meal) => {
    return meal.foodList?.reduce(
      (acc, item) => {
        acc.calories += item.calories * item.quantity;
        acc.carbs += item.carbohydrate * item.quantity;
        acc.protein += item.protein * item.quantity;
        acc.fat += item.fat * item.quantity;
        return acc;
      },
      { calories: 0, carbs: 0, protein: 0, fat: 0 }
    ) || { calories: 0, carbs: 0, protein: 0, fat: 0 };
  };

  return (
    <div className="meal-list">
      <h2 className="meal-list-title">{selectedDate} 식단 목록</h2>

      {message && (
        <div className={`meal-list-message ${isError ? 'error' : 'success'}`}>{message}</div>
      )}

      <div className="meal-list-date-picker">
        <label>
          날짜 선택: <input type="date" value={selectedDate} onChange={(e) => setSelectedDate(e.target.value)} />
        </label>
      </div>

      <div className="meal-list-add-button">
        <button onClick={() => navigate('/foods')}>음식 정보 상세보기</button>
      </div>

      <ul className="meal-list-items">
        {meals.length === 0 ? (
          <li className="meal-list-empty">식단이 없습니다.</li>
        ) : (
          meals.map((meal) => {
            const nutrition = calculateNutrition(meal);

            return (
              <li key={meal.id} className="meal-list-item" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <div>
                  <Link to={`/meals/${meal.id}`} className="meal-list-link">
                    [{translateMealType(meal.mealType)}] {formatTime(meal.eatTime)}
                  </Link>

                  {meal.foodList && meal.foodList.length > 0 && (
                    <div className="meal-list-foods">
                      포함된 음식: {meal.foodList.map((item, index) => (
                        <span key={index}>{item.foodName}{index < meal.foodList.length - 1 ? ', ' : ''}</span>
                      ))}
                    </div>
                  )}

                  <div className="meal-list-calories">
                    총 칼로리: <strong>{nutrition.calories.toFixed(2)} kcal</strong><br />
                    탄수화물: {nutrition.carbs.toFixed(2)}g / 단백질: {nutrition.protein.toFixed(2)}g / 지방: {nutrition.fat.toFixed(2)}g
                  </div>
                </div>

                {/* ✅ 오른쪽 정렬된 수정/삭제 */}
                <div className="meal-list-actions" style={{ marginLeft: 'auto' }}>
                  <button className="edit-btn" onClick={() => navigate(`/meals/edit/${meal.id}`)}>수정</button>
                  <button className="delete-btn" onClick={() => handleDelete(meal.id)}>삭제</button>
                </div>
              </li>
            );
          })
        )}
      </ul>
    </div>
  );
}

export default MealListPage;
