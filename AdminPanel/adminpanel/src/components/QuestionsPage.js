import React, { useEffect, useState } from 'react';
import { questionApi } from '../api';

export default function QuestionsPage() {
  const [questions, setQuestions] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchQuestions();
  }, []);

  const fetchQuestions = async () => {
    try {
      const data = await questionApi.getAll();
      setQuestions(data);
    } catch (err) {
      console.error(err);
      setError('Не удалось загрузить вопросы');
    }
  };

  const handleDeleteQuestion = async (id) => {
    if (!window.confirm(`Удалить вопрос #${id}?`)) return;
    try {
      await questionApi.remove(id);
      fetchQuestions();
    } catch (err) {
      console.error(err);
      setError('Не удалось удалить вопрос');
    }
  };

  return (
    <div className="container py-4">
      <h2 className="mb-4">Вопросы</h2>

      {error && (
        <div className="alert alert-danger py-2 mb-4" role="alert">
          {error}
        </div>
      )}

      {}
      <div className="table-responsive shadow-sm">
        <table className="table table-striped table-hover align-middle mb-0">
          <thead className="table-dark">
            <tr>
              <th scope="col">ID</th>
              <th scope="col">Тема</th>
              <th scope="col">Вопрос</th>
              <th scope="col">Варианты ответов</th>
              <th scope="col">Правильный ответ</th>
              <th scope="col" style={{ width: '100px' }}>
                Действия
              </th>
            </tr>
          </thead>
          <tbody>
            {questions.map((q) => (
              <tr key={q.questionId}>
                <td>{q.questionId}</td>
                <td>{q.topic}</td>
                <td>{q.question}</td>
                <td>
                  <ol className="mb-0 ps-3">
                    {q.options.map((option, index) => (
                      <li key={index}>{option}</li>
                    ))}
                  </ol>
                </td>
                <td>{q.correctAnswer}</td>
                <td>
                  <button
                    className="btn btn-sm btn-outline-danger"
                    onClick={() => handleDeleteQuestion(q.questionId)}
                  >
                    Удалить
                  </button>
                </td>
              </tr>
            ))}
            {questions.length === 0 && (
              <tr>
                <td colSpan="6" className="text-center text-muted py-3">
                  Вопросов не найдено.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
} 