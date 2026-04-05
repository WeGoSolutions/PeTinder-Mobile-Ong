import api from '../api';

export const login = async (email, senha) => {
    const response = await api.post('/ongs/login', {
        senha,
        email,
    });

    return response.data;
};

export default {
    login,
};
