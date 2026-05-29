import { useCallback } from 'react';
import { View, Text, StyleSheet, ScrollView, ActivityIndicator } from 'react-native';
import { useFocusEffect } from 'expo-router';
import { useGlobalDashboardData } from '../../services/globalDashboardService';
import { colors, scaleWidth, scaleHeight, scaleFont } from '../../constants/theme';
import KpiCard from '../../components/charts/KpiCard';
import EspeciesBarChart from '../../components/charts/EspeciesBarChart';
import StatusDonutChart from '../../components/charts/StatusDonutChart';
import FaixaEtariaChart from '../../components/charts/FaixaEtariaChart';

export default function GlobalDashboard() {
    const {
        kpis,
        especiesData,
        statusData,
        motivosRetornoData,
        faixaEtariaData,
        loading,
        error,
        refetch,
    } = useGlobalDashboardData();

    useFocusEffect(
        useCallback(() => {
            refetch();
        }, [refetch])
    );

    if (loading) {
        return (
            <View style={styles.loadingContainer}>
                <ActivityIndicator size="large" color={colors.primaryPink} />
            </View>
        );
    }

    if (error) {
        return (
            <View style={styles.errorContainer}>
                <Text style={styles.errorText}>Erro ao carregar dados globais.</Text>
                <Text style={styles.errorSubtext}>{error}</Text>
            </View>
        );
    }

    return (
        <ScrollView
            style={styles.container}
            contentContainerStyle={styles.contentContainer}
            showsVerticalScrollIndicator={false}
        >
            <Text style={styles.sectionTitle}>Indicadores Globais</Text>
            <View style={styles.kpiGrid}>
                <KpiCard
                    label="Total de Entradas"
                    value={kpis?.totalEntradas}
                    color={colors.primaryPink}
                />
                <KpiCard
                    label="Taxa de Adoção"
                    value={kpis?.taxaAdocao}
                    suffix="%"
                    color="#4CAF50"
                />
                <KpiCard
                    label="Tempo Médio de Adoção"
                    value={kpis?.tempoMedioAdocao}
                    suffix=" dias"
                    color="#FF9800"
                />
                <KpiCard
                    label="Taxa de Devolução"
                    value={kpis?.taxaRetorno}
                    suffix="%"
                    color="#F44336"
                />
            </View>

            <EspeciesBarChart data={especiesData} />

            <StatusDonutChart data={statusData} />

            <FaixaEtariaChart data={faixaEtariaData} />
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: colors.white,
    },
    contentContainer: {
        paddingBottom: scaleHeight(20),
    },
    loadingContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: colors.white,
    },
    errorContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: colors.white,
        padding: 20,
    },
    errorText: {
        color: colors.black,
        fontSize: scaleFont(16),
        fontFamily: 'Poppins_600SemiBold',
        textAlign: 'center',
    },
    errorSubtext: {
        color: colors.black,
        fontSize: scaleFont(13),
        fontFamily: 'Poppins_400Regular',
        opacity: 0.6,
        textAlign: 'center',
        marginTop: scaleHeight(8),
    },
    sectionTitle: {
        fontSize: scaleFont(16),
        fontFamily: 'Poppins_600SemiBold',
        color: colors.black,
        marginHorizontal: scaleWidth(16),
        marginTop: scaleHeight(16),
        marginBottom: scaleHeight(12),
    },
    kpiGrid: {
        flexDirection: 'row',
        flexWrap: 'wrap',
        justifyContent: 'space-between',
        paddingHorizontal: scaleWidth(16),
        marginBottom: scaleHeight(8),
    },
});
