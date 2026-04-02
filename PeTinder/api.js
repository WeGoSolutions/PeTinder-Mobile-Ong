import axios from "axios";

const baseURL =
    process.env.EXPO_PUBLIC_BACKEND_API_URL ||
    process.env.EXPO_BACKEND_API_URL ||
    "http://localhost:3000";

const api = axios.create({
    baseURL
});

export default api;