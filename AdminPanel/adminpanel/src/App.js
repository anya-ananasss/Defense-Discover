import React, {useState} from 'react';
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate
} from 'react-router-dom';

import LoginPage from './components/LoginPage';
import UsersPage from './components/UsersPage';
import RolesPage from './components/RolesPage';
import QuestionsPage from './components/QuestionsPage';
import NavBar from './components/NavBar';

function App() {
  const isAuthenticated = !!localStorage.getItem('token');
  const [count, setCount] = useState(false);

  const upd = () => {
    setCount(!count)
  }

  return (
    <Router>
      <NavBar upd={upd} />
      <Routes>
        <Route
          path="/login"
          element={
            isAuthenticated ? <Navigate to="/users" replace /> : <LoginPage upd={upd}/>
          }
        />
        <Route
          path="/users"
          element={isAuthenticated ? <UsersPage upd={upd}/> : <Navigate to="/login" replace />}
        />
        <Route
          path="/roles"
          element={isAuthenticated ? <RolesPage upd={upd} /> : <Navigate to="/login" replace />}
        />
        <Route
          path="/questions"
          element={isAuthenticated ? <QuestionsPage /> : <Navigate to="/login" replace />}
        />
        <Route
          path="*"
          element={<Navigate to={isAuthenticated ? "/users" : "/login"} replace />}
        />
      </Routes>
    </Router>
  );
}

export default App;
