import { useLocalSearchParams } from 'expo-router';
import { StyleSheet, Text, View } from 'react-native';

export default function Chat() {
    const { userId, petId } = useLocalSearchParams();

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Chat</Text>
            <Text style={styles.subtitle}>Conversa com interessado da ONG</Text>
            <Text style={styles.meta}>userId: {String(userId ?? '-')}</Text>
            <Text style={styles.meta}>petId: {String(petId ?? '-')}</Text>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#FFFFFF',
        padding: 24,
    },
    title: {
        fontSize: 24,
        fontWeight: '700',
        color: '#1A1A1A',
        marginBottom: 8,
    },
    subtitle: {
        fontSize: 14,
        color: '#6E6E6E',
        marginBottom: 10,
    },
    meta: {
        fontSize: 13,
        color: '#80465D',
        marginTop: 2,
    },
});
