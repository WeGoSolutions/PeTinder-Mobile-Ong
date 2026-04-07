import {
    View,
    Text,
    StyleSheet,
    ScrollView,
    ActivityIndicator,
} from 'react-native';
import { useCallback, useEffect, useState } from 'react';
import { useFocusEffect, useRouter } from 'expo-router';
import LikesBarChart from '../../components/charts/LikesBarChart';
import OngInteressadoItem from '../../components/ong/OngInteressadoItem';
import DynamicButton from '../../components/DynamicButton';
import { usePets } from '../../services/petService';
import { getSession } from '../../services/sessionService';
import { listarInteressadosDaOng } from '../../services/interessadosService';


export default function Home() {
    const router = useRouter();
    const { pets, adoptedCount, notAdoptedCount, loading: petsLoading, error: petsError } = usePets();
    const [interessados, setInteressados] = useState([]);
    const [isLoadingInteressados, setIsLoadingInteressados] = useState(true);
    const [ongId, setOngId] = useState('');

    useEffect(() => {
        const loadSession = async () => {
            const session = await getSession();
            setOngId(String(session?.ongId ?? ''));
        };

        loadSession();
    }, []);

    const loadInteressados = useCallback(async () => {
        if (!ongId) {
            setInteressados([]);
            setIsLoadingInteressados(false);
            return;
        }

        try {
            const response = await listarInteressadosDaOng(ongId);
            const lista = Array.isArray(response?.data) ? response.data : [];
            setInteressados(lista.slice(0, 3));
        } catch (error) {
            setInteressados([]);
        } finally {
            setIsLoadingInteressados(false);
        }
    }, [ongId]);

    useFocusEffect(
        useCallback(() => {
            if (ongId) {
                setIsLoadingInteressados(true);
                loadInteressados();
            }
        }, [loadInteressados, ongId])
    );

    useEffect(() => {
        loadInteressados();
    }, [loadInteressados]);

    return (
        <ScrollView
            style={styles.container}
            contentContainerStyle={styles.contentContainer}
            showsVerticalScrollIndicator={false}
        >
            <View style={styles.section}>
                <Text style={styles.title}>Últimos interessados</Text>
                <View style={styles.interessadosList}>
                    {isLoadingInteressados ? (
                        <View style={styles.loadingContainer}>
                            <ActivityIndicator size="small" color="#80465D" />
                        </View>
                    ) : interessados.length === 0 ? (
                        <Text style={styles.emptyText}>Nenhum interessado recente no momento.</Text>
                    ) : (
                        interessados.map((interessado) => (
                            <OngInteressadoItem
                                key={`${interessado.userId || 'interessado'}-${interessado.petId || 'pet'}`}
                                userName={interessado.userName}
                                petNome={interessado.petNome}
                                userEmail={interessado.userEmail}
                                imageUrl={interessado.imageUrl}
                                userId={interessado.userId}
                                petId={interessado.petId}
                            />
                        ))
                    )}
                </View>
                <View style={styles.verMaisContainer}>
                    <DynamicButton
                        variant="tertiary"
                        textStyle={styles.verMaisText}
                        onPress={() => router.replace('/ong/interessados')}
                    >
                        Ver mais
                    </DynamicButton>
                </View>
            </View>

            <View style={styles.section}>
                <Text style={styles.title}>Pets mais curtidos</Text>
                <LikesBarChart
                    pets={pets}
                    maxVisibleBars={6}
                    homeChart={true}
                />
            </View>
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#ffffff',
    },
    contentContainer: {
        padding: 16,
        gap: 16,
    },
    section: {
        minHeight: 260,
        borderRadius: 16,
        padding: 10,
        backgroundColor: '#FFF9FC',
        borderWidth: 2,
        borderColor: '#E8C8D5',
    },
    title: {
        fontSize: 22,
        fontWeight: '700',
        color: '#1A1A1A',
        marginBottom: 6,
    },
    interessadosList: {
        gap: 10,
    },
    loadingContainer: {
        paddingVertical: 12,
    },
    emptyText: {
        fontSize: 14,
        color: '#6E6E6E',
        textAlign: 'center',
        paddingVertical: 12,
    },
    verMaisContainer: {
        alignItems: 'center',
    },
    verMaisText: {
        color: '#80465D',
        fontSize: 16,
    },
});
