import api from '../api';

export const listarPetsDaOng = async (ongId, page = 0, size = 10) => {
    const currentPage = Number.isFinite(Number(page)) ? Math.max(0, Number(page)) : 0;
    const pageSize = Number.isFinite(Number(size)) ? Math.max(1, Number(size)) : 10;

    return api.get(`/ongs/${ongId}/pets`, {
        params: { page: currentPage, size: pageSize },
    });
};

export const criarPet = async (payload) => {
    return api.post('/pets', payload);
};

export const atualizarPet = async (petId, payload) => {
    return api.put(`/pets/${petId}`, payload);
};

export const deletarPet = async (petId) => {
    return api.delete(`/pets/${petId}`);
};

export const marcarPetComoAdotado = async (petId, adotanteId) => {
    return api.post(`/status/adopted/${petId}/${adotanteId}`);
};

export const marcarComoAdotadoExterno = async (petId) => {
    const ADOTANTE_EXTERNO_ID = '11111111-1111-1111-1111-111111111111';
    return api.post(`/status/adopted/${petId}/${ADOTANTE_EXTERNO_ID}`);
};

export const voltarParaAdocao = async (petId, adotanteIdToDelete) => {
    return api.delete(`/status/${petId}/${adotanteIdToDelete}`);
};

export const fetchAdotanteInfoPetCard = async (petId) => {
    return api.get(`/status/adopted/${petId}`);
};

export const getMensagensPendentesPetCard = async (ongId, petName, petId) => {
    const response = await api.get(`/ongs/${ongId}/mensagens-pendentes`);
    const data = Array.isArray(response?.data) ? response.data : [];

    return data.filter((msg) => {
        const msgPetId = String(msg?.petId ?? '');
        const msgPetNome = String(msg?.nomePet ?? msg?.petNome ?? '').toLowerCase();
        const targetPetId = String(petId ?? '');
        const targetPetNome = String(petName ?? '').toLowerCase();

        if (targetPetId && msgPetId) {
            return msgPetId === targetPetId;
        }

        if (targetPetNome && msgPetNome) {
            return msgPetNome === targetPetNome;
        }

        return false;
    });
};

export default {
    listarPetsDaOng,
    criarPet,
    atualizarPet,
    deletarPet,
    marcarPetComoAdotado,
    marcarComoAdotadoExterno,
    voltarParaAdocao,
    fetchAdotanteInfoPetCard,
    getMensagensPendentesPetCard,
};
