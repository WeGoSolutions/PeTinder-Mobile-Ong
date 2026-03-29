import { Tabs } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';

export default function OngTabsLayout() {
    return (
        <Tabs
            screenOptions={({ route }) => ({
                headerShown: false,
                tabBarActiveTintColor: '#000000',
                tabBarInactiveTintColor: '#000000',
                tabBarStyle: {
                    height: 80,
                    paddingBottom: 8,
                    paddingTop: 8,
                    borderTopWidth: 1,
                    borderTopColor: '#ffc0d97c',
                    backgroundColor: '#ffc0d97c',
                },
                tabBarIcon: ({ color, size, focused }) => {
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

                    return <Ionicons name={iconName} size={28} color={color} />;
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
        </Tabs>
    );
}
