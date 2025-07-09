import { useEffect, useState } from 'react';
import api from '../../api/axios';
import { useNavigate } from 'react-router-dom';
import '../../styles/pages/mealAdd.css';

function MealAddPage() {
  const navigate = useNavigate();

  const [eatTime, setEatTime] = useState('');
  const [mealType, setMealType] = useState('');
  const [myFoods, setMyFoods] = useState([]);
  const [csvFoods, setCsvFoods] = useState([]);
  const [selectedFoods, setSelectedFoods] = useState([]);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);
  const [existingMealTypes, setExistingMealTypes] = useState([]);
  const [imageFile, setImageFile] = useState(null);
  const [showAllCsv, setShowAllCsv] = useState(false);

  useEffect(() => {
    fetchMyFoods();
    fetchCsvFoods();
    fetchExistingMeals();
  }, []);

  const fetchMyFoods = async () => {
    try {
      const res = await api.get('/api/foods');
      setMyFoods(res.data);
    } catch (err) {
      console.error('내 음식 불러오기 실패:', err);
    }
  };

  const fetchCsvFoods = async () => {
    try {
      const res = await api.get('/api/foods/csv');
      setCsvFoods(res.data);
    } catch (err) {
      console.error('CSV 음식 불러오기 실패:', err);
    }
  };

  const fetchExistingMeals = async () => {
    try {
      const today = new Date().toISOString().split('T')[0];
      const res = await api.get('/api/meals', { params: { date: today } });
      const types = res.data.map((m) => m.mealType);
      setExistingMealTypes(types);
    } catch (err) {
      console.error('기존 식단 조회 실패:', err);
    }
  };

  const handleMealTypeChange = (e) => {
    const value = e.target.value;
    setMealType(value);

    if (existingMealTypes.includes(value)) {
      setMessage('이미 해당 식사 종류가 등록되어 있습니다.');
      setIsError(true);
    } else {
      setMessage('');
      setIsError(false);
    }
  };

  const handleAddFood = () => {
    setSelectedFoods([...selectedFoods, { foodId: '', quantity: '' }]);
  };

  const handleRemoveFood = (index) => {
    const updated = [...selectedFoods];
    updated.splice(index, 1);
    setSelectedFoods(updated);
  };

  const handleFoodChange = (index, field, value) => {
    const updated = [...selectedFoods];
    updated[index][field] = value;
    setSelectedFoods(updated);
  };

  const calculatedNutrition = selectedFoods.reduce((acc, item) => {
    const food = myFoods.find(f => f.id.toString() === item.foodId);
    if (!food || !item.quantity) return acc;
    const quantity = parseFloat(item.quantity);
    acc.calories += food.calories * quantity;
    acc.carbs += food.carbohydrate * quantity;
    acc.protein += food.protein * quantity;
    acc.fat += food.fat * quantity;
    return acc;
  }, { calories: 0, carbs: 0, protein: 0, fat: 0 });

  const handleSubmit = async (e) => {
    e.preventDefault();
    const validFoods = selectedFoods.filter(f => f.foodId && f.quantity);
    if (validFoods.length === 0) return;

    const dto = {
      eatTime,
      mealType,
      foodList: validFoods.map(f => ({
        foodId: parseInt(f.foodId),
        quantity: parseFloat(f.quantity)
      }))
    };

    const formData = new FormData();
    formData.append('dto', new Blob([JSON.stringify(dto)], { type: 'application/json' }));
    if (imageFile) {
      formData.append('image', imageFile);
    }

    try {
      await api.post('/api/meals', formData);
      setMessage('식단 등록 완료');
      setIsError(false);
      setIsSuccess(true);
    } catch (err) {
      const msg = err?.response?.data?.message || '식단 등록 실패';
      setMessage(msg);
      setIsError(true);
      setIsSuccess(false);
    }
  };

  const handleCsvFoodClick = async (f) => {
    if (!f || !f.name) return;

    try {
      await api.post('/api/foods', {
        name: f.name?.trim(),
        originCategory: f.originCategory || '',
        originSubCategory: f.originSubCategory || '',
        originDetailCategory: f.originDetailCategory || '',
        standardAmount: parseFloat(f.standardAmount),
        calories: parseFloat(f.calories),
        carbohydrate: parseFloat(f.carbohydrate),
        protein: parseFloat(f.protein),
        fat: parseFloat(f.fat),
        sodium: parseFloat(f.sodium),
        referenceBasis: 'CSV 기반 자동 등록',
      });

      const res = await api.get('/api/foods');
      setMyFoods(res.data);

      const newFood = res.data.find(food => food.name.trim() === f.name.trim());

      if (!newFood) {
        setMessage(`${f.name} 등록은 되었지만 선택에 실패했습니다.`);
        setIsError(true);
        return;
      }

      const exists = selectedFoods.some(item => item.foodId === newFood.id.toString());
      if (exists) return;

      setSelectedFoods(prev => [...prev, { foodId: newFood.id.toString(), quantity: '1' }]);
      setMessage(`${f.name} 등록 및 선택 완료`);
      setIsError(false);

    } catch (err) {
      if (err.response?.status === 409) {
        const already = myFoods.find(food => food.name.trim() === f.name.trim());
        if (already) {
          setSelectedFoods(prev => [...prev, { foodId: already.id.toString(), quantity: '1' }]);
          setMessage(`${f.name}은(는) 이미 등록된 음식입니다.`);
          setIsError(false);
        } else {
          setMessage(`${f.name}은(는) 이미 등록되었지만 선택할 수 없습니다.`);
          setIsError(true);
        }
      } else {
        setMessage(`${f.name} 등록 실패`);
        setIsError(true);
      }
    }
  };

  return (
    <div className="meal-add">
      <section className="meal-title">
        <h2>🍽️ 식단 등록</h2>
        <p>건강한 하루를 위한 한 끼를 기록해보세요!</p>
      </section>

      {message && <div className={`message ${isError ? 'error' : 'success'}`}>{message}</div>}

      <form onSubmit={handleSubmit} className="meal-form">
        <div className="card">
          <label>식사 시간</label>
          <input type="datetime-local" value={eatTime} onChange={(e) => setEatTime(e.target.value)} />
        </div>

        <div className="card">
          <label>식사 종류</label>
          <select value={mealType} onChange={handleMealTypeChange}>
            <option value="">선택</option>
            <option value="breakfast">아침</option>
            <option value="lunch">점심</option>
            <option value="dinner">저녁</option>
            <option value="snack">간식</option>
          </select>
        </div>

        <div className="card">
          <label>CSV 음식 목록</label>
          <input
            type="text"
            placeholder="음식 이름 검색"
            value={searchKeyword}
            onChange={(e) => setSearchKeyword(e.target.value)}
          />
          <ul className="csv-food-list">
            {csvFoods
              .filter(f => f.name?.toLowerCase().includes(searchKeyword.toLowerCase()))
              .slice(0, showAllCsv ? csvFoods.length : 10)
              .map((f, i) => (
                <li key={i} onClick={() => handleCsvFoodClick(f)}>
                  {f.name} ({f.calories} kcal)
                </li>
              ))}
          </ul>
          {csvFoods.length > 10 && (
            <div style={{
              display: 'flex',
              justifyContent: 'center',
              marginTop: '10px'
            }}>
              <button
                type="button"
                className="common-action-btn light"
                onClick={() => setShowAllCsv(!showAllCsv)}
              >
                {showAllCsv ? '접기' : '더 보기'}
              </button>
            </div>
          )}


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
                {myFoods.map((food) => (
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
                onClick={() => handleRemoveFood(index)}
              >
                삭제
              </button>
            </div>
          ))}

          <div style={{
            display: 'flex',
            justifyContent: 'center',  // ✅ 중앙 정렬
            marginTop: '10px'
          }}>
            <button
              type="button"
              className="food-add-btn"
              onClick={handleAddFood}
            >
              음식 추가
            </button>
          </div>

        </div>  {/* ✅ 이거를 빼먹었어서 구조가 망가졌던 거야 */}


        <div className="card">
          <label>🧾 탄단지 요약</label>
          <div className="nutrient-cards">
            <NutrientCard emoji="🍚" label="탄수화물" value={calculatedNutrition.carbs} />
            <NutrientCard emoji="🍗" label="단백질" value={calculatedNutrition.protein} />
            <NutrientCard emoji="🥑" label="지방" value={calculatedNutrition.fat} />
          </div>
        </div>

        <div className="card">
          <label>식단 이미지</label>
          <input type="file" accept="image/*" onChange={(e) => setImageFile(e.target.files[0])} />
        </div>

        <div className="submit-section">
          <button type="submit">+ 식단 등록하기</button>
          {isSuccess && (
            <button type="button" className="go-list-btn" onClick={() => navigate('/meals')}>
              👉 식단 목록 보러가기
            </button>
          )}
        </div>
      </form>
    </div>
  );
}

function NutrientCard({ emoji, label, value }) {
  return (
    <div className="nutrient-card">
      <div className="emoji">{emoji}</div>
      <div className="label">{label}</div>
      <div className="value">{value.toFixed(2)} g</div>
    </div>
  );
}

export default MealAddPage;
