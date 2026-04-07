import api from '../api';

export const listarInteressadosDaOng = async (ongId) => {
    try {
        const response = await api.get(`/ongs/${ongId}/mensagens-pendentes`);
        const dados = Array.isArray(response?.data) ? response.data : [];

        const interessados = dados.map((item) => ({
            petId: item?.petId || null,
            petNome: item?.petNome || '',
            userId: item?.userId || null,
            userName: item?.userName || 'Usuario',
            userEmail: item?.userEmail || '',
            imageUrl: item?.imageUrl || null,
            dataStatus: item?.dataStatus || null,
        }));

        return {
            success: true,
            data: interessados,
        };
    } catch (error) {
        console.error('Erro ao buscar interessados da ONG:', error);
        return {
            success: false,
            data: [],
            error,
        };
    }
};

export default {
    listarInteressadosDaOng,
};
