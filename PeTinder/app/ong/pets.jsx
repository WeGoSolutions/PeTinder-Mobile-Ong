import { useEffect, useMemo, useState } from 'react';
import { View, Text, StyleSheet, ScrollView, Image } from 'react-native';
import { colors } from '../../constants/theme';
import PetSearchInput from '../../components/ong/PetSearchInput';
import PetCard from '../../components/ong/PetCard';
import GenericModal from '../../components/GenericModal';
import { listarPetsDaOng } from '../../services/petApiService';
import { getSession } from '../../services/sessionService';
import { useRouter } from 'expo-router';
import DynamicButton from '../../components/DynamicButton';
import { Ionicons } from '@expo/vector-icons';


export default function Pets() {
    const router = useRouter();
    const [search, setSearch] = useState('');
    const [pets, setPets] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [selectedPet, setSelectedPet] = useState(null);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const modalImageUrl = selectedPet?.imageUrl?.[0] || '';
    const isSelectedPetAdopted = Array.isArray(selectedPet?.status) && selectedPet.status.includes('ADOPTED');

    const openPetModal = (pet) => {
        setSelectedPet(pet);
        setIsModalVisible(true);
    };

    const closePetModal = () => {
        setIsModalVisible(false);
        setSelectedPet(null);
    };

    const handleGoToEdit = () => {
        if (!selectedPet) {
            return;
        }

        router.push({
            pathname: '/ong/petForm',
            params: {
                mode: 'edit',
                petId: String(selectedPet.petId),
                from: '/ong/pets',
                nome: selectedPet.petNome || '',
                idade: selectedPet.idade !== undefined && selectedPet.idade !== null ? String(selectedPet.idade) : '',
                porte: selectedPet.porte || '',
                tags: (selectedPet.tags || []).join('|'),
                descricao: selectedPet.descricao || '',
                isCastrado: String(Boolean(selectedPet.isCastrado)),
                isVermifugo: String(Boolean(selectedPet.isVermifugo)),
                isVacinado: String(Boolean(selectedPet.isVacinado)),
                sexo: selectedPet.sexo || '',
                imageUrls: (selectedPet.imageUrl || []).join('|'),
            },
        });

        closePetModal();
    };

    useEffect(() => {
        const fetchPets = async () => {
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

                const firstImageUrl = content[0]?.imageUrl?.[0];
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
                setTotalPages(result.data.totalPages ?? 0);
                setTotalElements(result.data.totalElements ?? 0);
            } catch (error) {
                console.error('Erro ao listar pets da ONG:', error);
            }
        };

        fetchPets();
    }, [currentPage]);

    const filteredPets = useMemo(() => {
        const term = search.trim().toLowerCase();

        if (!term) {
            return pets;
        }

        return pets.filter((pet) => String(pet?.petNome ?? '').toLowerCase().includes(term));
    }, [search, pets]);

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <PetSearchInput
                value={search}
                onChangeText={setSearch}
                placeholder="Pesquisar por nome"
            />

            <View style={styles.list}>
                {filteredPets.length > 0 ? (
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

            <GenericModal
                visible={isModalVisible}
                onClose={closePetModal}
                title={selectedPet?.petNome || 'Pet'}
            >
                <View style={[styles.imageContainer, isSelectedPetAdopted && styles.isAdopted]}>
                    {modalImageUrl ? (
                        <Image
                            source={{ uri: modalImageUrl }}
                            style={styles.image}
                            resizeMode="cover"
                        />
                    ) : (
                        <View style={styles.placeholderContainer}>
                            <Ionicons name="image-outline" size={32} color={colors.mauve} />
                        </View>
                    )}
                </View>

                <View style={styles.modalActions}>
                    <DynamicButton
                        variant="modal-secondary"
                        onPress={closePetModal}
                        style={styles.modalActionButton}
                        textStyle={{ color: '#80465D' }}
                    >
                        Deletar Pet
                    </DynamicButton>

                    <DynamicButton
                        variant="modal-primary"
                        onPress={handleGoToEdit}
                        style={styles.modalActionButton}
                        textStyle={{ color: '#fff' }}
                    >
                        Editar Pet
                    </DynamicButton>
                </View>
                <View style={styles.modalActions}>
                    <DynamicButton
                        variant="tertiary"
                        textStyle={{ color: '#80465D' }}
                        onPress={() => console.log('Forgot password')}
                        style={{ marginBottom: 10 }}
                    >
                        Mudar Status de Adoção
                    </DynamicButton>
                </View>
            </GenericModal>
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
    modalText: {
        fontSize: 14,
        color: colors.textStrong,
    },
    modalActions: {
        flexDirection: 'row',
        justifyContent: 'center',
        gap: 10,
        marginTop: 4,
    },
    modalActionButton: {
        width: '48%',
    },
    imageContainer: {
        alignSelf: 'center',
        height: 270,
        width: '95%',
        borderWidth: 6,
        borderColor: colors.mauve,
        backgroundColor: colors.mauve,
        borderRadius: 12,
        overflow: 'hidden',
        alignItems: 'center'
    },
    isAdopted: {
        borderColor: colors.lightMauve,
        backgroundColor: colors.lightMauve,
    },
    image: {
        width: '100%',
        height: '100%',
    },
    placeholderContainer: {
        width: '100%',
        height: '100%',
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: colors.roseSurface,
    },
});
