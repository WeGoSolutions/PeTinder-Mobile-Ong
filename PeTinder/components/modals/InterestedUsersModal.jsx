import { ScrollView, StyleSheet, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { colors } from '../../constants/theme';
import GenericModal from '../GenericModal';
import OngInteressadoSelectItem from '../ong/OngInteressadoSelectItem';

export default function InterestedUsersModal({
    visible,
    onClose,
    petName,
    interestedUsers,
    isLoading,
    onSelectUser,
}) {
    const users = Array.isArray(interestedUsers) ? interestedUsers : [];

    return (
        <GenericModal
            visible={visible}
            onClose={onClose}
            title={`Usuários interessados no(a) ${petName || ''}:`}
            modalCardStyle={styles.modalCard}
            contentStyle={styles.modalContent}
        >
            {isLoading ? (
                <Text style={styles.emptyText}>Carregando interessados...</Text>
            ) : users.length === 0 ? (
                <View style={styles.emptyStateContainer}>
                    <Ionicons name="search-outline" size={34} color={colors.mauve} />
                    <Text style={styles.emptyText}>
                        Ainda não temos nenhum interessado, mas não se preocupe, em pouco tempo irão aparecer!
                    </Text>
                </View>
            ) : (
                <ScrollView
                    style={styles.list}
                    contentContainerStyle={styles.listContent}
                    showsVerticalScrollIndicator
                    persistentScrollbar
                    indicatorStyle="black"
                >
                    {users.map((user, index) => (
                        <OngInteressadoSelectItem
                            key={`${user.userId || 'interessado'}-${index}`}
                            name={user.userName || 'Usuario'}
                            avatarUri={user.imageUrl}
                            accessibilityLabel="Selecionar interessado"
                            onPress={() => onSelectUser(user.userId)}
                        />
                    ))}
                </ScrollView>
            )}
        </GenericModal>
    );
}

const styles = StyleSheet.create({
    modalCard: {
        minHeight: '45%',
        maxHeight: '45%',
    },
    modalContent: {
        flex: 1,
        minHeight: 0,
    },
    list: {
        flex: 1,
        minHeight: 0,
        marginTop: 8
    },
    listContent: {
        gap: 10,
        paddingBottom: 20,
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
