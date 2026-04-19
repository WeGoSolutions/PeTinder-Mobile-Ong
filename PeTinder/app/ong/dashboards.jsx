import { useState } from 'react';
import { View, StyleSheet, ScrollView, ActivityIndicator, Text } from 'react-native';
import { useDashboardData } from '../../services/dashboardService';
import { colors, scaleHeight } from '../../constants/theme';
import DonutChart from '../../components/charts/DonutChart';
import LikesBarChart from '../../components/charts/LikesBarChart';
import OngPendenciasList from '../../components/ong/OngPendenciasList';

export default function Dashboards() {
    const [donutResetSignal, setDonutResetSignal] = useState(0);
    const { pets, adoptedCount, notAdoptedCount, pendingPets, loading, error } = useDashboardData();

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
                <Text style={styles.errorText}>Erro ao carregar dados. Tente novamente.</Text>
            </View>
        );
    }

    return (
        <View style={styles.container}>
            <ScrollView
                style={styles.content}
                contentContainerStyle={styles.contentContainer}
                showsVerticalScrollIndicator={false}
                onTouchStart={() => setDonutResetSignal((prev) => prev + 1)}
            >
                <DonutChart
                    adoptedCount={adoptedCount}
                    notAdoptedCount={notAdoptedCount}
                    resetSignal={donutResetSignal}
                />

                <LikesBarChart
                    pets={pets}
                    maxVisibleBars={6}
                />

                <OngPendenciasList pets={pendingPets} />
            </ScrollView>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: colors.white,
    },
    content: {
        flex: 1,
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
        fontSize: 16,
        textAlign: 'center',
    },
});
