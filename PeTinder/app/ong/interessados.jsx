import { useCallback, useEffect, useState } from 'react';
import { ActivityIndicator, FlatList, RefreshControl, StyleSheet, Text, View } from 'react-native';
import { useFocusEffect } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { colors } from '../../constants/theme';
import OngInteressadoItem from '../../components/ong/OngInteressadoItem';
import { getSession } from '../../services/sessionService';
import { listarInteressadosDaOng } from '../../services/interessadosService';

export default function Interessados() {
    const [interessados, setInteressados] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [isRefreshing, setIsRefreshing] = useState(false);
    const [ongId, setOngId] = useState('');
    const [loadError, setLoadError] = useState(false);

    const loadInteressados = useCallback(async () => {
        if (!ongId) {
            setInteressados([]);
            setIsLoading(false);
            setLoadError(false);
            return;
        }

        try {
            const response = await listarInteressadosDaOng(ongId);
            setInteressados(Array.isArray(response?.data) ? response.data : []);
            setLoadError(!response?.success);
        } catch (error) {
            setInteressados([]);
            setLoadError(true);
        } finally {
            setIsLoading(false);
            setIsRefreshing(false);
        }
    }, [ongId]);

    useEffect(() => {
        const loadSession = async () => {
            const session = await getSession();
            setOngId(String(session?.ongId ?? ''));
        };

        loadSession();
    }, []);

    useFocusEffect(
        useCallback(() => {
            if (ongId) {
                setIsLoading(true);
                loadInteressados();
            }
        }, [loadInteressados, ongId])
    );

    useEffect(() => {
        loadInteressados();
    }, [loadInteressados]);

    const handleRefresh = async () => {
        setIsRefreshing(true);
        await loadInteressados();
    };

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Interessados</Text>

            {isLoading ? (
                <View style={styles.loadingContainer}>
                    <ActivityIndicator size="large" color={colors.mauve} />
                </View>
            ) : interessados.length === 0 ? (
                <View style={styles.emptyStateContainer}>
                    <Ionicons name="people-outline" size={36} color={colors.mauve} />
                    <Text style={styles.emptyText}>Nenhum interessado encontrado no momento.</Text>
                </View>
            ) : (
                <FlatList
                    data={interessados}
                    keyExtractor={(item, index) => `${item.userId || 'interessado'}-${index}`}
                    renderItem={({ item }) => (
                        <OngInteressadoItem
                            userName={item.userName}
                            petNome={item.petNome}
                            userEmail={item.userEmail}
                            imageUrl={item.imageUrl}
                            userId={item.userId}
                            petId={item.petId}
                        />
                    )}
                    contentContainerStyle={styles.listContent}
                    ItemSeparatorComponent={() => <View style={styles.separator} />}
                    showsVerticalScrollIndicator
                    refreshControl={
                        <RefreshControl refreshing={isRefreshing} onRefresh={handleRefresh} tintColor={colors.mauve} />
                    }
                />
            )}
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#ffffff',
        padding: 24,
    },
    title: {
        fontSize: 24,
        fontWeight: '700',
        color: '#1A1A1A',
        marginBottom: 8,
    },
    subtitle: {
        fontSize: 14,
        color: '#6E6E6E',
        marginBottom: 16,
    },
    loadingContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    emptyStateContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        gap: 10,
    },
    emptyText: {
        fontSize: 14,
        color: '#6E6E6E',
        textAlign: 'center',
    },
    listContent: {
        paddingBottom: 24,
    },
    separator: {
        height: 10,
    },
});
