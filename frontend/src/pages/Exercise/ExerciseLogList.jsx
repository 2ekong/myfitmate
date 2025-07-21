import React, { useState, useEffect } from 'react';
import axios from '../../api/axios';
import { useNavigate } from 'react-router-dom';
import '../../styles/pages/exerciseLogList.css';

function formatKoreanDateTime(isoString) {
  const date = new Date(isoString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');

  let hours = date.getHours();
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const period = hours < 12 ? '오전' : '오후';

  if (hours === 0) hours = 12;
  else if (hours > 12) hours -= 12;

  return `${year}-${month}-${day} ${period} ${String(hours).padStart(2, '0')}:${minutes}`;
}

const ExerciseLogList = () => {
  const [date, setDate] = useState(() => {
    const today = new Date();
    return today.toISOString().split('T')[0];
  });
  const [logs, setLogs] = useState([]);
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);

  const accessToken = localStorage.getItem('accessToken');
  const navigate = useNavigate();

  useEffect(() => {
    if (!accessToken) return;

    const fetchLogs = async () => {
      try {
        const response = await axios.get('/api/exercise/log', {
          params: { date },
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });
        setLogs(response.data);
      } catch (error) {
        console.error('운동 기록 조회 실패', error);
        setMessage('운동 기록을 불러오는 데 실패했습니다.');
        setIsError(true);
      }
    };

    fetchLogs();
  }, [date, accessToken]);

  const handleDelete = async (logId) => {
    try {
      await axios.delete(`/api/exercise/log/${logId}`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      setLogs((prev) => prev.filter((log) => log.id !== logId));
      setMessage('운동 기록이 삭제되었습니다.');
      setIsError(false);
    } catch (err) {
      console.error('운동 삭제 실패', err);
      setMessage('운동 삭제 실패');
      setIsError(true);
    }
  };

  const handleEdit = (log) => {
    navigate(`/exercise/edit/${log.id}`, { state: log });
  };

  return (
    <div className="exercise-log-list" style={{ padding: '24px' }}>
      <section style={{ textAlign: 'center', marginBottom: '24px' }}>
        <h2>운동 기록</h2>
        <input
          type="date"
          value={date}
          onChange={(e) => setDate(e.target.value)}
          className="form-input"
          style={{ marginTop: '12px', padding: '8px', borderRadius: '8px', border: '1px solid #ccc' }}
        />
      </section>

      {message && (
        <div
          className={`message ${isError ? 'message-error' : 'message-success'}`}
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

      <section>
        {logs.length === 0 ? (
          <div
            style={{
              textAlign: 'center',
              color: '#888',
              padding: '32px',
              border: '1px dashed #ccc',
              borderRadius: '12px'
            }}
          >
            운동 기록이 없습니다.
          </div>
        ) : (
          <ul style={{
            display: 'flex',
            flexDirection: 'column',
            gap: '16px',
            listStyleType: 'none',
            padding: 0,
            margin: 0
          }}>
            {logs.map((log) => (
              <li
                key={log.id}
                style={{
                  padding: '20px',
                  background: '#ffffff',
                  borderRadius: '16px',
                  boxShadow: '0 4px 12px rgba(0, 0, 0, 0.08)'
                }}
              >
                <div style={{ fontWeight: 'bold', fontSize: '1.1rem' }}>{log.exerciseName}</div>
                <div style={{ marginTop: '6px', color: '#555' }}>
                  <span>{log.durationMinutes}분</span> | <span>{log.kcalBurned}kcal</span><br />
                  <span>{formatKoreanDateTime(log.exerciseAt)}</span>
                </div>

                <div
                  style={{
                    marginTop: '16px',
                    display: 'flex',
                    gap: '10px',
                    justifyContent: 'flex-end'  // ✅ 오른쪽 정렬 추가
                  }}
                >
                  <button
                    onClick={() => handleEdit(log)}
                    style={{
                      padding: '8px 16px',
                      borderRadius: '10px',
                      background: '#c8e6c9',
                      color: '#1b5e20',
                      border: 'none',
                      fontWeight: 'bold',
                      cursor: 'pointer'
                    }}
                  >
                    수정
                  </button>

                  <button
                    onClick={() => handleDelete(log.id)}
                    style={{
                      padding: '8px 16px',
                      borderRadius: '10px',
                      background: '#ffe082',
                      color: '#5d4037',
                      border: 'none',
                      fontWeight: 'bold',
                      cursor: 'pointer'
                    }}
                  >
                    삭제
                  </button>
                </div>

              </li>
            ))}
          </ul>
        )}
      </section>
    </div>
  );
};

export default ExerciseLogList;
