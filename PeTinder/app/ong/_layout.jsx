import { Tabs, usePathname, useRouter, useFocusEffect } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { Image, Pressable, StyleSheet, Text, View } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useCallback, useEffect, useState } from 'react';
import { useOngInfo } from '../../services/petService';
import { getSession } from '../../services/sessionService';

const DEFAULT_ONG_NAME = 'ONG';
const DEFAULT_ONG_IMAGE_URI = null;

function OngHeader({ ongName = DEFAULT_ONG_NAME, ongImageUri = DEFAULT_ONG_IMAGE_URI }) {
    const router = useRouter();
    const pathname = usePathname();
    const insets = useSafeAreaInsets();
    const [sessionName, setSessionName] = useState(null);

    const loadSessionName = useCallback(async () => {
        try {
            const { name } = await getSession();
            console.log('Nome recuperado da sessão:', name);
            if (name) {
                setSessionName(name);
            }
        } catch (error) {
            console.error('Erro ao carregar sessão:', error);
        }
    }, []);

    useFocusEffect(
        useCallback(() => {
            loadSessionName();
        }, [loadSessionName])
    );

    const displayName = sessionName || ongName;

    return (
        <View style={[styles.headerWrapper, { paddingTop: Math.max(insets.top, 8) + 8 }]}>
            <View style={styles.headerContainer}>
                <View style={styles.avatarContainer}>
                    {ongImageUri ? (
                        <Image source={{ uri: ongImageUri }} style={styles.avatarImage} />
                    ) : (
                        <Ionicons name="business-outline" size={20} color="#80465D" />
                    )}
                </View>
                <Text style={styles.headerName}>Olá, {displayName}!</Text>
            </View>
            <Pressable
                style={styles.settingsButton}
                onPress={() =>
                    router.push({
                        pathname: '/ong/configuracoes',
                        params: {
                            from: pathname,
                        },
                    })
                }
                accessibilityRole="button"
                accessibilityLabel="Ir para configuracoes"
            >
                <Ionicons name="settings-outline" size={30} color="#000000" />
            </Pressable>
        </View>
    );
}

function SecondaryHeader({ title, backTo }) {
    const router = useRouter();
    const insets = useSafeAreaInsets();

    const handleBack = () => {
        if (typeof backTo === 'string' && backTo.trim().length > 0) {
            router.replace(backTo);
            return;
        }

        router.back();
    };

    return (
        <View style={[styles.headerWrapper, { paddingTop: Math.max(insets.top, 8) + 8 }]}>
            <Pressable
                style={styles.backButton}
                onPress={handleBack}
                accessibilityRole="button"
                accessibilityLabel="Voltar"
            >
                <Ionicons name="chevron-back" size={28} color="#000000" />
            </Pressable>

            <Text style={styles.headerName}>{title}</Text>

            <View style={styles.headerRightSpacer} />
        </View>
    );
}

export default function OngTabsLayout() {
    const insets = useSafeAreaInsets();

    return (
        <Tabs
            screenOptions={({ route }) => ({
                headerShown: true,
                header: () => {
                    if (route.name === 'configuracoes') {
                        const settingsBackTo =
                            typeof route.params?.from === 'string' && route.params.from.trim().length > 0
                                ? route.params.from
                                : '/ong';
                        return <SecondaryHeader title="Configuracoes" backTo={settingsBackTo} />;
                    }
                    if (route.name === 'chat') {
                        const chatUserName =
                            typeof route.params?.userName === 'string' && route.params.userName.trim().length > 0
                                ? route.params.userName
                                : 'Chat';
                        return <SecondaryHeader title={chatUserName} />;
                    }
                    return <OngHeader />;
                },
                tabBarActiveTintColor: '#000000',
                tabBarInactiveTintColor: '#000000',
                tabBarStyle: {
                    display: route.name === 'configuracoes' || route.name === 'chat' ? 'none' : 'flex',
                    height: 72 + insets.bottom,
                    paddingBottom: Math.max(8, insets.bottom),
                    paddingTop: 8,
                    borderTopWidth: 1,
                    borderTopColor: '#ffc0d97c',
                    backgroundColor: '#ffc0d97c',
                },
                tabBarIcon: ({ color, focused }) => {
                    let iconName;

                    if (route.name === 'index') {
                        iconName = focused ? 'home' : 'home-outline';
                    } else if (route.name === 'interessados') {
                        iconName = focused ? 'people' : 'people-outline';
                    } else if (route.name === 'dashboards') {
                        iconName = focused ? 'stats-chart' : 'stats-chart-outline';
                    } else {
                        iconName = focused ? 'paw' : 'paw-outline';
                    }

                    return <Ionicons name={iconName} size={30} color={color} />;
                },
            })}
        >
            <Tabs.Screen
                name="index"
                options={{
                    title: '',
                }}
            />
            <Tabs.Screen
                name="interessados"
                options={{
                    title: '',
                }}
            />
            <Tabs.Screen
                name="dashboards"
                options={{
                    title: '',
                }}
            />
            <Tabs.Screen
                name="pets"
                options={{
                    title: '',
                }}
            />
            <Tabs.Screen
                name="configuracoes"
                options={{
                    href: null,
                    title: '',
                }}
            />
            <Tabs.Screen
                name="chat"
                options={{
                    href: null,
                    title: '',
                }}
            />
        </Tabs>
    );
}

const styles = StyleSheet.create({
    headerWrapper: {
        backgroundColor: '#ffc0d97c',
        paddingHorizontal: 20,
        paddingBottom: 16,
        borderBottomWidth: 1,
        borderBottomColor: '#ffc0d97c',
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
    },
    headerContainer: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        gap: 16
    },
    headerName: {
        fontSize: 22,
        color: '#1A1A1A',
        fontWeight: '600',
        marginTop: 2,
    },
    avatarContainer: {
        width: 42,
        height: 42,
        borderRadius: 21,
        backgroundColor: '#F9EEF3',
        borderWidth: 1,
        borderColor: '#EAC4D4',
        alignItems: 'center',
        justifyContent: 'center',
        overflow: 'hidden',
    },
    settingsButton: {
        width: 42,
        height: 42,
        alignItems: 'center',
        justifyContent: 'center',
    },
    backButton: {
        width: 42,
        height: 42,
        alignItems: 'center',
        justifyContent: 'center',
    },
    headerRightSpacer: {
        width: 42,
        height: 42,
    },
    avatarImage: {
        width: '100%',
        height: '100%',
    },
});
