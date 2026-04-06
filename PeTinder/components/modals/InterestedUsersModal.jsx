import { Image, Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { colors } from '../../constants/theme';
import GenericModal from '../GenericModal';

export default function InterestedUsersModal({
    visible,
    onClose,
    petName,
    interestedUsers,
    isLoading,
    onSelectUser,
}) {
    return (
        <GenericModal
            visible={visible}
            onClose={onClose}
            title={`Usuários interessados no(a) ${petName || ''}:`}
        >
            {isLoading ? (
                <Text style={styles.emptyText}>Carregando interessados...</Text>
            ) : interestedUsers.length === 0 ? (
                <View style={styles.emptyStateContainer}>
                    <Ionicons name="search-outline" size={34} color={colors.mauve} />
                    <Text style={styles.emptyText}>
                        Ainda não temos nenhum interessado, mas não se preocupe, em pouco tempo irão aparecer!
                    </Text>
                </View>
            ) : (
                <ScrollView style={styles.list} contentContainerStyle={styles.listContent}>
                    {interestedUsers.map((user, index) => (
                        <Pressable
                            key={`${user.userId || 'interessado'}-${index}`}
                            style={styles.item}
                            onPress={() => onSelectUser(user.userId)}
                        >
                            {user.imageUrl ? (
                                <Image source={{ uri: user.imageUrl }} style={styles.avatar} />
                            ) : (
                                <View style={styles.avatarPlaceholder}>
                                    <Ionicons name="person-outline" size={20} color={colors.mauve} />
                                </View>
                            )}

                            <View style={styles.textContent}>
                                <Text style={styles.userName} numberOfLines={1}>
                                    {user.userName || 'Usuário'}
                                </Text>
                                <Text style={styles.caption} numberOfLines={1}>
                                    Toque para marcar como adotado
                                </Text>
                            </View>

                            <Ionicons name="checkmark-circle-outline" size={22} color={colors.mauve} />
                        </Pressable>
                    ))}
                </ScrollView>
            )}
        </GenericModal>
    );
}

const styles = StyleSheet.create({
    list: {
        maxHeight: 320,
    },
    listContent: {
        gap: 10,
    },
    item: {
        flexDirection: 'row',
        alignItems: 'center',
        borderWidth: 1,
        borderColor: colors.roseBorder,
        borderRadius: 12,
        backgroundColor: colors.white,
        padding: 10,
        gap: 10,
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
    textContent: {
        flex: 1,
        minWidth: 0,
    },
    userName: {
        fontSize: 15,
        fontWeight: '700',
        color: colors.textStrong,
    },
    caption: {
        fontSize: 12,
        color: colors.mauve,
        marginTop: 2,
    },
    emptyText: {
        fontSize: 14,
        color: colors.mauve,
        textAlign: 'center',
        marginVertical: 10,
    },
    emptyStateContainer: {
        alignItems: 'center',
        justifyContent: 'center',
        gap: 8,
        paddingVertical: 8,
    },
});
