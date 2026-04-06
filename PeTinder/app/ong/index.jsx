import {
    View,
    Text,
    StyleSheet,
    ScrollView
} from 'react-native';
import { useRouter } from 'expo-router';
import LikesBarChart from '../../components/charts/LikesBarChart';
import OngInteressadoItem from '../../components/ong/OngInteressadoItem';
import DynamicButton from '../../components/DynamicButton';
import { usePets } from '../../services/petService';

const interessadosMock = [
    {
        id: 'interessado-1',
        userId: 'u-1',
        petId: 'p-2',
        name: 'Catleya Ishii',
        petName: 'Luna',
        avatarUri: 'https://i.pravatar.cc/100?img=24',
    },
    {
        id: 'interessado-2',
        userId: 'u-2',
        petId: 'p-1',
        name: 'Rafael Souza',
        petName: 'Thor',
        avatarUri: 'https://i.pravatar.cc/100?img=14',
    },
    {
        id: 'interessado-3',
        userId: 'u-3',
        petId: 'p-3',
        name: 'Beatriz Lima',
        petName: 'Mel',
        avatarUri: 'https://i.pravatar.cc/100?img=32',
    },
];

export default function Home() {
    const router = useRouter();
    const interessadosList = interessadosMock.slice(0, 3);
    const { pets, adoptedCount, notAdoptedCount, loading: petsLoading, error: petsError } = usePets();

    return (
        <ScrollView
            style={styles.container}
            contentContainerStyle={styles.contentContainer}
            showsVerticalScrollIndicator={false}
        >
            <View style={styles.section}>
                <Text style={styles.title}>Últimos interessados</Text>
                <View style={styles.interessadosList}>
                    {interessadosList.map((interessado) => (
                        <OngInteressadoItem
                            key={interessado.id}
                            name={interessado.name}
                            petName={interessado.petName}
                            avatarUri={interessado.avatarUri}
                            userId={interessado.userId}
                            petId={interessado.petId}
                        />
                    ))}
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
    verMaisContainer: {
        alignItems: 'center',
    },
    verMaisText: {
        color: '#80465D',
        fontSize: 16,
    },
});
