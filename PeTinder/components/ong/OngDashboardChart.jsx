import { StyleSheet, Text, View } from 'react-native';

const PLOT_HEIGHT = 120;

export default function OngDashboardChart({
    title,
    labels = ['Thor', 'Luna', 'Mel', 'Tiana', 'Rex', 'Koda', 'Kenny'],
    values = [6, 8, 7, 12, 10, 14, 16],
}) {
    const sortedData = labels
        .map((label, index) => ({
            label,
            value: values[index] ?? 0,
        }))
        .sort((a, b) => b.value - a.value);

    const maxValue = Math.max(...sortedData.map((item) => item.value), 1);
    const barHeights = sortedData.map((item) => {
        const value = item.value;
        if (value <= 0) return 0;
        return Math.max(6, (value / maxValue) * PLOT_HEIGHT);
    });

    return (
        <View>
            {!!title && <Text style={styles.title}>{title}</Text>}
            <View style={styles.chartArea}>
                <View style={styles.plotArea}>
                    {sortedData.map((item, index) => (
                        <View key={`${item.label}-${index}`} style={styles.columnItem}>
                            <View style={styles.barTrack}>
                                <View
                                    style={[
                                        styles.column,
                                        {
                                            height: barHeights[index],
                                        },
                                    ]}
                                />
                            </View>
                            <Text style={styles.valueLabel}>{item.value}</Text>
                            <Text style={styles.axisLabel} numberOfLines={1}>{item.label}</Text>
                        </View>
                    ))}
                </View>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    title: {
        fontSize: 16,
        color: '#1A1A1A',
        fontWeight: '700',
        marginBottom: 8,
    },
    chartArea: {
        height: 220,
        borderRadius: 12,
        borderWidth: 1,
        borderColor: '#E8C8D5',
        backgroundColor: '#FFFFFF',
        paddingHorizontal: 12,
        paddingTop: 12,
        paddingBottom: 12,
    },
    plotArea: {
        flex: 1,
        flexDirection: 'row',
        alignItems: 'flex-end',
        justifyContent: 'space-between',
    },
    columnItem: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'flex-end',
    },
    barTrack: {
        height: PLOT_HEIGHT,
        justifyContent: 'flex-end',
        alignItems: 'center',
    },
    column: {
        width: 18,
        borderRadius: 10,
        backgroundColor: '#80465D',
    },
    valueLabel: {
        fontSize: 11,
        fontWeight: '600',
        color: '#5D2F43',
        marginTop: 4,
    },
    axisLabel: {
        fontSize: 10,
        color: '#6E6E6E',
        marginTop: 2,
    },
});
