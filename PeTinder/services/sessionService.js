import AsyncStorage from '@react-native-async-storage/async-storage';
import api from '../api';

const TOKEN_KEY = 'token';
const NAME_KEY = 'nome_ong';

const isJwtToken = (value) => {
    if (typeof value !== 'string') {
        return false;
    }

    const parts = value.split('.');
    return parts.length === 3;
};

export const saveSession = async (token, nome) => {
    try {
        if (typeof nome === 'string') {
            await AsyncStorage.setItem(NAME_KEY, nome);
        }

        if (isJwtToken(token)) {
            await AsyncStorage.setItem(TOKEN_KEY, token);
            api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        } else {
            await AsyncStorage.removeItem(TOKEN_KEY);
            delete api.defaults.headers.common['Authorization'];
        }
    } catch (error) {
        console.error('Erro ao salvar sessão:', error);
        throw error;
    }
};

export const getSession = async () => {
    try {
        const storedToken = await AsyncStorage.getItem(TOKEN_KEY);
        const name = await AsyncStorage.getItem(NAME_KEY);
        const token = isJwtToken(storedToken) ? storedToken : null;

        if (token) {
            api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        } else {
            delete api.defaults.headers.common['Authorization'];
            if (storedToken) {
                await AsyncStorage.removeItem(TOKEN_KEY);
            }
        }

        return { token, name };
    } catch (error) {
        console.error('Erro ao recuperar sessão:', error);
        return { token: null, name: null };
    }
};

export const clearSession = async () => {
    try {
        await AsyncStorage.removeItem(TOKEN_KEY);
        await AsyncStorage.removeItem(NAME_KEY);

        delete api.defaults.headers.common['Authorization'];
    } catch (error) {
        console.error('Erro ao limpar sessão:', error);
        throw error;
    }
};

export default {
    saveSession,
    getSession,
    clearSession,
};
