import { View, Text, StyleSheet } from 'react-native';
import { PieChart } from 'react-native-gifted-charts';
import { colors, scaleWidth, scaleHeight, scaleFont } from '../../constants/theme';

/**
 * Donut chart showing distribution by final status
 */
export default function StatusDonutChart({ data = [] }) {
    if (!data || data.length === 0) {
        return (
            <View style={styles.container}>
                <Text style={styles.title}>Distribuição por Status</Text>
                <Text style={styles.emptyText}>Sem dados disponíveis</Text>
            </View>
        );
    }

    const statusColors = {
        'Adotado': '#4CAF50',
        'Lar Temporário': '#FF9800',
        'Eutanasiado': '#F44336',
        'Transferido': '#2196F3',
        'Encontrado Morto': '#9E9E9E',
        'Em andamento': colors.primaryPink,
    };

    const total = data.reduce((sum, item) => sum + item.quantidade, 0);

    const pieData = data.map((item) => ({
        value: item.quantidade,
        color: statusColors[item.status] || colors.mauve,
        text: `${Math.round((item.quantidade / total) * 100)}%`,
    }));

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Distribuição por Status</Text>
            <View style={styles.chartRow}>
                <PieChart
                    data={pieData}
                    donut
                    radius={scaleWidth(70)}
                    innerRadius={scaleWidth(45)}
                    centerLabelComponent={() => (
                        <View style={styles.centerLabel}>
                            <Text style={styles.centerValue}>{total.toLocaleString('pt-BR')}</Text>
                            <Text style={styles.centerText}>Total</Text>
                        </View>
                    )}
                />
                <View style={styles.legend}>
                    {data.map((item, index) => (
                        <View key={index} style={styles.legendItem}>
                            <View style={[styles.legendDot, { backgroundColor: statusColors[item.status] || colors.mauve }]} />
                            <Text style={styles.legendText} numberOfLines={1}>
                                {item.status} ({item.quantidade})
                            </Text>
                        </View>
                    ))}
                </View>
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
    chartRow: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
    },
    centerLabel: {
        alignItems: 'center',
        justifyContent: 'center',
    },
    centerValue: {
        fontSize: scaleFont(16),
        fontFamily: 'Poppins_700Bold',
        color: colors.black,
    },
    centerText: {
        fontSize: scaleFont(10),
        fontFamily: 'Poppins_400Regular',
        color: colors.black,
        opacity: 0.6,
    },
    legend: {
        flex: 1,
        marginLeft: scaleWidth(16),
    },
    legendItem: {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: scaleHeight(6),
    },
    legendDot: {
        width: 10,
        height: 10,
        borderRadius: 5,
        marginRight: scaleWidth(8),
    },
    legendText: {
        fontSize: scaleFont(11),
        fontFamily: 'Poppins_400Regular',
        color: colors.black,
        flex: 1,
    },
    emptyText: {
        fontSize: scaleFont(13),
        color: colors.black,
        opacity: 0.5,
        textAlign: 'center',
        paddingVertical: scaleHeight(20),
    },
});
