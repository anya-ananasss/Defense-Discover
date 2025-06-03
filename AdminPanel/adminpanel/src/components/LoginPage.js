// src/components/LoginPage.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authApi } from '../api';

export default function LoginPage(props) {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    try {
      const realToken = await authApi.login({ email, password });
      if (!realToken) {
        throw new Error('В ответе не найдено поле token');
      }
      localStorage.setItem('token', realToken);
      props.upd();
      navigate('/users');
    } catch (err) {
      console.log(err)
      setError('Неверные учётные данные');
    }
  };

  return (
      <div className="container d-flex justify-content-center align-items-center" style={{ minHeight: '80vh' }}>
        <div className="card shadow-sm w-100" style={{ maxWidth: '400px' }}>
          <div className="card-body">
            <h3 className="card-title text-center mb-4">Login</h3>
            {error && (
                <div className="alert alert-danger py-2" role="alert">
                  {error}
                </div>
            )}
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label htmlFor="emailInput" className="form-label">
                  Email
                </label>
                <input
                    id="emailInput"
                    type="email"
                    className="form-control"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="you@example.com"
                    required
                />
              </div>

              <div className="mb-3">
                <label htmlFor="passwordInput" className="form-label">
                  Password
                </label>
                <input
                    id="passwordInput"
                    type="password"
                    className="form-control"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="••••••••"
                    required
                />
              </div>

              <div className="d-grid">
                <button type="submit" className="btn btn-primary">
                  Log In
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
  );
}
