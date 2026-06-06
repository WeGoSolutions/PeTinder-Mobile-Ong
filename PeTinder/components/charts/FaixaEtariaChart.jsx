import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, Pressable } from 'react-native';
import { BarChart } from 'react-native-gifted-charts';
import { colors, scaleWidth, scaleHeight, scaleFont } from '../../constants/theme';

/**
 * Bar chart showing distribution by age group
 */
export default function FaixaEtariaChart({ data = [] }) {
    const [tooltip, setTooltip] = useState({ visible: false, text: '' });

    useEffect(() => {
        if (!tooltip.visible) return;
        const t = setTimeout(() => setTooltip({ visible: false, text: '' }), 1500);
        return () => clearTimeout(t);
    }, [tooltip.visible]);

    if (!data || data.length === 0) {
        return (
            <View style={styles.container}>
                <Text style={styles.title}>Distribuição por Faixa Etária</Text>
                <Text style={styles.emptyText}>Sem dados disponíveis</Text>
            </View>
        );
    }

    const faixaColors = {
        'Adulto': colors.primaryPink,
        'Filhote': '#4CAF50',
        'Recém-nascido': '#FF9800',
        'Não informado': '#9E9E9E',
    };

    const barData = data.map((item) => ({
        value: item.quantidade,
        frontColor: faixaColors[item.faixaEtaria] || colors.mauve,
        topLabelComponent: () => (
            <Text style={styles.barLabel}>{item.quantidade.toLocaleString('pt-BR')}</Text>
        ),
        labelComponent: () => (
            <Pressable
                onLongPress={() => setTooltip({ visible: true, text: item.faixaEtaria || 'N/A' })}
                android_ripple={{ color: 'transparent' }}
            >
                <Text style={styles.xAxisText} numberOfLines={1} ellipsizeMode="tail">{item.faixaEtaria || 'N/A'}</Text>
            </Pressable>
        ),
    }));

    const maxValue = Math.max(...data.map(d => d.quantidade), 1);

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Distribuição por Faixa Etária</Text>
            <View style={styles.chartWrapper}>
                <BarChart
                    data={barData}
                    barWidth={scaleWidth(50)}
                    spacing={scaleWidth(24)}
                    roundedTop
                    roundedBottom
                    xAxisThickness={1}
                    yAxisThickness={0}
                    yAxisTextStyle={styles.yAxisText}
                    xAxisLabelTextStyle={styles.xAxisText}
                    noOfSections={4}
                    maxValue={maxValue * 1.2}
                    height={scaleHeight(150)}
                    isAnimated
                    showScrollIndicator
                />
                {tooltip.visible && (
                    <View style={styles.tooltip} pointerEvents="none">
                        <Text style={styles.tooltipText}>{tooltip.text}</Text>
                    </View>
                )}
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
        width: scaleWidth(60),
        textAlign: 'center',
    },
    emptyText: {
        fontSize: scaleFont(13),
        color: colors.black,
        opacity: 0.5,
        textAlign: 'center',
        paddingVertical: scaleHeight(20),
    },
    tooltip: {
        position: 'absolute',
        bottom: scaleHeight(60),
        alignSelf: 'center',
        backgroundColor: colors.black,
        paddingHorizontal: scaleWidth(10),
        paddingVertical: scaleHeight(6),
        borderRadius: 6,
        zIndex: 10,
    },
    tooltipText: {
        color: colors.white,
        fontSize: scaleFont(11),
        fontFamily: 'Poppins_500Medium',
    },
});
