import { Tabs, usePathname, useRouter, useFocusEffect } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { Image, Platform, Pressable, StyleSheet, Text, View } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useCallback, useEffect, useState } from 'react';
import { getSession } from '../../services/sessionService';
import { getOngImage } from '../../services/ongSettingsService';
import { colors } from '../../constants/theme';
import { resolveImageUri } from '../../utils/imageUri';

const DEFAULT_ONG_NAME = 'ONG';
const DEFAULT_ONG_IMAGE_URI = null;

const getStringParam = (value) => {
    if (typeof value === 'string') {
        return value;
    }

    if (Array.isArray(value) && typeof value[0] === 'string') {
        return value[0];
    }

    return '';
};

function OngHeader({ ongName = DEFAULT_ONG_NAME, ongImageUri = DEFAULT_ONG_IMAGE_URI }) {
    const router = useRouter();
    const pathname = usePathname();
    const insets = useSafeAreaInsets();
    const [sessionName, setSessionName] = useState(null);
    const [sessionImageUri, setSessionImageUri] = useState(ongImageUri);
    const [imageError, setImageError] = useState(false);

    const loadSessionName = useCallback(async () => {
        try {
            const { name } = await getSession();
            if (name) {
                setSessionName(name);
            }
        } catch (error) {
            console.error('Erro ao carregar sessão:', error);
        }
    }, []);

    const loadOngImage = useCallback(async () => {
        try {
            const { ongId } = await getSession();
            if (!ongId) {
                setSessionImageUri(null);
                return;
            }

            const imageUrl = await getOngImage(ongId);
            setSessionImageUri(resolveImageUri(imageUrl));
        } catch (error) {
            setSessionImageUri(null);
        }
    }, []);

    useFocusEffect(
        useCallback(() => {
            loadSessionName();
            loadOngImage();
        }, [loadSessionName, loadOngImage])
    );

    const displayName = sessionName || ongName;

    useEffect(() => {
        setImageError(false);
    }, [sessionImageUri]);

    return (
        <View style={[styles.headerWrapper, { paddingTop: Math.max(insets.top, 8) + 8 }]}>
            <View style={styles.headerContainer}>
                <View style={styles.avatarContainer}>
                    {sessionImageUri && !imageError ? (
                        <Image
                            source={{ uri: sessionImageUri }}
                            style={styles.avatarImage}
                            onError={() => setImageError(true)}
                        />
                    ) : (
                        <Ionicons name="business-outline" size={20} color={colors.mauve} />
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
                            backTo: pathname,
                        },
                    })
                }
                accessibilityRole="button"
                accessibilityLabel="Ir para configuracoes"
            >
                <Ionicons name="settings-outline" size={30} color={colors.black} />
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
                <Ionicons name="chevron-back" size={28} color={colors.black} />
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
                        const backToParam = getStringParam(route.params?.backTo).trim();
                        const fromParam = getStringParam(route.params?.from).trim();
                        const settingsBackTo = backToParam.length > 0 ? backToParam : (fromParam.length > 0 ? fromParam : '/ong');
                        return <SecondaryHeader title="Configurações" backTo={settingsBackTo} />;
                    }
                    if (route.name === 'chat') {
                        const userNameParam = getStringParam(route.params?.userName).trim();
                        const chatUserName = userNameParam.length > 0 ? userNameParam : 'Chat';
                        return <SecondaryHeader title={chatUserName} />;
                    }
                    if (route.name === 'petForm') {
                        const petFormMode = getStringParam(route.params?.mode).trim().toLowerCase();
                        const petFormTitle = petFormMode === 'edit' ? 'Edição de Pet' : 'Adição de Pet';
                        const backToParam = getStringParam(route.params?.backTo).trim();
                        const fromParam = getStringParam(route.params?.from).trim();
                        const petFormBackTo = backToParam.length > 0 ? backToParam : (fromParam.length > 0 ? fromParam : '/ong/pets');
                        return <SecondaryHeader title={petFormTitle} backTo={petFormBackTo} />;
                    }
                    return <OngHeader />;
                },
                tabBarActiveTintColor: colors.black,
                tabBarInactiveTintColor: colors.black,
                tabBarStyle: {
                    display: route.name === 'configuracoes' || route.name === 'chat' || route.name === 'petForm' ? 'none' : 'flex',
                    height: 72 + insets.bottom,
                    paddingBottom: Math.max(8, insets.bottom),
                    paddingTop: 8,
                    borderTopWidth: 0,
                    borderTopColor: colors.lightPink,
                    backgroundColor: colors.lightPink,
                    elevation: 0,
                    shadowOpacity: 0,
                    ...(Platform.OS === 'android' ? { shadowColor: 'transparent' } : null),
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
            <Tabs.Screen
                name="petForm"
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
        backgroundColor: colors.lightPink,
        paddingHorizontal: 20,
        paddingBottom: 16,
        borderBottomWidth: 0,
        borderBottomColor: colors.lightPink,
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
        color: colors.textStrong,
        fontWeight: '600',
        marginTop: 2,
    },
    avatarContainer: {
        width: 42,
        height: 42,
        borderRadius: 21,
        backgroundColor: colors.roseSurface,
        borderWidth: 1,
        borderColor: colors.roseBorderSoft,
        alignItems: 'center',
        justifyContent: 'center',
        overflow: 'hidden',
    },
    avatarImage: {
        width: '100%',
        height: '100%',
    },
    settingsButton: {
        width: 44,
        height: 44,
        alignItems: 'center',
        justifyContent: 'center',
    },
    backButton: {
        width: 44,
        height: 44,
        alignItems: 'center',
        justifyContent: 'center',
    },
    headerRightSpacer: {
        width: 44,
        height: 44,
    },
    avatarImage: {
        width: '100%',
        height: '100%',
    },
});
