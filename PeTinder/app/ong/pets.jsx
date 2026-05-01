import { useCallback, useEffect, useMemo, useState } from 'react';
import { ActivityIndicator, Pressable, View, Text, StyleSheet, ScrollView } from 'react-native';
import { colors } from '../../constants/theme';
import PetSearchInput from '../../components/ong/PetSearchInput';
import PetCard from '../../components/ong/PetCard';
import PetModalFlow from '../../components/modals/PetModalFlow';
import { listarPetsDaOng } from '../../services/petApiService';
import { getSession } from '../../services/sessionService';
import { useFocusEffect, useLocalSearchParams } from 'expo-router';
import { resolveImageUri } from '../../utils/imageUri';


export default function Pets() {
    const { refresh } = useLocalSearchParams();
    const [search, setSearch] = useState('');
    const [pets, setPets] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);
    const [isLoadingPets, setIsLoadingPets] = useState(false);
    const [selectedPet, setSelectedPet] = useState(null);
    const [isModalVisible, setIsModalVisible] = useState(false);

    const openPetModal = (pet) => {
        setSelectedPet(pet);
        setIsModalVisible(true);
    };

    const closePetModal = () => {
        setIsModalVisible(false);
        setSelectedPet(null);
    };

    const fetchPets = useCallback(async () => {
        setIsLoadingPets(true);
        try {
            const { ongId } = await getSession();
            if (!ongId) {
                return;
            }

            const result = await listarPetsDaOng(ongId, currentPage, 10);

            if (!result?.data) {
                console.error('Erro: resposta invalida ao listar pets', result);
                return;
            }

            const content = result.data.content || [];

            const firstImageUrl = resolveImageUri(content[0]?.imageUrl?.[0]);
            if (firstImageUrl) {
                try {
                    const resp = await fetch(firstImageUrl);
                    if (!resp.ok) {
                        console.error('Imagem nao acessivel:', firstImageUrl, 'status:', resp.status);
                    }
                } catch (err) {
                    console.error('Erro ao acessar imagem:', firstImageUrl, err);
                }
            }

            setPets(content);
            setTotalPages(Number(result.data.totalPages ?? 0));
            setTotalElements(Number(result.data.totalElements ?? content.length));
        } catch (error) {
            console.error('Erro ao listar pets da ONG:', error);
        } finally {
            setIsLoadingPets(false);
        }
    }, [currentPage]);

    useEffect(() => {
        fetchPets();
    }, [fetchPets, refresh]);

    useFocusEffect(
        useCallback(
            () => {
                fetchPets();
            },
            [fetchPets]
        )
    );

    const filteredPets = useMemo(() => {
        const term = search.trim().toLowerCase();

        if (!term) {
            return pets;
        }

        return pets.filter((pet) => String(pet?.petNome ?? '').toLowerCase().includes(term));
    }, [search, pets]);

    const shouldShowPagination = totalElements > 15 && totalPages > 1;

    const handlePreviousPage = () => {
        setCurrentPage((prev) => Math.max(0, prev - 1));
    };

    const handleNextPage = () => {
        setCurrentPage((prev) => Math.min(totalPages - 1, prev + 1));
    };

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <PetSearchInput
                value={search}
                onChangeText={setSearch}
                placeholder="Pesquisar por nome"
            />

            <View style={styles.list}>
                {isLoadingPets ? (
                    <View style={styles.loadingContainer}>
                        <ActivityIndicator size="large" color={colors.mauve} />
                        <Text style={styles.loadingText}>Carregando pets...</Text>
                    </View>
                ) : filteredPets.length > 0 ? (
                    filteredPets.map((pet) => (
                        <View key={pet.petId} style={styles.cardCell}>
                            <PetCard
                                id={pet.petId}
                                name={pet.petNome}
                                imageUrl={pet?.imageUrl?.[0]}
                                imageUrls={pet?.imageUrl || []}
                                idade={pet?.idade}
                                porte={pet?.porte}
                                tags={pet?.tags || []}
                                descricao={pet?.descricao}
                                isCastrado={Boolean(pet?.isCastrado)}
                                isVermifugo={Boolean(pet?.isVermifugo)}
                                isVacinado={Boolean(pet?.isVacinado)}
                                sexo={pet?.sexo}
                                isAdopted={Array.isArray(pet?.status) && pet.status.includes('ADOPTED')}
                                onPress={() => openPetModal(pet)}
                            />
                        </View>
                    ))
                ) : (
                    <Text style={styles.emptyState}>Nenhum pet encontrado.</Text>
                )}
            </View>

            {shouldShowPagination ? (
                <View style={styles.paginationContainer}>
                    <Pressable
                        style={[styles.paginationButton, currentPage === 0 && styles.paginationButtonDisabled]}
                        onPress={handlePreviousPage}
                        disabled={currentPage === 0 || isLoadingPets}
                    >
                        <Text style={styles.paginationButtonText}>Anterior</Text>
                    </Pressable>

                    <Text style={styles.paginationInfo}>Página {currentPage + 1} de {totalPages}</Text>

                    <Pressable
                        style={[
                            styles.paginationButton,
                            (currentPage >= totalPages - 1 || isLoadingPets) && styles.paginationButtonDisabled,
                        ]}
                        onPress={handleNextPage}
                        disabled={currentPage >= totalPages - 1 || isLoadingPets}
                    >
                        <Text style={styles.paginationButtonText}>Próxima</Text>
                    </Pressable>
                </View>
            ) : null}

            <PetModalFlow
                visible={isModalVisible}
                onClose={closePetModal}
                pet={selectedPet}
                onRefresh={fetchPets}
            />
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    container: {
        padding: 8,
        backgroundColor: colors.white,
        flexGrow: 1,
    },
    title: {
        fontSize: 24,
        fontWeight: '700',
        color: colors.textStrong,
        marginBottom: 8,
    },
    subtitle: {
        fontSize: 14,
        color: colors.mauve,
        marginBottom: 14,
    },
    list: {
        marginTop: 14,
        flexDirection: 'row',
        flexWrap: 'wrap',
        justifyContent: 'space-between',
        rowGap: 10,
    },
    cardCell: {
        width: '48%',
    },
    emptyState: {
        fontSize: 14,
        color: colors.mauve,
        textAlign: 'center',
        marginTop: 16,
    },
    loadingContainer: {
        width: '100%',
        alignItems: 'center',
        justifyContent: 'center',
        paddingVertical: 24,
        gap: 8,
    },
    loadingText: {
        fontSize: 14,
        color: colors.mauve,
    },
    paginationContainer: {
        marginTop: 14,
        marginBottom: 8,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: 8,
    },
    paginationButton: {
        borderWidth: 1,
        borderColor: colors.roseBorder,
        borderRadius: 10,
        paddingHorizontal: 12,
        paddingVertical: 8,
        backgroundColor: colors.white,
    },
    paginationButtonDisabled: {
        opacity: 0.45,
    },
    paginationButtonText: {
        color: colors.textStrong,
        fontWeight: '600',
        fontSize: 13,
    },
    paginationInfo: {
        color: colors.mauve,
        fontSize: 13,
        fontWeight: '600',
    },
});
