import { useCallback, useEffect, useState } from 'react';
import api from '../api';

const useBackend = String(process.env.EXPO_PUBLIC_UTILIZAR_BACKEND ?? '').toLowerCase() === 'true';

const fetchKpisFromApi = async () => {
    const response = await api.get('/dashs/global/kpis');
    return response?.data ?? {};
};

const fetchEntradasPorEspecieFromApi = async () => {
    const response = await api.get('/dashs/global/entradas-por-especie');
    return Array.isArray(response?.data) ? response.data : [];
};

const fetchDistribuicaoStatusFromApi = async () => {
    const response = await api.get('/dashs/global/distribuicao-status');
    return Array.isArray(response?.data) ? response.data : [];
};

const fetchMotivosRetornoFromApi = async () => {
    const response = await api.get('/dashs/global/motivos-retorno');
    return Array.isArray(response?.data) ? response.data : [];
};

const fetchDistribuicaoFaixaEtariaFromApi = async () => {
    const response = await api.get('/dashs/global/distribuicao-faixa-etaria');
    return Array.isArray(response?.data) ? response.data : [];
};

export const useGlobalDashboardData = () => {
    const [kpis, setKpis] = useState(null);
    const [especiesData, setEspeciesData] = useState([]);
    const [statusData, setStatusData] = useState([]);
    const [motivosRetornoData, setMotivosRetornoData] = useState([]);
    const [faixaEtariaData, setFaixaEtariaData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchData = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);

            if (!useBackend) {
                setError('Backend não configurado para dados globais');
                return;
            }

            const [kpisResult, especiesResult, statusResult, motivosResult, faixaResult] = await Promise.all([
                fetchKpisFromApi(),
                fetchEntradasPorEspecieFromApi(),
                fetchDistribuicaoStatusFromApi(),
                fetchMotivosRetornoFromApi(),
                fetchDistribuicaoFaixaEtariaFromApi(),
            ]);

            setKpis(kpisResult);
            setEspeciesData(especiesResult);
            setStatusData(statusResult);
            setMotivosRetornoData(motivosResult);
            setFaixaEtariaData(faixaResult);
        } catch (err) {
            setError(err?.message ?? 'Erro ao carregar dados globais');
            console.error('Error fetching global dashboard data:', err);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    return {
        kpis,
        especiesData,
        statusData,
        motivosRetornoData,
        faixaEtariaData,
        loading,
        error,
        refetch: fetchData,
    };
};

export default {
    useGlobalDashboardData,
};
