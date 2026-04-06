import { useEffect, useMemo, useState } from 'react';
import { View, Text, StyleSheet, ScrollView } from 'react-native';
import { colors } from '../../constants/theme';
import PetSearchInput from '../../components/ong/PetSearchInput';
import PetCard from '../../components/ong/PetCard';
import { listarPetsDaOng } from '../../services/petApiService';
import { getSession } from '../../services/sessionService';

export default function Pets() {
    const [search, setSearch] = useState('');
    const [pets, setPets] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);

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

                const firstImageUrl = content[0]?.imagensUrls?.[0];
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

        return pets.filter((pet) =>
            String(pet?.name ?? '').toLowerCase().includes(term)
        );
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
                        <PetCard
                            key={pet.id}
                            id={pet.id}
                            name={pet.name}
                            imageUrl={pet?.imagensUrls?.[0]}
                            isAdopted={Boolean(pet?.adopted)}
                        />
                    ))
                ) : (
                    <Text style={styles.emptyState}>Nenhum pet encontrado.</Text>
                )}
            </View>
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
        gap: 10,
    },
    emptyState: {
        fontSize: 14,
        color: colors.mauve,
        textAlign: 'center',
        marginTop: 16,
    },
});
