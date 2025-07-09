import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../../api/axios';
import '../../styles/pages/mealEdit.css';

function MealEditPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [eatTime, setEatTime] = useState('');
  const [mealType, setMealType] = useState('');
  const [foods, setFoods] = useState([]);
  const [selectedFoods, setSelectedFoods] = useState([]);
  const [imageFile, setImageFile] = useState(null);
  const [previewImage, setPreviewImage] = useState(null);
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);

  useEffect(() => {
    const fetchMeal = async () => {
      try {
        const res = await api.get(`/api/meals/${id}`);
        const meal = res.data;
        setEatTime(meal.eatTime);
        setMealType(meal.mealType);
        setSelectedFoods(meal.foodList.map(f => ({
          foodId: f.foodId.toString(),
          foodName: f.foodName,
          quantity: f.quantity
        })));
        if (meal.imageUrl) {
          setPreviewImage(`http://localhost:8080${meal.imageUrl.startsWith('/') ? meal.imageUrl : '/' + meal.imageUrl}`);
        }
      } catch (err) {
        console.error('식단 조회 실패:', err);
        setMessage('식단 정보 불러오기 실패');
        setIsError(true);
      }
    };

    const fetchFoods = async () => {
      try {
        const res = await api.get('/api/foods');
        setFoods(res.data);
      } catch (err) {
        console.error('음식 목록 조회 실패:', err);
      }
    };

    fetchMeal();
    fetchFoods();
  }, [id]);

  const handleFoodChange = (index, field, value) => {
    const newList = [...selectedFoods];
    newList[index][field] = value;

    if (field === 'foodId') {
      const selected = foods.find(f => f.id.toString() === value);
      newList[index]['foodName'] = selected ? selected.name : '';
    }

    setSelectedFoods(newList);
  };

  const addFoodField = () => {
    setSelectedFoods([...selectedFoods, { foodId: '', foodName: '', quantity: '' }]);
  };

  const removeFoodField = (index) => {
    const newList = selectedFoods.filter((_, i) => i !== index);
    setSelectedFoods(newList);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const validFoodList = selectedFoods
      .filter(f => f.foodId && f.quantity)
      .map(f => ({
        foodId: parseInt(f.foodId),
        quantity: parseFloat(f.quantity)
      }));

    const dto = {
      eatTime,
      mealType,
      foodList: validFoodList
    };

    const formData = new FormData();
    formData.append('dto', new Blob([JSON.stringify(dto)], { type: 'application/json' }));
    if (imageFile) {
      formData.append('image', imageFile);
    }

    try {
      await api.put(`/api/meals/${id}`, formData);
      setMessage('수정 완료!');
      setIsError(false);
      navigate(`/meals/${id}`);
    } catch (err) {
      console.error('수정 실패:', err);
      setMessage('수정 실패');
      setIsError(true);
    }
  };

  const calculatedNutrition = selectedFoods.reduce((acc, item) => {
    const food = foods.find(f => f.id.toString() === item.foodId);
    if (!food || !item.quantity) return acc;
    const quantity = parseFloat(item.quantity);
    acc.calories += food.calories * quantity;
    acc.carbs += food.carbohydrate * quantity;
    acc.protein += food.protein * quantity;
    acc.fat += food.fat * quantity;
    return acc;
  }, { calories: 0, carbs: 0, protein: 0, fat: 0 });

  return (
    <div className="meal-edit">
      <h2 className="meal-edit-title">식단 수정</h2>

      {message && (
        <div className={`meal-edit-message ${isError ? 'error' : 'success'}`}>{message}</div>
      )}

      <form onSubmit={handleSubmit} className="meal-edit-form">
        <div className="card">
          <label>식사 시간</label>
          <input type="datetime-local" value={eatTime} onChange={(e) => setEatTime(e.target.value)} />
        </div>

        <div className="card">
          <label>식사 종류</label>
          <select value={mealType} onChange={(e) => setMealType(e.target.value)}>
            <option value="">선택</option>
            <option value="breakfast">아침</option>
            <option value="lunch">점심</option>
            <option value="dinner">저녁</option>
            <option value="snack">간식</option>
          </select>
        </div>

        <div className="card">
          <label>음식 선택</label>
          {selectedFoods.map((item, index) => (
            <div key={index} className="food-select-row">
              <select
                value={item.foodId}
                onChange={(e) => handleFoodChange(index, 'foodId', e.target.value)}
                className="food-select"
              >
                <option value="">음식 선택</option>
                {foods.map((food) => (
                  <option key={food.id} value={food.id}>{food.name}</option>
                ))}
              </select>
              <input
                type="number"
                step="0.1"
                placeholder="수량"
                value={item.quantity}
                onChange={(e) => handleFoodChange(index, 'quantity', e.target.value)}
                className="food-input"
              />
              <button
                type="button"
                className="food-delete-btn"
                onClick={() => removeFoodField(index)}
              >
                삭제
              </button>
            </div>
          ))}
          <button
            type="button"
            className="food-add-btn"
            onClick={addFoodField}
          >
            음식 추가
          </button>
        </div>

        <div className="card">
          <label>탄단지 요약</label>
          <div className="nutrient-cards">
            <div className="nutrient-card">
              <div className="emoji">🍚</div>
              <div className="label">탄수화물</div>
              <div className="value">{calculatedNutrition.carbs.toFixed(2)} g</div>
            </div>
            <div className="nutrient-card">
              <div className="emoji">🍗</div>
              <div className="label">단백질</div>
              <div className="value">{calculatedNutrition.protein.toFixed(2)} g</div>
            </div>
            <div className="nutrient-card">
              <div className="emoji">🥑</div>
              <div className="label">지방</div>
              <div className="value">{calculatedNutrition.fat.toFixed(2)} g</div>
            </div>
          </div>
        </div>

        <div className="card">
          <label>식단 이미지</label>
          {previewImage && (
            <div className="meal-edit-preview">
              <img src={previewImage} alt="식단 이미지 미리보기" />
            </div>
          )}
          <input
            type="file"
            accept="image/*"
            onChange={(e) => {
              const file = e.target.files[0];
              setImageFile(file);
              if (file) setPreviewImage(URL.createObjectURL(file));
            }}
          />
        </div>

        <div className="submit-section">
          <button type="submit">수정 완료</button>
          <button type="button" className="go-list-btn" onClick={() => navigate('/meals')}>
            목록으로 돌아가기
          </button>
        </div>
      </form>
    </div>
  );
}

export default MealEditPage;
