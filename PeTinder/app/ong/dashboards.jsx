import { useCallback, useState } from 'react';
import { View, StyleSheet, ScrollView, ActivityIndicator, Text, TouchableOpacity } from 'react-native';
import { useFocusEffect } from 'expo-router';
import { useDashboardData } from '../../services/dashboardService';
import { colors, scaleHeight, scaleWidth, scaleFont } from '../../constants/theme';
import DonutChart from '../../components/charts/DonutChart';
import LikesBarChart from '../../components/charts/LikesBarChart';
import OngPendenciasList from '../../components/ong/OngPendenciasList';
import GlobalDashboard from '../../components/charts/GlobalDashboard';

export default function Dashboards() {
    const [activeTab, setActiveTab] = useState('petinder');
    const [donutResetSignal, setDonutResetSignal] = useState(0);
    const { pets, adoptedCount, notAdoptedCount, pendingPets, loading, error, refetch } = useDashboardData();

    useFocusEffect(
        useCallback(() => {
            if (activeTab === 'petinder') {
                refetch();
            }
        }, [refetch, activeTab])
    );

    const renderTabSelector = () => (
        <View style={styles.tabContainer}>
            <TouchableOpacity
                style={[styles.tab, activeTab === 'petinder' && styles.activeTab]}
                onPress={() => setActiveTab('petinder')}
            >
                <Text style={[styles.tabText, activeTab === 'petinder' && styles.activeTabText]}>
                    Dados PeTinder
                </Text>
            </TouchableOpacity>
            <TouchableOpacity
                style={[styles.tab, activeTab === 'global' && styles.activeTab]}
                onPress={() => setActiveTab('global')}
            >
                <Text style={[styles.tabText, activeTab === 'global' && styles.activeTabText]}>
                    Dados Globais
                </Text>
            </TouchableOpacity>
        </View>
    );

    if (activeTab === 'global') {
        return (
            <View style={styles.container}>
                {renderTabSelector()}
                <GlobalDashboard />
            </View>
        );
    }

    if (loading) {
        return (
            <View style={styles.container}>
                {renderTabSelector()}
                <View style={styles.loadingContainer}>
                    <ActivityIndicator size="large" color={colors.primaryPink} />
                </View>
            </View>
        );
    }

    if (error) {
        return (
            <View style={styles.container}>
                {renderTabSelector()}
                <View style={styles.errorContainer}>
                    <Text style={styles.errorText}>Erro ao carregar dados. Tente novamente.</Text>
                </View>
            </View>
        );
    }

    return (
        <View style={styles.container}>
            {renderTabSelector()}
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
                    maxVisibleBars={4}
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
    tabContainer: {
        flexDirection: 'row',
        paddingHorizontal: scaleWidth(16),
        paddingTop: scaleHeight(12),
        paddingBottom: scaleHeight(8),
        backgroundColor: colors.white,
        borderBottomWidth: 1,
        borderBottomColor: colors.scrollGray,
    },
    tab: {
        flex: 1,
        paddingVertical: scaleHeight(10),
        alignItems: 'center',
        borderRadius: 8,
        marginHorizontal: scaleWidth(4),
        backgroundColor: colors.roseSurface,
    },
    activeTab: {
        backgroundColor: colors.primaryPink,
    },
    tabText: {
        fontSize: scaleFont(13),
        fontFamily: 'Poppins_500Medium',
        color: colors.mauve,
    },
    activeTabText: {
        color: colors.white,
        fontFamily: 'Poppins_600SemiBold',
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
