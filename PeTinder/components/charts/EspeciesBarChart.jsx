import { View, Text, StyleSheet } from 'react-native';
import { BarChart } from 'react-native-gifted-charts';
import { colors, scaleWidth, scaleHeight, scaleFont } from '../../constants/theme';

/**
 * Horizontal bar chart component for species distribution
 */
export default function EspeciesBarChart({ data = [] }) {
    if (!data || data.length === 0) {
        return (
            <View style={styles.container}>
                <Text style={styles.title}>Entradas por Espécie</Text>
                <Text style={styles.emptyText}>Sem dados disponíveis</Text>
            </View>
        );
    }

    const chartColors = [
        colors.primaryPink,
        colors.mauve,
        colors.strongPink,
        colors.lightMauve,
        colors.buttonPink,
    ];

    const barData = data.map((item, index) => ({
        value: item.quantidade,
        label: item.especie?.length > 8 ? item.especie.substring(0, 8) + '...' : item.especie,
        frontColor: chartColors[index % chartColors.length],
        topLabelComponent: () => (
            <Text style={styles.barLabel}>{item.quantidade}</Text>
        ),
    }));

    const maxValue = Math.max(...data.map(d => d.quantidade), 1);

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Entradas por Espécie</Text>
            <View style={styles.chartWrapper}>
                <BarChart
                    data={barData}
                    barWidth={scaleWidth(38)}
                    spacing={scaleWidth(20)}
                    roundedTop
                    roundedBottom
                    xAxisThickness={1}
                    yAxisThickness={0}
                    yAxisTextStyle={styles.yAxisText}
                    xAxisLabelTextStyle={styles.xAxisText}
                    noOfSections={4}
                    maxValue={maxValue * 1.2}
                    height={scaleHeight(160)}
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
        fontSize: scaleFont(9),
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
