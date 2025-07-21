import { useEffect, useState } from 'react';
import api from '../../api/axios';
import { Link } from 'react-router-dom';
import '../../styles/pages/foodList.css';

function FoodListPage() {
  const [foods, setFoods] = useState([]);
  const [keyword, setKeyword] = useState('');
  const [filteredFoods, setFilteredFoods] = useState([]);
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);

  useEffect(() => {
    fetchFoods();
  }, []);

  useEffect(() => {
    const lower = keyword.toLowerCase();
    setFilteredFoods(
      foods.filter((f) => f.name.toLowerCase().includes(lower))
    );
  }, [keyword, foods]);

  const fetchFoods = async () => {
    try {
      const res = await api.get('/api/foods');
      setFoods(res.data);
    } catch (err) {
      console.error('음식 불러오기 실패:', err);
      setMessage('음식 목록을 불러오지 못했습니다.');
      setIsError(true);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('정말로 이 음식을 삭제하시겠습니까?')) return;

    try {
      await api.delete(`/api/foods/${id}`);
      setFoods((prev) => prev.filter((food) => food.id !== id));
      setMessage('삭제 완료');
      setIsError(false);
    } catch (err) {
      console.error('삭제 실패:', err);
      setMessage('삭제 실패');
      setIsError(true);
    }
  };

  return (
    <div className="food-list">
      <div className="meal-title header-with-button">
        <h2>내가 등록한 음식</h2>
        <Link to="/foods/register">
          <button className="common-action-btn">+ 음식 등록하기</button>
        </Link>
      </div>

      {message && (
        <div className={`message ${isError ? 'error' : 'success'}`}>{message}</div>
      )}

      <div className="food-list-search">
        <input
          type="text"
          placeholder="음식 이름으로 검색"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <Link to="/foods/csv">
          <button className="common-action-btn light">CSV 목록</button>
        </Link>
      </div>

      {filteredFoods.length === 0 ? (
        <p className="food-list-empty">등록된 음식이 없습니다.</p>
      ) : (
        <ul className="food-list-items">
          {filteredFoods.map((food) => (
            <li key={food.id} className="food-list-item card">
              <div className="food-list-content">
                <Link to={`/foods/${food.id}`} className="food-list-name">
                  <strong>{food.name}</strong>
                </Link>
                <div className="food-list-nutrition">
                  <span>열량: {food.calories} kcal / 제공량: {food.standardAmount}g</span><br />
                  <span>탄수화물: {food.carbohydrate}g, 단백질: {food.protein}g, 지방: {food.fat}g, 나트륨: {food.sodium}mg</span>
                </div>
              </div>
              <div className="food-list-actions">
                <Link to={`/foods/edit/${food.id}`}>
                  <button className="action-btn green">수정</button>
                </Link>
                <button
                  className="action-btn yellow"
                  onClick={() => handleDelete(food.id)}
                >
                  삭제
                </button>
              </div>


            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default FoodListPage;
