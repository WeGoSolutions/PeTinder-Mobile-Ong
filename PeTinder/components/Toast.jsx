import { StyleSheet, Text, View } from 'react-native';
import { colors } from '../constants/theme';

export default function Toast({ visible, title, message, type = 'info' }) {
    if (!visible) {
        return null;
    }

    return (
        <View
            pointerEvents="none"
            style={[
                styles.toast,
                type === 'success' && styles.toastSuccess,
                type === 'error' && styles.toastError,
                type === 'warning' && styles.toastWarning,
            ]}
        >
            <Text style={styles.toastTitle}>{title}</Text>
            <Text style={styles.toastMessage}>{message}</Text>
        </View>
    );
}

const styles = StyleSheet.create({
    toast: {
        position: 'absolute',
        left: 16,
        right: 16,
        bottom: 24,
        borderRadius: 12,
        paddingHorizontal: 14,
        paddingVertical: 12,
        borderWidth: 1,
        backgroundColor: '#E9EEF8',
        borderColor: '#8CA3CC',
        shadowColor: '#000',
        shadowOpacity: 0.12,
        shadowRadius: 8,
        shadowOffset: { width: 0, height: 4 },
        elevation: 5,
    },
    toastSuccess: {
        backgroundColor: '#E5F6EA',
        borderColor: '#7EC392',
    },
    toastError: {
        backgroundColor: '#FBEAEA',
        borderColor: '#E28B8B',
    },
    toastWarning: {
        backgroundColor: '#FFF4DF',
        borderColor: '#E7B867',
    },
    toastTitle: {
        fontSize: 13,
        fontWeight: '700',
        color: colors.textStrong,
    },
    toastMessage: {
        marginTop: 2,
        fontSize: 12,
        color: colors.textStrong,
    },
});