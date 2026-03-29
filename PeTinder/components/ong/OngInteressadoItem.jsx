import { Image, Pressable, StyleSheet, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';

const FALLBACK_AVATAR = 'https://i.pravatar.cc/100?img=12';

export default function OngInteressadoItem({ name, petName, avatarUri, userId, petId }) {
    const router = useRouter();

    return (
        <View style={styles.item}>
            <View style={styles.headerContainer}>
                <Image
                    source={{ uri: avatarUri || FALLBACK_AVATAR }}
                    style={styles.avatar}
                />
                <View style={styles.textContainer}>
                    <Text style={styles.name}>{name}</Text>
                    <Text style={styles.petInfo}>Interessado em: {petName}</Text>
                </View>
            </View>
            <View style={styles.redirecionarContainer}>
                <Pressable
                    style={styles.forwardButton}
                    onPress={() =>
                        router.push({
                            pathname: '/ong/chat',
                            params: {
                                userId: String(userId ?? ''),
                                petId: String(petId ?? ''),
                                userName: String(name ?? ''),
                            },
                        })
                    }
                    accessibilityRole="button"
                    accessibilityLabel="Ir para chat"
                >
                    <Ionicons name="chevron-forward" size={28} color="#000000" />
                </Pressable>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    item: {
        flexDirection: 'row',
        alignItems: 'center',
        borderWidth: 1,
        borderColor: '#E8C8D5',
        borderRadius: 12,
        backgroundColor: '#FFFFFF',
        padding: 10,
        gap: 10,
        justifyContent: 'space-between',
    },
    avatar: {
        width: 44,
        height: 44,
        borderRadius: 22,
    },
    textContainer: {
        flex: 1
    },
    name: {
        fontSize: 15,
        fontWeight: '700',
        color: '#1A1A1A',
    },
    petInfo: {
        fontSize: 13,
        color: '#222222',
        marginTop: 2,
    },
    headerContainer: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        gap: 10
    },
    redirecionarContainer: {
        justifyContent: 'center',
        alignItems: 'center',
    },
    forwardButton: {
        width: 42,
        height: 42,
        alignItems: 'center',
        justifyContent: 'center',
    },
});
