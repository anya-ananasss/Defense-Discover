import axios from 'axios';

const API_GATEWAY = 'https://valyalshchikov.ru/api/v1';
const ROLE_SERVICE = 'https://valyalshchikov.ru/api/v1';


function getAuthHeaders() {
  const token = localStorage.getItem('token');
  return token ? { Authorization: `Bearer ${token}` } : {};
}

export const authApi = {
  login: (credentials) =>
    axios.post(`${API_GATEWAY}/login/admin`, credentials).then(res => res.data),
  registerAndLogin: (user) =>
    axios.post(`${API_GATEWAY}/registerAndLogin`, user).then(res => res.data),
};

export const userApi = {
  getAll: () =>
    axios.get(`${API_GATEWAY}/admin/getAllUsers`, { headers: getAuthHeaders() })
      .then(res => res.data),
  create: (userDto) =>
    axios.post(`${API_GATEWAY}/admin/registerWithRole`, userDto, { headers: getAuthHeaders() }),
  remove: (username) =>
    axios.post(
      `${API_GATEWAY}/admin/deleteUser`,
      { username: username },
      { headers: getAuthHeaders() }
    ),

  addRole: (username, roleName) =>
    axios.post(
      `${API_GATEWAY}/admin/addRoleToUser`,
      { username: username, role: { name: roleName } },
      { headers: getAuthHeaders() }
    ),
  removeRole: (username, roleName) =>
    axios.post(
      `${API_GATEWAY}/admin/deleteRoleFromUser`,
      { username, role: { name: roleName } },
      { headers: getAuthHeaders() }
    ),
};

export const roleApi = {
  getAll: () =>
    axios.get(`${ROLE_SERVICE}/admin/getAllRoles`, { headers: getAuthHeaders() }).then(res => res.data)
};

export const questionApi = {
  getAll: () =>
    axios.get(`${API_GATEWAY}/admin/questions`, { headers: getAuthHeaders() })
      .then(res => res.data),
  remove: (id) =>
    axios.delete(`${API_GATEWAY}/admin/questions/${id}`, { headers: getAuthHeaders() })
};
