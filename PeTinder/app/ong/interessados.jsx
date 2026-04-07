import { useCallback, useEffect, useState } from 'react';
import { ActivityIndicator, Pressable, RefreshControl, ScrollView, StyleSheet, Text, View } from 'react-native';
import { useFocusEffect } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { colors } from '../../constants/theme';
import OngInteressadoItem from '../../components/ong/OngInteressadoItem';
import { getSession } from '../../services/sessionService';
import { listarInteressadosDaOng } from '../../services/interessadosService';

export default function Interessados() {
    const [interessados, setInteressados] = useState([]);
    const [expandedPets, setExpandedPets] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [isRefreshing, setIsRefreshing] = useState(false);
    const [ongId, setOngId] = useState('');
    const [loadError, setLoadError] = useState(false);

    const groupedInteressados = interessados.reduce((groups, interessado) => {
        const petNome = interessado?.petNome?.trim() || 'Sem nome';
        if (!groups[petNome]) {
            groups[petNome] = [];
        }
        groups[petNome].push(interessado);
        return groups;
    }, {});

    const sortedGroups = Object.entries(groupedInteressados)
        .sort(([petNomeA], [petNomeB]) => petNomeA.localeCompare(petNomeB, 'pt-BR'));

    const togglePetGroup = (petNome) => {
        setExpandedPets((current) => (
            current.includes(petNome)
                ? current.filter((item) => item !== petNome)
                : [...current, petNome]
        ));
    };

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
                <ScrollView
                    style={styles.list}
                    contentContainerStyle={styles.listContent}
                    showsVerticalScrollIndicator
                    refreshControl={
                        <RefreshControl refreshing={isRefreshing} onRefresh={handleRefresh} tintColor={colors.mauve} />
                    }
                >
                    {sortedGroups.map(([petNome, petInteressados]) => {
                        const isExpanded = expandedPets.includes(petNome);
                        return (
                            <View key={petNome} style={styles.groupCard}>
                                <Pressable style={styles.groupHeader} onPress={() => togglePetGroup(petNome)}>
                                    <View style={styles.groupHeaderText}>
                                        <Text style={styles.groupTitle} numberOfLines={1}>{petNome}</Text>
                                        <Text style={styles.groupSubtitle} numberOfLines={1}>
                                            {petInteressados.length} interessado{petInteressados.length === 1 ? '' : 's'}
                                        </Text>
                                    </View>

                                    <Ionicons
                                        name={isExpanded ? 'chevron-up' : 'chevron-down'}
                                        size={22}
                                        color={colors.mauve}
                                    />
                                </Pressable>

                                {isExpanded ? (
                                    <View style={styles.groupItems}>
                                        {petInteressados.map((item, index) => (
                                            <OngInteressadoItem
                                                key={`${item.userId || 'interessado'}-${item.petId || 'pet'}-${index}`}
                                                userName={item.userName}
                                                petNome={item.petNome}
                                                userEmail={item.userEmail}
                                                imageUrl={item.imageUrl}
                                                userId={item.userId}
                                                petId={item.petId}
                                            />
                                        ))}
                                    </View>
                                ) : null}
                            </View>
                        );
                    })}
                </ScrollView>
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
    list: {
        flex: 1,
    },
    listContent: {
        paddingTop: 10,
        paddingBottom: 24,
        gap: 12,
    },
    groupCard: {
        borderWidth: 1,
        borderColor: colors.roseBorder,
        borderRadius: 14,
        backgroundColor: colors.lightPink,
        padding: 10,
        gap: 10,
    },
    groupHeader: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: 10,
    },
    groupHeaderText: {
        flex: 1,
        minWidth: 0,
    },
    groupTitle: {
        fontSize: 16,
        fontWeight: '700',
        color: colors.textStrong,
    },
    groupSubtitle: {
        marginTop: 2,
        fontSize: 12,
        color: colors.black,
    },
    groupItems: {
        gap: 10,
    },
});
