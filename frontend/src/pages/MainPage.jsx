import { useEffect, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';
import {
  PieChart, Pie, Cell,
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
  ResponsiveContainer
} from 'recharts';
import '../styles/pages/mainPage.css';

function MainPage() {
  const [nutrientData, setNutrientData] = useState(null);
  const [weeklyData, setWeeklyData] = useState(null);
  const accessToken = localStorage.getItem('accessToken');

  useEffect(() => {
    if (!accessToken) return;

    const fetchData = async () => {
      try {
        const [nutrientRes, weeklyRes] = await Promise.all([
          axios.get('http://localhost:8080/api/statistics/nutrients/today', {
            headers: { Authorization: `Bearer ${accessToken}` }
          }),
          axios.get('http://localhost:8080/api/statistics/weekly', {
            headers: { Authorization: `Bearer ${accessToken}` }
          })
        ]);

        setNutrientData(nutrientRes.data);

        const { labels, mealCalories, exerciseCalories } = weeklyRes.data;
        const dailyCalories = labels.map((date, idx) => ({
          date,
          totalMealCalories: mealCalories[idx],
          totalExerciseCalories: exerciseCalories[idx]
        }));
        setWeeklyData({ dailyCalories });
      } catch (err) {
        console.error('요약 통계 불러오기 실패:', err);
      }
    };

    fetchData();
  }, [accessToken]);

  const suggestionCards = [
    { icon: '🍽️', title: '운동을 기록해 보세요', to: '/exercise/add' },
    { icon: '🏋️‍♀️', title: '식단을 추가해 보세요', to: '/meals/add' },
    { icon: '📊', title: '나의 통계를 확인해 보세요', to: '/mypage' },
    { icon: '🧠', title: 'AI와 상담해 보세요', to: '/ai-coach' },
  ];

  return (
    <div className="main-page">
      <section className="main-greeting">
        <h1>오늘도 건강하게!</h1>
        <p>자연스러운 건강 관리, 함께 해요</p>
      </section>

      <section className="suggestion-cards">
        {suggestionCards.map((item, idx) => (
          <Link to={item.to} key={idx} className="suggestion-card">
            <div className="icon">{item.icon}</div>
            <div className="text">{item.title}</div>
          </Link>
        ))}
      </section>

      <section className="summary-section">
        <div className="summary-card">
          <h3>오늘의 영양소 밸런스</h3>
          {nutrientData ? (
            <>
              <PieChart width={300} height={250}>
                <Pie
                  dataKey="value"
                  isAnimationActive={true}
                  data={[
                    { name: '단백질', value: nutrientData.protein },
                    { name: '탄수화물', value: nutrientData.carbohydrate },
                    { name: '지방', value: nutrientData.fat },
                  ]}
                  cx="50%"
                  cy="50%"
                  outerRadius={80}
                  labelLine={false}
                  label={false} // ✅ 내부 label 제거
                >
                  <Cell fill="#3E593C" />
                  <Cell fill="#B2D38D" />
                  <Cell fill="#8B4513" />
                </Pie>
                <Tooltip formatter={(value) => [`${Math.round(value)}g`, '섭취량']} />
              </PieChart>

              {/* ✅ 하단에 항목별 텍스트 + 네모 컬러 */}
<div style={{ marginTop: '12px', textAlign: 'center', fontSize: '0.9rem' }}>
  <div style={{ display: 'flex', justifyContent: 'center', gap: '12px' }}>
    <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
      <span style={{ width: '12px', height: '12px', backgroundColor: '#3E593C' }}></span>
      <span>단백질: {Math.round(nutrientData.protein)}g</span>
    </span>
    <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
      <span style={{ width: '12px', height: '12px', backgroundColor: '#B2D38D' }}></span>
      <span>탄수화물: {Math.round(nutrientData.carbohydrate)}g</span>
    </span>
    <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
      <span style={{ width: '12px', height: '12px', backgroundColor: '#8B4513' }}></span>
      <span>지방: {Math.round(nutrientData.fat)}g</span>
    </span>
  </div>
</div>

            </>
          ) : <p>기록이 없습니다</p>}
        </div>

        <div className="summary-card">
          <h3>이번 주 칼로리 트렌드</h3>
          {weeklyData ? (
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={weeklyData.dailyCalories}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="totalMealCalories" name="섭취 칼로리" fill="#8B4513" />
                <Bar dataKey="totalExerciseCalories" name="소모 칼로리" fill="#3E593C" />
              </BarChart>
            </ResponsiveContainer>
          ) : <p>기록이 없습니다</p>}
        </div>
      </section>

      <section className="section-ai-tip">
        <div className="ai-tip-box">
          <span className="ai-tip-title">💡 오늘의 건강 팁</span>
          <p>아보카도는 건강한 지방과 섬유질이 풍부해요! 하루 반 개 정도 섭취하면 심장 건강에 도움이 됩니다. 🥑✨</p>
          <Link to="/ai-coach">
            <button className="btn btn-primary">AI 건강 코치에게 물어보기</button>
          </Link>
        </div>
      </section>
    </div>
  );
}

export default MainPage;
