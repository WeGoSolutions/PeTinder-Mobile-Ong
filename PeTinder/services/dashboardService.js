import { useCallback, useEffect, useState } from 'react';
import api from '../api';
import mockData from '../data/db.json';
import { getSession } from './sessionService';

const useBackend = String(process.env.EXPO_PUBLIC_UTILIZAR_BACKEND ?? '').toLowerCase() === 'true';

const isNotFoundError = (error) => error?.response?.status === 404;

const normalizePetFromRanking = (item) => ({
    id: item?.id ?? item?.petId ?? null,
    name: item?.nome ?? item?.name ?? 'Pet',
    likes: Number(item?.curtidas ?? item?.likes ?? 0),
});

const normalizePetPendingFromApi = (item) => ({
    id: item?.id ?? item?.petId ?? null,
    name: item?.nome ?? item?.name ?? 'Pet',
    needs: Array.isArray(item?.faltas) ? item.faltas.filter(Boolean) : [],
    imageUrl: item?.imageUrl ?? item?.imagemUrl ?? null,
});

const fetchDashboardRankingFromApi = async (ongId) => {
    try {
        const response = await api.get(`/dashs/ranking/${ongId}`);
        const data = Array.isArray(response?.data) ? response.data : [];
        return data.map(normalizePetFromRanking);
    } catch (error) {
        if (isNotFoundError(error)) {
            return [];
        }

        throw error;
    }
};

const fetchDashboardStatsFromApi = async (ongId) => {
    try {
        const response = await api.get(`/dashs/adotados-ou-nao/${ongId}`);
        const data = response?.data ?? {};

        return {
            adoptedCount: Number(data?.adotados ?? 0),
            notAdoptedCount: Number(data?.naoAdotados ?? 0),
        };
    } catch (error) {
        if (isNotFoundError(error)) {
            return {
                adoptedCount: 0,
                notAdoptedCount: 0,
            };
        }

        throw error;
    }
};

const fetchDashboardPendingFromApi = async (ongId) => {
    try {
        const response = await api.get(`/dashs/pendencias/${ongId}`);
        const data = Array.isArray(response?.data) ? response.data : [];
        return data
            .map(normalizePetPendingFromApi)
            .filter((pet) => pet.needs.length > 0);
    } catch (error) {
        if (isNotFoundError(error)) {
            return [];
        }

        throw error;
    }
};

const fetchDashboardRankingFromMock = async () => {
    const pets = Array.isArray(mockData?.pets) ? mockData.pets : [];

    return pets.map((pet) => ({
        id: pet?.id ?? null,
        name: pet?.name ?? 'Pet',
        likes: Number(pet?.likes ?? 0),
    }));
};

const fetchDashboardStatsFromMock = async () => {
    const pets = Array.isArray(mockData?.pets) ? mockData.pets : [];
    const adoptedCount = pets.filter((pet) => Boolean(pet?.adopted)).length;
    const notAdoptedCount = pets.length - adoptedCount;

    return {
        adoptedCount,
        notAdoptedCount,
    };
};

const fetchDashboardPendingFromMock = async () => {
    const pets = Array.isArray(mockData?.pets) ? mockData.pets : [];

    return pets
        .map((pet) => {
            const needs = [];

            if (pet?.isCastrado === false) {
                needs.push('Castracao');
            }

            if (pet?.isVermifugo === false) {
                needs.push('Vermifugo');
            }

            if (pet?.isVacinado === false) {
                needs.push('Vacina');
            }

            return {
                id: pet?.id ?? null,
                name: pet?.name ?? 'Pet',
                needs,
                imageUrl: pet?.imageUrl ?? null,
            };
        })
        .filter((pet) => pet.needs.length > 0);
};

export const useDashboardData = () => {
    const [pets, setPets] = useState([]);
    const [adoptedCount, setAdoptedCount] = useState(0);
    const [notAdoptedCount, setNotAdoptedCount] = useState(0);
    const [pendingPets, setPendingPets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchDashboardData = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);

            if (useBackend) {
                const session = await getSession();
                const ongId = String(session?.ongId ?? '').trim();

                if (!ongId) {
                    throw new Error('ONG nao encontrada na sessao');
                }

                const [ranking, stats, pending] = await Promise.all([
                    fetchDashboardRankingFromApi(ongId),
                    fetchDashboardStatsFromApi(ongId),
                    fetchDashboardPendingFromApi(ongId),
                ]);

                setPets(ranking);
                setAdoptedCount(stats.adoptedCount);
                setNotAdoptedCount(stats.notAdoptedCount);
                setPendingPets(pending);
                return;
            }

            const [ranking, stats, pending] = await Promise.all([
                fetchDashboardRankingFromMock(),
                fetchDashboardStatsFromMock(),
                fetchDashboardPendingFromMock(),
            ]);

            setPets(ranking);
            setAdoptedCount(stats.adoptedCount);
            setNotAdoptedCount(stats.notAdoptedCount);
            setPendingPets(pending);
        } catch (err) {
            setError(err?.message ?? 'Erro ao carregar dashboard');
            setPets([]);
            setAdoptedCount(0);
            setNotAdoptedCount(0);
            setPendingPets([]);
            console.error('Error fetching dashboard data:', err);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchDashboardData();
    }, [fetchDashboardData]);

    return {
        pets,
        adoptedCount,
        notAdoptedCount,
        pendingPets,
        totalCount: adoptedCount + notAdoptedCount,
        loading,
        error,
        refetch: fetchDashboardData,
    };
};

export default {
    useDashboardData,
};