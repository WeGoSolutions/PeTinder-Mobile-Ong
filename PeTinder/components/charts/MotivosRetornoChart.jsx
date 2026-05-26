import { View, Text, StyleSheet } from 'react-native';
import { BarChart } from 'react-native-gifted-charts';
import { colors, scaleWidth, scaleHeight, scaleFont } from '../../constants/theme';

/**
 * Bar chart showing top reasons for return/devolution
 */
export default function MotivosRetornoChart({ data = [] }) {
    if (!data || data.length === 0) {
        return (
            <View style={styles.container}>
                <Text style={styles.title}>Motivos de Devolução</Text>
                <Text style={styles.emptyText}>Sem dados disponíveis</Text>
            </View>
        );
    }

    const shortenMotivo = (motivo) => {
        if (!motivo) return '';
        const cleaned = motivo.replace('Devolução de adoção - ', '').replace('Devolução de adoção -', '');
        return cleaned.length > 14 ? cleaned.substring(0, 14) + '...' : cleaned;
    };

    const barData = data.map((item) => ({
        value: item.quantidade,
        label: shortenMotivo(item.motivo),
        frontColor: colors.primaryPink,
        topLabelComponent: () => (
            <Text style={styles.barLabel}>{item.quantidade}</Text>
        ),
    }));

    const maxValue = Math.max(...data.map(d => d.quantidade), 1);

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Motivos de Devolução</Text>
            <View style={styles.chartWrapper}>
                <BarChart
                    data={barData}
                    barWidth={scaleWidth(36)}
                    spacing={scaleWidth(14)}
                    roundedTop
                    roundedBottom
                    xAxisThickness={1}
                    yAxisThickness={0}
                    yAxisTextStyle={styles.yAxisText}
                    xAxisLabelTextStyle={styles.xAxisText}
                    noOfSections={4}
                    maxValue={maxValue * 1.2}
                    height={scaleHeight(140)}
                    isAnimated
                />
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        backgroundColor: colors.white,
        borderRadius: 12,
        padding: scaleWidth(16),
        marginHorizontal: scaleWidth(16),
        marginBottom: scaleHeight(16),
        shadowColor: colors.black,
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.08,
        shadowRadius: 4,
        elevation: 3,
    },
    title: {
        fontSize: scaleFont(15),
        fontFamily: 'Poppins_600SemiBold',
        color: colors.black,
        marginBottom: scaleHeight(12),
    },
    chartWrapper: {
        alignItems: 'center',
        overflow: 'hidden',
    },
    barLabel: {
        fontSize: scaleFont(10),
        color: colors.black,
        fontFamily: 'Poppins_500Medium',
        marginBottom: 4,
    },
    yAxisText: {
        fontSize: scaleFont(10),
        color: colors.black,
        opacity: 0.5,
    },
    xAxisText: {
        fontSize: scaleFont(8),
        color: colors.black,
        opacity: 0.7,
        width: scaleWidth(50),
        textAlign: 'center',
    },
    emptyText: {
        fontSize: scaleFont(13),
        color: colors.black,
        opacity: 0.5,
        textAlign: 'center',
        paddingVertical: scaleHeight(20),
    },
});
