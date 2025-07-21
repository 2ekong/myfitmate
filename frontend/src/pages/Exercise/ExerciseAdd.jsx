import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api/axios';
import '../../styles/pages/exerciseAdd.css';
import '../../styles/components/ExerciseCard.css';

function ExerciseAdd() {
  const [query, setQuery] = useState('');
  const [allExercises, setAllExercises] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [selected, setSelected] = useState(null);
  const [duration, setDuration] = useState('');
  const [exerciseAt, setExerciseAt] = useState('');
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchExercises = async () => {
      try {
        const res = await api.get('/api/exercise');
        setAllExercises(res.data);
      } catch (err) {
        console.error('운동 목록 불러오기 실패:', err);
        setMessage('운동 목록 불러오기 실패');
        setIsError(true);
      }
    };
    fetchExercises();
  }, []);

  useEffect(() => {
    if (query.trim() === '') {
      setFiltered([]);
    } else {
      const matches = allExercises.filter(ex => ex.name.includes(query));
      setFiltered(matches);
    }
  }, [query, allExercises]);

  const handleSubmit = async () => {
    try {
      await api.post('/api/exercise/log', {
        exerciseId: selected.id,
        durationMinutes: parseFloat(duration),
        exerciseAt,
      });
      setMessage('운동 기록 완료!');
      setIsError(false);
      setIsSuccess(true);
    } catch (err) {
      console.error('운동 추가 실패:', err);
      setMessage('운동 추가 실패');
      setIsError(true);
      setIsSuccess(false);
    }
  };

  const bgGradients = [
    'linear-gradient(to bottom right, #fff9db, #f0fff4)',
    'linear-gradient(to bottom right, #e0f7fa, #fce4ec)',
    'linear-gradient(to bottom right, #f3e5f5, #e8f5e9)',
    'linear-gradient(to bottom right, #ffecb3, #ffe0b2)',
    'linear-gradient(to bottom right, #dcedc8, #f0f4c3)',
    'linear-gradient(to bottom right, #ffe082, #ffccbc)',
  ];

  return (
    <div className="exercise-add" style={{ padding: '20px' }}>
      {/* ✅ 제목 */}
      <section style={{ textAlign: 'center', marginBottom: '40px' }}>
        <h2 style={{ fontSize: '2rem', marginBottom: '8px' }}>운동 추가하기</h2>
        <p style={{ color: '#5f7d6b' }}>오늘의 활력을 기록해보세요!</p>
      </section>

      {/* ✅ 메시지 */}
      {message && (
        <div
          className={`message ${isError ? 'message-error' : 'message-success'}`}
          style={{
            textAlign: 'center',
            padding: '12px',
            marginBottom: '20px',
            borderRadius: '8px',
            color: isError ? '#c62828' : '#2e7d32',
            backgroundColor: isError ? '#ffebee' : '#e8f5e9',
          }}
        >
          {message}
        </div>
      )}

      {/* ✅ 인기 운동 + 입력폼 */}
      <div
        style={{
          display: 'grid',
          gap: '40px',
          justifyContent: 'center',
          alignItems: 'start',
        }}
      >
        {/* 인기 운동 카드 */}
        <div
          style={{
            width: '720px',
            padding: '24px',
            border: '1px solid #e0e0e0',
            borderRadius: '20px',
            backgroundColor: '#ffffff',
            boxShadow: '0 4px 12px rgba(0,0,0,0.04)',
          }}
        >
          <h3 style={{ fontSize: '1.3rem' }}>인기 운동</h3>
          <p style={{ color: '#888' }}>자주 하는 운동을 빠르게 선택하세요.</p>
          <div
            style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(160px, 1fr))',
              gap: '16px',
              marginTop: '16px',
            }}
          >
            {allExercises.slice(0, 6).map((ex, index) => (
              <div
                key={ex.id}
                onClick={() => {
                  setQuery(ex.name);
                  setSelected(ex);
                }}
                style={{
                  padding: '16px',
                  borderRadius: '12px',
                  background: bgGradients[index % bgGradients.length],
                  textAlign: 'center',
                  cursor: 'pointer',
                  boxShadow: '0 1px 4px rgba(0,0,0,0.05)',
                  border: '1px solid #ddd',
                }}
              >
                
                <div style={{ fontWeight: '600' }}>{ex.name}</div>
                <div style={{ fontSize: '0.9rem', color: '#f57f17', fontWeight: 'bold' }}>
                  {ex.kcalPerMinute || 5}kcal/분
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* 운동 정보 입력 카드 */}
        <div
          style={{
            width: '720px',
            padding: '24px',
            border: '1px solid #e0e0e0',
            borderRadius: '20px',
            backgroundColor: '#ffffff',
            boxShadow: '0 4px 12px rgba(0,0,0,0.04)',
          }}
        >
          <h3 style={{ fontSize: '1.3rem' }}>운동 정보 입력</h3>
          <p style={{ color: '#888' }}>운동의 세부 정보를 입력해주세요</p>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', marginTop: '16px' }}>
            <input
              type="text"
              value={query}
              onChange={e => {
                setQuery(e.target.value);
                setSelected(null);
              }}
              placeholder="예: 요가, 조깅, 홈트레이닝 등"
              style={{ padding: '10px', borderRadius: '8px', border: '1px solid #ccc' }}
            />
            {filtered.length > 0 && (
              <ul style={{ border: '1px solid #ccc', borderRadius: '4px', backgroundColor: '#fff' }}>
                {filtered.map(item => (
                  <li
                    key={item.id}
                    onClick={() => {
                      setQuery(item.name);
                      setSelected(item);
                      setFiltered([]);
                    }}
                    style={{ padding: '8px', cursor: 'pointer' }}
                  >
                    {item.name}
                  </li>
                ))}
              </ul>
            )}

            <input
              type="number"
              placeholder="운동 시간 (분)"
              value={duration}
              onChange={e => setDuration(e.target.value)}
              style={{ padding: '10px', borderRadius: '8px', border: '1px solid #ccc' }}
            />

            <input
              type="datetime-local"
              value={exerciseAt}
              onChange={e => setExerciseAt(e.target.value)}
              style={{ padding: '10px', borderRadius: '8px', border: '1px solid #ccc' }}
            />

            <button
              onClick={handleSubmit}
              disabled={!selected}
              style={{
                padding: '14px',
                background: '#f57f17',
                color: 'white',
                borderRadius: '8px',
                border: 'none',
                fontWeight: 'bold',
                cursor: 'pointer',
              }}
            >
              + 운동 추가하기
            </button>

            {isSuccess && (
              <button
                onClick={() => navigate('/exercise/log')}
                style={{
                  padding: '10px',
                  border: '1px solid #ccc',
                  borderRadius: '8px',
                  backgroundColor: '#ffffff',
                  cursor: 'pointer',
                }}
              >
                👉 운동 기록 보러가기
              </button>
            )}
          </div>
        </div>
      </div>

      {/* 운동 팁 섹션 */}
      <section
        style={{
          background: '#fff8e1',
          padding: '16px',
          borderRadius: '12px',
          textAlign: 'center',
          maxWidth: '700px',
          margin: '40px auto 0',
        }}
      >
        <p>💪 꾸준한 운동이 건강의 비결이에요! 작은 움직임부터 시작해서 점차 늘려가세요. 🌱✨</p>
      </section>
    </div>
);

}

export default ExerciseAdd;
