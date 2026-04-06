import { Image, StyleSheet, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { colors } from '../../constants/theme';
import GenericModal from '../GenericModal';

export default function AdopterInfoModal({
    visible,
    onClose,
    petName,
    adopterInfo,
}) {
    return (
        <GenericModal
            visible={visible}
            onClose={onClose}
            title={`Adotante do(a) ${petName || ''}:`}
        >
            {adopterInfo ? (
                <View style={styles.adotanteInfo}>
                    {adopterInfo.imagemUrl ? (
                        <Image
                            source={{ uri: adopterInfo.imagemUrl }}
                            style={styles.adotanteImage}
                        />
                    ) : (
                        <View style={styles.adotantePlaceholder}>
                            <Ionicons name="person-outline" size={34} color={colors.mauve} />
                        </View>
                    )}
                    <Text style={styles.adotanteName}>{adopterInfo.nomeUsuario || 'Usuário'}</Text>
                    <Text style={styles.adotanteEmail}>Email: {adopterInfo.email || 'Não informado'}</Text>
                </View>
            ) : (
                <Text style={styles.emptyState}>Nenhum adotante encontrado para este pet.</Text>
            )}
        </GenericModal>
    );
}

const styles = StyleSheet.create({
    emptyState: {
        fontSize: 14,
        color: colors.mauve,
        textAlign: 'center',
        marginTop: 16,
    },
    adotanteInfo: {
        alignItems: 'center',
        gap: 8,
        paddingVertical: 4,
    },
    adotanteImage: {
        width: 80,
        height: 80,
        borderRadius: 40,
        backgroundColor: colors.roseSurface,
    },
    adotantePlaceholder: {
        width: 80,
        height: 80,
        borderRadius: 40,
        backgroundColor: colors.roseSurface,
        alignItems: 'center',
        justifyContent: 'center',
    },
    adotanteName: {
        fontSize: 18,
        fontWeight: '700',
        color: colors.textStrong,
        textAlign: 'center',
    },
    adotanteEmail: {
        fontSize: 14,
        color: colors.textStrong,
        textAlign: 'center',
    },
});
