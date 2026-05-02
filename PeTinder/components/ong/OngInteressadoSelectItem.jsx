import { useEffect, useMemo, useState } from 'react';
import { Image, Pressable, StyleSheet, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { colors } from '../../constants/theme';
import { resolveImageUri } from '../../utils/imageUri';

export default function OngInteressadoSelectItem({
    name,
    avatarUri,
    onPress,
    accessibilityLabel = 'Selecionar interessado',
}) {
    const [imageError, setImageError] = useState(false);
    const resolvedImageUrl = useMemo(() => resolveImageUri(avatarUri), [avatarUri]);

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
                    <Text style={styles.name} numberOfLines={1}>{name || 'Usuario'}</Text>
                </View>
            </View>
            <View style={styles.redirecionarContainer}>
                <Pressable
                    style={styles.forwardButton}
                    onPress={onPress}
                    accessibilityRole="button"
                    accessibilityLabel={accessibilityLabel}
                >
                    <Ionicons name="chevron-forward" size={30} color={colors.black} />
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
        padding: 12,
        paddingRight: 60,
        gap: 12,
        justifyContent: 'flex-start',
        position: 'relative',
    },
    avatar: {
        width: 50,
        height: 50,
        borderRadius: 25,
    },
    avatarPlaceholder: {
        width: 50,
        height: 50,
        borderRadius: 25,
        backgroundColor: colors.roseSurface,
        alignItems: 'center',
        justifyContent: 'center',
    },
    textContainer: {
        flex: 1,
        minWidth: 0,
    },
    name: {
        fontSize: 16,
        fontWeight: '700',
        color: colors.textStrong,
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
        right: 12,
        top: 0,
        bottom: 0,
        justifyContent: 'center',
        alignItems: 'center',
        width: 46,
    },
    forwardButton: {
        width: 46,
        height: 46,
        alignItems: 'center',
        justifyContent: 'center',
    },
});
