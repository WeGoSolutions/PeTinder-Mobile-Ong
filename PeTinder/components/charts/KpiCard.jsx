import { View, Text, StyleSheet } from 'react-native';
import { colors, scaleWidth, scaleHeight, scaleFont } from '../../constants/theme';

/**
 * KPI Card component for global dashboard
 * Displays a single KPI value with label and optional suffix
 */
export default function KpiCard({ label, value, suffix = '', color = colors.primaryPink }) {
    const displayValue = value !== null && value !== undefined
        ? typeof value === 'number'
            ? Number.isInteger(value) ? value.toLocaleString('pt-BR') : value.toFixed(1)
            : String(value)
        : '--';

    return (
        <View style={[styles.card, { borderLeftColor: color }]}>
            <Text style={[styles.value, { color }]} numberOfLines={1}>
                {displayValue}{suffix}
            </Text>
            <Text style={styles.label} numberOfLines={2}>
                {label}
            </Text>
        </View>
    );
}

const styles = StyleSheet.create({
    card: {
        backgroundColor: colors.white,
        borderRadius: 12,
        paddingVertical: scaleHeight(14),
        paddingHorizontal: scaleWidth(12),
        width: '47%',
        marginBottom: scaleHeight(12),
        borderLeftWidth: 4,
        shadowColor: colors.black,
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.08,
        shadowRadius: 4,
        elevation: 3,
    },
    value: {
        fontSize: scaleFont(20),
        fontFamily: 'Poppins_700Bold',
        marginBottom: scaleHeight(4),
    },
    label: {
        fontSize: scaleFont(11),
        fontFamily: 'Poppins_400Regular',
        color: colors.black,
        opacity: 0.7,
    },
});
