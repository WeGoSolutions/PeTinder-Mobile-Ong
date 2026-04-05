import axios from "axios";

const env = process.env;
const useBackend = String(env.EXPO_PUBLIC_UTILIZAR_BACKEND ?? '').toLowerCase() === 'true';

const backendApiUrl = env.EXPO_PUBLIC_BACKEND_API_URL;
const jsonServerUrl = env.EXPO_PUBLIC_JSON_SERVER_URL;

const baseURL = useBackend ? backendApiUrl : jsonServerUrl;

if (!baseURL) {
    console.warn('API baseURL nao definida. Configure EXPO_PUBLIC_BACKEND_API_URL no .env');
}

const api = axios.create({
    baseURL
});

export default api;