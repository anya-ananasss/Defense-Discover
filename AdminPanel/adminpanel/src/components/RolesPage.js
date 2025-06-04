
import React, { useEffect, useState } from 'react';
import { roleApi } from '../api';

export default function RolesPage() {
  const [roles, setRoles] = useState([]);
  const [newRoleName, setNewRoleName] = useState('');
  const [editingRole, setEditingRole] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchRoles();
  }, []);

  const fetchRoles = async () => {
    try {
      const data = await roleApi.getAll();
      setRoles(data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleCreateRole = async (e) => {
    e.preventDefault();
    setError(null);
    if (!newRoleName.trim()) {
      setError('Введите имя роли');
      return;
    }
    try {
      await roleApi.create(newRoleName);
      setNewRoleName('');
      fetchRoles();
    } catch (err) {
      console.error(err);
      setError('Не удалось создать роль');
    }
  };

  const handleDeleteRole = async (id) => {
    if (!window.confirm(`Удалить роль с id ${id}?`)) return;
    try {
      await roleApi.remove(id);
      fetchRoles();
    } catch (err) {
      console.error(err);
    }
  };

  const handleEditRole = (role) => {
    setEditingRole(role);
    setNewRoleName(role.name);
  };

  const handleUpdateRole = async (e) => {
    e.preventDefault();
    if (!newRoleName.trim()) return;
    try {
      await roleApi.update(editingRole.id, newRoleName);
      setEditingRole(null);
      setNewRoleName('');
      fetchRoles();
    } catch (err) {
      console.error(err);
    }
  };

  return (
      <div className="container py-4">
        <h2 className="mb-4">Roles</h2>

        {/* Форма для добавления/редактирования роли */}
        <div className="card mb-5 shadow-sm">
          <div className="card-body">
            <h5 className="card-title">
              {editingRole ? 'Edit Role' : 'Create New Role'}
            </h5>
            {error && (
                <div className="alert alert-danger py-2" role="alert">
                  {error}
                </div>
            )}
            <form onSubmit={editingRole ? handleUpdateRole : handleCreateRole}>
              <div className="row g-3 align-items-end">
                <div className="col-md-8">
                  <label htmlFor="roleInput" className="form-label">
                    {editingRole ? 'Role Name' : 'New Role'}
                  </label>
                  <input
                      id="roleInput"
                      type="text"
                      className="form-control"
                      value={newRoleName}
                      onChange={(e) => setNewRoleName(e.target.value)}
                      placeholder="Введите название роли"
                  />
                </div>
                <div className="col-md-2">
                  <button
                      type="submit"
                      className={
                        editingRole ? 'btn btn-warning w-100' : 'btn btn-success w-100'
                      }
                  >
                    {editingRole ? 'Update' : 'Create'}
                  </button>
                </div>
                {editingRole && (
                    <div className="col-md-2 d-flex">
                      <button
                          type="button"
                          className="btn btn-secondary w-100"
                          onClick={() => {
                            setEditingRole(null);
                            setNewRoleName('');
                          }}
                      >
                        Cancel
                      </button>
                    </div>
                )}
              </div>
            </form>
          </div>
        </div>

        <div className="table-responsive shadow-sm">
          <table className="table table-bordered align-middle mb-0">
            <thead className="table-light">
            <tr>
              <th scope="col" style={{ width: '10%' }}>
                ID
              </th>
              <th scope="col">Name</th>
              <th scope="col" style={{ width: '25%' }}>
                Действия
              </th>
            </tr>
            </thead>
            <tbody>
            {roles.map((r) => (
                <tr key={r.id}>
                  <td>{r.id}</td>
                  <td>{r.name}</td>
                  <td>
                    <button
                        className="btn btn-sm btn-outline-primary me-2"
                        onClick={() => handleEditRole(r)}
                    >
                      Редактировать
                    </button>
                    <button
                        className="btn btn-sm btn-outline-danger"
                        onClick={() => handleDeleteRole(r.id)}
                    >
                      Удалить
                    </button>
                  </td>
                </tr>
            ))}
            {roles.length === 0 && (
                <tr>
                  <td colSpan="3" className="text-center text-muted py-3">
                    Ролей не найдено.
                  </td>
                </tr>
            )}
            </tbody>
          </table>
        </div>
      </div>
  );
}
