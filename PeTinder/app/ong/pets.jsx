import { View, Text, StyleSheet } from 'react-native';

export default function Pets() {
    return (
        <View style={styles.container}>
            <Text style={styles.title}>Pets</Text>
            <Text style={styles.subtitle}>Cadastro e gerenciamento de pets</Text>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#ffffff',
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
    },
});
