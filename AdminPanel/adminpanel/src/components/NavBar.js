import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

export default function NavBar(props) {
  const navigate = useNavigate();
  const isAuthenticated = !!localStorage.getItem('token');

  const handleLogout = () => {
    localStorage.removeItem('token');
    props.upd();
    navigate('/login');
  };

  return (
      <nav className="navbar navbar-expand-lg navbar-light bg-light shadow-sm">
        <div className="container">
          <Link className="navbar-brand fw-bold" to="/">
            Defense-Discover AdminPanel
          </Link>

          <button
              className="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#mainNavbar"
              aria-controls="mainNavbar"
              aria-expanded="false"
              aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon" />
          </button>

          <div className="collapse navbar-collapse" id="mainNavbar">
            {isAuthenticated ? (
                <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                  <li className="nav-item">
                    <Link className="nav-link" to="/users">
                      Пользователи
                    </Link>
                  </li>
                  <li className="nav-item">
                    <Link className="nav-link" to="/roles">
                      Роли
                    </Link>
                  </li>
                  <li className="nav-item">
                    <Link className="nav-link" to="/questions">
                      Вопросы
                    </Link>
                  </li>
                </ul>
            ) : null}

            <ul className="navbar-nav ms-auto mb-2 mb-lg-0">
              {isAuthenticated ? (
                  <li className="nav-item">
                    <button
                        className="btn btn-outline-danger"
                        onClick={handleLogout}
                    >
                      Выйти
                    </button>
                  </li>
              ) : (
                  <li className="nav-item">
                    <Link className="btn btn-outline-primary" to="/login">
                      Войти
                    </Link>
                  </li>
              )}
            </ul>
          </div>
        </div>
      </nav>
  );
}
