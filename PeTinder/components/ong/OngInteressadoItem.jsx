import { useEffect, useMemo, useState } from 'react';
import { Image, Pressable, StyleSheet, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';
import { colors } from '../../constants/theme';
import { resolveImageUri } from '../../utils/imageUri';

export default function OngInteressadoItem({
    userName,
    petNome,
    userEmail,
    imageUrl,
    userId,
    petId,
}) {
    const router = useRouter();
    const displayName = userName || 'Usuario';
    const [imageError, setImageError] = useState(false);
    const resolvedImageUrl = useMemo(() => resolveImageUri(imageUrl), [imageUrl]);

    useEffect(() => {
        setImageError(false);
    }, [resolvedImageUrl]);

    return (
        <View style={styles.item}>
            <View style={styles.headerContainer}>
                {resolvedImageUrl && !imageError ? (
                    <Image
                        source={{ uri: resolvedImageUrl }}
                        style={styles.avatar}
                        onError={() => setImageError(true)}
                    />
                ) : (
                    <View style={styles.avatarPlaceholder}>
                        <Ionicons name="person-outline" size={20} color={colors.mauve} />
                    </View>
                )}
                <View style={styles.textContainer}>
                    <Text style={styles.name} numberOfLines={1}>{displayName}</Text>
                    <Text style={styles.petInfo} numberOfLines={1}>Interessado em: {petNome || '-'}</Text>
                    <Text style={styles.emailInfo} numberOfLines={1}>{userEmail || '-'}</Text>
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
                                userName: String(displayName),
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
    avatarPlaceholder: {
        width: 44,
        height: 44,
        borderRadius: 22,
        backgroundColor: colors.roseSurface,
        alignItems: 'center',
        justifyContent: 'center',
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
    emailInfo: {
        fontSize: 12,
        color: colors.mauve,
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
