import { clearSession } from './sessionService';

export const logout = async () => {
    await clearSession();
};

export default {
    logout,
};
