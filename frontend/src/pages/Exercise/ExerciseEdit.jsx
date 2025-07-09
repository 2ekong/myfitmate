import { useLocation, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from '../../api/axios';
import '../../styles/pages/exerciseEdit.css';

function formatDateTimeLocal(dateStr) {
  const date = new Date(dateStr);
  const offset = date.getTimezoneOffset() * 60000;
  return new Date(date - offset).toISOString().slice(0, 16);
}

function ExerciseEdit() {
  const { state: log } = useLocation();
  const navigate = useNavigate();
  const accessToken = localStorage.getItem('accessToken');

  const [query, setQuery] = useState(log.exerciseName);
  const [selected, setSelected] = useState({
    id: log.exerciseId,
    name: log.exerciseName,
  });
  const [allExercises, setAllExercises] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [duration, setDuration] = useState(String(log.durationMinutes));
  const [exerciseAt, setExerciseAt] = useState(formatDateTimeLocal(log.exerciseAt));
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);

  useEffect(() => {
    const fetchExercises = async () => {
      try {
        const res = await axios.get('/api/exercise', {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });
        setAllExercises(res.data);
      } catch (err) {
        console.error('운동 목록 불러오기 실패:', err);
        setMessage('운동 목록을 불러오지 못했습니다.');
        setIsError(true);
      }
    };
    fetchExercises();
  }, [accessToken]);

  useEffect(() => {
    if (query.trim() === '') {
      setFiltered([]);
    } else {
      const matches = allExercises.filter((ex) =>
        ex.name.includes(query)
      );
      setFiltered(matches);
    }
  }, [query, allExercises]);

  const handleUpdate = async () => {
    if (!selected?.id) {
      setMessage('운동을 선택해주세요.');
      setIsError(true);
      return;
    }

    if (!duration || isNaN(duration) || parseFloat(duration) <= 0) {
      setMessage('운동 시간을 숫자로 입력해주세요.');
      setIsError(true);
      return;
    }

    if (!exerciseAt) {
      setMessage('운동 날짜/시간을 선택해주세요.');
      setIsError(true);
      return;
    }

    try {
      await axios.patch(
        `/api/exercise/log/${log.id}`,
        {
          exerciseId: selected.id,
          durationMinutes: parseFloat(duration),
          exerciseAt,
        },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      setMessage('운동 기록이 수정되었습니다.');
      setIsError(false);
      navigate('/exercise/log');
    } catch (err) {
      console.error('운동 수정 실패:', err);
      setMessage('운동 수정 실패');
      setIsError(true);
    }
  };

  return (
    <div className="exercise-edit" style={{ padding: '24px' }}>
      {/* 🏋️‍♀️ 상단 제목 */}
      <section style={{ textAlign: 'center', marginBottom: '24px' }}>
        <h2 style={{ fontSize: '1.8rem' }}>운동 기록 수정</h2>
      </section>

      {/* ✅ 메시지 출력 */}
      {message && (
        <div
          style={{
            textAlign: 'center',
            padding: '12px',
            marginBottom: '20px',
            borderRadius: '8px',
            color: isError ? '#c62828' : '#2e7d32',
            backgroundColor: isError ? '#ffebee' : '#e8f5e9'
          }}
        >
          {message}
        </div>
      )}

      {/* ✅ 흰 카드 박스 스타일 */}
      <div
        style={{
          maxWidth: '600px',
          margin: '0 auto',
          background: '#ffffff',
          borderRadius: '16px',
          boxShadow: '0 4px 12px rgba(0, 0, 0, 0.08)',
          padding: '24px',
          display: 'flex',
          flexDirection: 'column',
          gap: '16px'
        }}
      >
        <input
          type="text"
          value={query}
          onChange={(e) => {
            setQuery(e.target.value);
            if (e.target.value !== selected?.name) setSelected(null);
          }}
          placeholder="운동 이름 검색"
          className="form-input"
          style={{ padding: '10px', borderRadius: '8px', border: '1px solid #ccc' }}
        />

        {filtered.length > 0 && (
          <ul className="search-dropdown">
            {filtered.map((item) => (
              <li
                key={item.id}
                onClick={() => {
                  setQuery(item.name);
                  setSelected(item);
                  setFiltered([]);
                }}
                className="search-item"
              >
                {item.name}
              </li>
            ))}
          </ul>
        )}

        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          <input
            type="number"
            placeholder="운동 시간"
            value={duration}
            onChange={(e) => setDuration(e.target.value)}
            className="form-input"
            style={{
              padding: '10px',
              borderRadius: '8px',
              border: '1px solid #ccc',
              flex: '1'
            }}
          />
          <span style={{ fontSize: '1rem', color: '#555' }}>분</span>
        </div>


        <input
          type="datetime-local"
          value={exerciseAt}
          onChange={(e) => setExerciseAt(e.target.value)}
          className="form-input"
          style={{ padding: '10px', borderRadius: '8px', border: '1px solid #ccc' }}
        />

        {/* ✅ 리스트용 버튼 스타일로 수정 */}
        <button
          onClick={handleUpdate}
          style={{
            padding: '14px 0',
            width: '100%',
            borderRadius: '12px',
            background: '#fb8c00', // 주황색
            color: '#fff',
            border: 'none',
            fontWeight: 'bold',
            fontSize: '1rem',
            cursor: 'pointer',
            marginTop: '12px'
          }}
        >
          + 수정 완료
        </button>

      </div>
    </div>
  );

}

export default ExerciseEdit;
