import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api/axios';
import '../../styles/pages/foodCsvSearch.css';

function FoodCsvSearchPage() {
  const [csvFoods, setCsvFoods] = useState([]);
  const [keyword, setKeyword] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);
  const [disabledFoodNames, setDisabledFoodNames] = useState([]);
  const navigate = useNavigate();

  const fetchFoods = async () => {
    setLoading(true);
    setMessage('');
    setIsSuccess(false);
    try {
      const res = await api.get('/api/foods/search', {
        params: { keyword },
      });
      setCsvFoods(res.data);
    } catch (err) {
      console.error('CSV 음식 검색 실패:', err);
      setMessage('검색 실패');
      setIsError(true);
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (food) => {
    try {
      const dto = {
        name: food.name?.trim(),
        originCategory: food.originCategory?.trim() || '',
        originSubCategory: food.originSubCategory?.trim() || '',
        originDetailCategory: food.originDetailCategory?.trim() || '',
        standardAmount: parseFloat(food.standardAmount),
        calories: parseFloat(food.calories),
        carbohydrate: parseFloat(food.carbohydrate),
        protein: parseFloat(food.protein),
        fat: parseFloat(food.fat),
        sodium: parseFloat(food.sodium),
        referenceBasis: 'CSV 기반 자동 등록',
      };

      await api.post('/api/foods', dto);
      setMessage(`${dto.name} 등록 완료`);
      setIsError(false);
      setIsSuccess(true);
      setDisabledFoodNames(prev => [...prev, dto.name]);
    } catch (err) {
      const status = err.response?.status;
      const msg = err.response?.data?.message;

      if (status === 409 && msg?.includes('이미 등록된 음식')) {
        setMessage(`${food.name}은(는) 이미 등록된 음식입니다.`);
        setIsError(false);
        setIsSuccess(false);
        setDisabledFoodNames(prev => [...prev, food.name]);
      } else if (status === 403) {
        setMessage('로그인이 만료되었습니다. 다시 로그인해주세요.');
        setIsError(true);
        setIsSuccess(false);
        setTimeout(() => navigate('/login'), 1500);
      } else {
        setMessage('등록 실패');
        setIsError(true);
        setIsSuccess(false);
      }

      console.error('등록 실패:', err);
    }
  };

  useEffect(() => {
    fetchFoods();
  }, []);

  return (
    <div className="csv-search">
      <section className="meal-title">
        <h2>CSV 음식 검색 및 등록</h2>
        <p>데이터 기반 음식 목록에서 선택하여 바로 등록해보세요.</p>
      </section>

      {message && (
        <div className={`message ${isError ? 'error' : 'success'}`}>{message}</div>
      )}

      <div className="csv-search-bar">
        <input
          type="text"
          value={keyword}
          placeholder="음식 이름으로 검색"
          onChange={(e) => setKeyword(e.target.value)}
        />
        <button className="common-action-btn light" onClick={fetchFoods}>검색</button>
      </div>

      {loading ? (
        <p className="csv-loading">로딩 중...</p>
      ) : csvFoods.length === 0 ? (
        <p className="csv-empty">검색 결과가 없습니다.</p>
      ) : (
        <ul className="csv-search-list">
          {csvFoods.map((food, idx) => {
            const isDisabled = disabledFoodNames.includes(food.name);
            return (
              <li key={idx} className="csv-search-item">
                <span className="csv-food-text">
                  <strong>{food.name}</strong> - {food.calories} kcal / {food.standardAmount}g
                </span>
                <button
                  className={`common-action-btn ${isDisabled ? 'gray' : 'light'}`}
                  disabled={isDisabled}
                  onClick={() => handleRegister(food)}
                >
                  {isDisabled ? '이미 등록됨' : '등록'}
                </button>
              </li>
            );
          })}
        </ul>
      )}

      {isSuccess && (
        <div className="submit-section">
          <button
            type="button"
            className="go-list-btn"
            onClick={() => navigate('/foods')}
          >
            👉 음식 목록 보러가기
          </button>
        </div>
      )}
    </div>
  );
}

export default FoodCsvSearchPage;
