import AsyncStorage from '@react-native-async-storage/async-storage';
import { Buffer } from 'buffer';
import api from '../api';

const TOKEN_KEY = 'token';
const NAME_KEY = 'nome_ong';
const ONG_ID_KEY = 'ong_id';

const isJwtToken = (value) => {
    if (typeof value !== 'string') {
        return false;
    }

    const parts = value.split('.');
    return parts.length === 3;
};

const hasValidTokenFormat = (value) => typeof value === 'string' && value.trim().length > 0;

const decodeBase64Url = (value) => {
    const base64 = value.replace(/-/g, '+').replace(/_/g, '/');
    const padding = base64.length % 4;
    const normalized = padding > 0 ? `${base64}${'='.repeat(4 - padding)}` : base64;
    return Buffer.from(normalized, 'base64').toString('utf8');
};

const isJwtExpired = (token) => {
    if (!isJwtToken(token)) {
        return false;
    }

    try {
        const payloadSegment = token.split('.')[1];
        if (!payloadSegment) {
            return true;
        }

        const payload = JSON.parse(decodeBase64Url(payloadSegment));
        if (!payload?.exp) {
            return false;
        }

        const nowInSeconds = Math.floor(Date.now() / 1000);
        return Number(payload.exp) <= nowInSeconds;
    } catch (error) {
        console.warn('Token JWT invalido, removendo sessao de token.');
        return true;
    }
};

export const saveSession = async (token, nome, ongId = null) => {
    try {
        if (typeof nome === 'string') {
            await AsyncStorage.setItem(NAME_KEY, nome);
        }

        if (ongId !== null && ongId !== undefined && String(ongId).trim().length > 0) {
            await AsyncStorage.setItem(ONG_ID_KEY, String(ongId));
        }

        if (!hasValidTokenFormat(token)) {
            await AsyncStorage.removeItem(TOKEN_KEY);
            delete api.defaults.headers.common['Authorization'];
            return;
        }

        await AsyncStorage.setItem(TOKEN_KEY, token);
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } catch (error) {
        console.error('Erro ao salvar sessão:', error);
        throw error;
    }
};

export const getSession = async () => {
    try {
        const storedToken = await AsyncStorage.getItem(TOKEN_KEY);
        const name = await AsyncStorage.getItem(NAME_KEY);
        const ongId = await AsyncStorage.getItem(ONG_ID_KEY);

        let token = hasValidTokenFormat(storedToken) ? storedToken : null;
        if (token && isJwtExpired(token)) {
            token = null;
        }

        if (token && hasValidTokenFormat(token)) {
            api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        } else {
            delete api.defaults.headers.common['Authorization'];
            if (storedToken) {
                await AsyncStorage.removeItem(TOKEN_KEY);
            }
        }

        return { token, name, ongId };
    } catch (error) {
        console.error('Erro ao recuperar sessão:', error);
        return { token: null, name: null, ongId: null };
    }
};

export const clearSession = async () => {
    try {
        await AsyncStorage.removeItem(TOKEN_KEY);
        await AsyncStorage.removeItem(NAME_KEY);
        await AsyncStorage.removeItem(ONG_ID_KEY);

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
