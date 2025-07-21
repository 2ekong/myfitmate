import { useState } from 'react';
import axios from 'axios';
import './aiCoachPage.css';

function AiCoachPage() {
  const [question, setQuestion] = useState('');
  const [answer, setAnswer] = useState('');
  const [loading, setLoading] = useState(false);

  const handleAsk = async () => {
    if (!question.trim()) return;

    try {
      setLoading(true);
      const res = await axios.get('http://localhost:8080/api/ai/ask', {
        params: { q: question }
      });
      setAnswer(res.data);
    } catch (err) {
      setAnswer('⚠️ 응답 중 오류가 발생했습니다.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="ai-coach-container">
      <h2>AI 건강 코치에게 묻기</h2>
      <textarea
        placeholder="건강, 운동, 식단 등에 대해 궁금한 점을 입력하세요."
        value={question}
        onChange={(e) => setQuestion(e.target.value)}
      />
      <div className="button-wrapper">
      <button onClick={handleAsk} disabled={loading}>
        {loading ? '답변 생성 중...' : '질문하기'}
      </button>
      </div>
      {answer && (
        <div className="ai-answer">
          <h4>AI의 답변</h4>
          <p>{answer}</p>
        </div>
      )}
    </div>
  );
}

export default AiCoachPage;
