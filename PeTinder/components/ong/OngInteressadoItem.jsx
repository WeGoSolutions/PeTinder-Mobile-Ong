import { Image, Pressable, StyleSheet, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';
import { colors } from '../../constants/theme';

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
                    <Text style={styles.name} numberOfLines={1}>{name}</Text>
                    <Text style={styles.petInfo} numberOfLines={1}>Interessado em: {petName}</Text>
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
                    <Ionicons name="chevron-forward" size={28} color={colors.black} />
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
        borderColor: colors.roseBorder,
        borderRadius: 12,
        backgroundColor: colors.white,
        padding: 10,
        paddingRight: 56,
        gap: 10,
        justifyContent: 'flex-start',
        position: 'relative',
    },
    avatar: {
        width: 44,
        height: 44,
        borderRadius: 22,
    },
    textContainer: {
        flex: 1,
        minWidth: 0,
    },
    name: {
        fontSize: 15,
        fontWeight: '700',
        color: colors.textStrong,
    },
    petInfo: {
        fontSize: 13,
        color: colors.textDefault,
        marginTop: 2,
    },
    headerContainer: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        gap: 10,
        flex: 1,
        minWidth: 0,
    },
    redirecionarContainer: {
        position: 'absolute',
        right: 10,
        top: 0,
        bottom: 0,
        justifyContent: 'center',
        alignItems: 'center',
        width: 42,
    },
    forwardButton: {
        width: 42,
        height: 42,
        alignItems: 'center',
        justifyContent: 'center',
    },
});
