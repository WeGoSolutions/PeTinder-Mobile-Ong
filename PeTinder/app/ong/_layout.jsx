import { Tabs, useRouter } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { Image, Pressable, StyleSheet, Text, View } from 'react-native';

const DEFAULT_ONG_NAME = 'ONG';
const DEFAULT_ONG_IMAGE_URI = null;

function OngHeader({ ongName = DEFAULT_ONG_NAME, ongImageUri = DEFAULT_ONG_IMAGE_URI }) {
    const router = useRouter();

    return (
        <View style={styles.headerWrapper}>
            <View style={styles.headerContainer}>
                <View style={styles.avatarContainer}>
                    {ongImageUri ? (
                        <Image source={{ uri: ongImageUri }} style={styles.avatarImage} />
                    ) : (
                        <Ionicons name="business-outline" size={20} color="#80465D" />
                    )}
                </View>
                <Text style={styles.headerName}>Olá, {ongName}!</Text>
            </View>
            <Pressable
                style={styles.settingsButton}
                onPress={() => router.push('/ong/configuracoes')}
                accessibilityRole="button"
                accessibilityLabel="Ir para configuracoes"
            >
                <Ionicons name="settings-outline" size={30} color="#000000" />
            </Pressable>
        </View>
    );
}

function ConfiguracoesHeader() {
    const router = useRouter();

    return (
        <View style={styles.headerWrapper}>
            <Pressable
                style={styles.backButton}
                onPress={() => router.back()}
                accessibilityRole="button"
                accessibilityLabel="Voltar"
            >
                <Ionicons name="chevron-back" size={28} color="#000000" />
            </Pressable>

            <Text style={styles.headerName}>Configurações</Text>

            <View style={styles.headerRightSpacer} />
        </View>
    );
}

export default function OngTabsLayout() {
    return (
        <Tabs
            screenOptions={({ route }) => ({
                headerShown: true,
                header: () => (route.name === 'configuracoes' ? <ConfiguracoesHeader /> : <OngHeader />),
                tabBarActiveTintColor: '#000000',
                tabBarInactiveTintColor: '#000000',
                tabBarStyle: {
                    display: route.name === 'configuracoes' ? 'none' : 'flex',
                    height: 80,
                    paddingBottom: 8,
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
                        iconName = focused ? 'grid' : 'grid-outline';
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
        </Tabs>
    );
}

const styles = StyleSheet.create({
    headerWrapper: {
        backgroundColor: '#ffc0d97c',
        paddingHorizontal: 20,
        paddingTop: 20,
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
