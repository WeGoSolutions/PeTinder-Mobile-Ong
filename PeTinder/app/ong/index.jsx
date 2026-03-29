import {
    View,
    Text,
    StyleSheet,
    KeyboardAvoidingView,
    Platform,
    ScrollView
} from 'react-native';

export default function Home() {
    return (
        <View style={styles.container}>
            <Text style={styles.title}>Home Inicial</Text>
            <Text style={styles.subtitle}>Página inicial da Ong</Text>
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
