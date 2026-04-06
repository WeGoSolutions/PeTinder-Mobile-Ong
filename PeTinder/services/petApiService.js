import api from '../api';

export const listarPetsDaOng = async (ongId, page = 0, size = 10) => {
    const currentPage = Number.isFinite(Number(page)) ? Math.max(0, Number(page)) : 0;
    const pageSize = Number.isFinite(Number(size)) ? Math.max(1, Number(size)) : 10;

    return api.get(`/ongs/${ongId}/pets`, {
        params: { page: currentPage, size: pageSize },
    });
};

export default {
    listarPetsDaOng,
};
