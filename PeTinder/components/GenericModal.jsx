import { Modal, Pressable, StyleSheet, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { colors } from '../constants/theme';

export default function GenericModal({ visible, title, onClose, children, titleStyle }) {
    return (
        <Modal
            visible={visible}
            animationType="fade"
            transparent
            onRequestClose={onClose}
        >
            <View style={styles.overlay}>
                <Pressable style={styles.backdrop} onPress={onClose} />

                <View style={styles.modalCard}>
                    <View style={styles.header}>
                        {typeof title === 'string' && title.trim().length > 0 ? (
                            <Text style={[styles.title, titleStyle]}>{title}</Text>
                        ) : null}
                        <Pressable
                            style={styles.closeButton}
                            onPress={onClose}
                            accessibilityRole="button"
                            accessibilityLabel="Fechar modal"
                        >
                            <Ionicons name="close" size={22} color={colors.textStrong} />
                        </Pressable>
                    </View>

                    <View style={styles.content}>{children}</View>
                </View>
            </View>
        </Modal>
    );
}

const styles = StyleSheet.create({
    overlay: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 20,
    },
    backdrop: {
        ...StyleSheet.absoluteFillObject,
        backgroundColor: 'rgba(0, 0, 0, 0.35)',
    },
    modalCard: {
        width: '100%',
        maxWidth: 440,
        backgroundColor: colors.white,
        borderRadius: 14,
        borderWidth: 1,
        borderColor: colors.roseBorder,
        padding: 14,
        gap: 10,
    },
    header: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        position: 'relative',
        minHeight: 28,
    },
    title: {
        fontSize: 18,
        fontWeight: '700',
        color: colors.textStrong,
        textAlign: 'center',
        width: '100%',
        paddingHorizontal: 28,
    },
    closeButton: {
        position: 'absolute',
        right: 0,
        top: 0,
    },
    content: {
        gap: 10,
    },
});