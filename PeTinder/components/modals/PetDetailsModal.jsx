import { useEffect, useMemo, useState } from 'react';
import { Image, StyleSheet, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { colors } from '../../constants/theme';
import GenericModal from '../GenericModal';
import DynamicButton from '../DynamicButton';
import { resolveImageUri } from '../../utils/imageUri';

export default function PetDetailsModal({
    visible,
    onClose,
    onDelete,
    onEdit,
    onOpenStatus,
    pet,
}) {
    const [imageError, setImageError] = useState(false);
    const rawImageUrl = Array.isArray(pet?.imageUrl) ? pet.imageUrl[0] : pet?.imageUrl;
    const modalImageUrl = useMemo(() => resolveImageUri(rawImageUrl), [rawImageUrl]);
    const isAdopted = Array.isArray(pet?.status) && pet.status.includes('ADOPTED');

    useEffect(() => {
        setImageError(false);
    }, [modalImageUrl]);

    return (
        <GenericModal
            visible={visible}
            onClose={onClose}
            title={pet?.petNome || 'Pet'}
            titleStyle={{ fontSize: 24 }}
        >
            <View style={[styles.imageContainer, isAdopted && styles.isAdopted]}>
                {modalImageUrl && !imageError ? (
                    <Image
                        source={{ uri: modalImageUrl }}
                        style={styles.image}
                        resizeMode="cover"
                        onError={() => setImageError(true)}
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
                    onPress={onDelete}
                    style={styles.modalActionButton}
                    textStyle={{ color: '#80465D', fontSize: 16 }}
                >
                    Deletar Pet
                </DynamicButton>

                <DynamicButton
                    variant="modal-primary"
                    onPress={onEdit}
                    style={styles.modalActionButton}
                    textStyle={{ color: '#fff', fontSize: 16 }}
                >
                    Editar Pet
                </DynamicButton>
            </View>

            <View style={styles.modalActions}>
                <DynamicButton
                    variant="tertiary"
                    textStyle={{ color: '#80465D' }}
                    onPress={onOpenStatus}
                    style={{ marginBottom: 10 }}
                >
                    Mudar Status de Adoção
                </DynamicButton>
            </View>
        </GenericModal>
    );
}

const styles = StyleSheet.create({
    imageContainer: {
        alignSelf: 'center',
        height: 270,
        width: '95%',
        borderWidth: 6,
        borderColor: colors.mauve,
        backgroundColor: colors.mauve,
        borderRadius: 12,
        overflow: 'hidden',
        alignItems: 'center',
        marginBottom: 6,
    },
    isAdopted: {
        borderColor: colors.strongPink,
        backgroundColor: colors.strongPink,
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
    modalActions: {
        flexDirection: 'row',
        justifyContent: 'center',
        gap: 10,
        marginTop: 4,
    },
    modalActionButton: {
        width: '48%',
    },
});
