// src/components/UsersPage.js
import React, { useEffect, useState } from 'react';
import { userApi, roleApi } from '../api';

export default function UsersPage() {
  const [users, setUsers] = useState([]);
  const [roles, setRoles] = useState([]);
  const [newUser, setNewUser] = useState({
    username: '',
    email: '',
    password: '',
    roleName: '',
  });
  const [error, setError] = useState(null);

  // При монтировании — загружаем данные
  useEffect(() => {
    fetchUsers();
    fetchRoles();
  }, []);

  const fetchUsers = async () => {
    try {
      const data = await userApi.getAll();
      setUsers(data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchRoles = async () => {
    try {
      const data = await roleApi.getAll();
      setRoles(data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleCreateUser = async (e) => {
    e.preventDefault();
    setError(null);
    const { username, email, password, roleName } = newUser;
    if (!username || !email || !password || !roleName) {
      setError('Заполните все поля');
      return;
    }
    try {
      await userApi.create({
        username,
        email,
        password,
        role: { name: roleName },
      });
      setNewUser({ username: '', email: '', password: '', roleName: '' });
      fetchUsers();
    } catch (err) {
      console.error(err);
      setError('Не удалось создать пользователя');
    }
  };

  const handleDeleteUser = async (username) => {
    if (!window.confirm(`Удалить пользователя ${username}?`)) return;
    try {
      await userApi.remove(username);
      fetchUsers();
    } catch (err) {
      console.error(err);
    }
  };

  const handleAddRole = async (username, roleName) => {
    try {
      await userApi.addRole(username, roleName);
      fetchUsers();
    } catch (err) {
      console.error(err);
    }
  };

  const handleRemoveRole = async (username, roleName) => {
    try {
      await userApi.removeRole(username, roleName);
      fetchUsers();
    } catch (err) {
      console.error(err);
    }
  };

  return (
      <div className="container py-4">
        <h2 className="mb-4">Пользователи</h2>

        {/* Форма создания нового пользователя */}
        <div className="card mb-5 shadow-sm">
          <div className="card-body">
            <h5 className="card-title">Создать нового пользователя</h5>
            {error && (
                <div className="alert alert-danger py-2" role="alert">
                  {error}
                </div>
            )}
            <form onSubmit={handleCreateUser}>
              <div className="row g-3">
                <div className="col-md-3">
                  <label htmlFor="newUserUsername" className="form-label">
                    Имя
                  </label>
                  <input
                      id="newUserUsername"
                      type="text"
                      className="form-control"
                      value={newUser.username}
                      onChange={(e) =>
                          setNewUser({ ...newUser, username: e.target.value })
                      }
                  />
                </div>
                <div className="col-md-3">
                  <label htmlFor="newUserEmail" className="form-label">
                    Email
                  </label>
                  <input
                      id="newUserEmail"
                      type="email"
                      className="form-control"
                      value={newUser.email}
                      onChange={(e) =>
                          setNewUser({ ...newUser, email: e.target.value })
                      }
                  />
                </div>
                <div className="col-md-3">
                  <label htmlFor="newUserPassword" className="form-label">
                    Пароль
                  </label>
                  <input
                      id="newUserPassword"
                      type="password"
                      className="form-control"
                      value={newUser.password}
                      onChange={(e) =>
                          setNewUser({ ...newUser, password: e.target.value })
                      }
                  />
                </div>
                <div className="col-md-2">
                  <label htmlFor="newUserRole" className="form-label">
                    Роль
                  </label>
                  <select
                      id="newUserRole"
                      className="form-select"
                      value={newUser.roleName}
                      onChange={(e) =>
                          setNewUser({ ...newUser, roleName: e.target.value })
                      }
                  >
                    <option value="">— Select Role —</option>
                    {roles.map((r) => (
                        <option key={r.id} value={r.name}>
                          {r.name}
                        </option>
                    ))}
                  </select>
                </div>
                <div className="col-md-1 d-flex align-items-end">
                  <button type="submit" className="btn btn-success w-100">
                    Create
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>

        {/* Таблица пользователей */}
        <div className="table-responsive shadow-sm">
          <table className="table table-striped table-hover align-middle mb-0">
            <thead className="table-dark">
            <tr>
              <th scope="col">Username</th>
              <th scope="col">Email</th>
              <th scope="col">Role</th>
              <th scope="col" style={{ width: '200px' }}>
                Actions
              </th>
            </tr>
            </thead>
            <tbody>
            {users.map((u) => (
                <tr key={u.username}>
                  <td>{u.username}</td>
                  <td>{u.email}</td>
                  <td>{u.role?.name || '—'}</td>
                  <td>
                    <button
                        className="btn btn-sm btn-outline-danger me-2"
                        onClick={() => handleDeleteUser(u.username)}
                    >
                      Удалить
                    </button>

                    {u.role ? (
                        <button
                            className="btn btn-sm btn-outline-warning"
                            onClick={() => handleRemoveRole(u.username, u.role.name)}
                        >
                          Удалить роль
                        </button>
                    ) : (
                        <select
                            className="form-select form-select-sm d-inline-block w-auto"
                            defaultValue=""
                            onChange={(e) =>
                                e.target.value &&
                                handleAddRole(u.username, e.target.value)
                            }
                        >
                          <option value="">Add Role…</option>
                          {roles.map((r) => (
                              <option key={r.id} value={r.name}>
                                {r.name}
                              </option>
                          ))}
                        </select>
                    )}
                  </td>
                </tr>
            ))}
            {users.length === 0 && (
                <tr>
                  <td colSpan="4" className="text-center text-muted py-3">
                    Пользователей не найдено.
                  </td>
                </tr>
            )}
            </tbody>
          </table>
        </div>
      </div>
  );
}
