import { View, Text, StyleSheet, Image } from 'react-native';

export default function LogoHome() {
    return (
        <View style={styles.container}>
            <View style={styles.row}>
                <Image source={require('../assets/Logo.png')} style={styles.logo} />
                <Text style={styles.titulo}>PeTinder</Text>
            </View>

            <Text style={styles.subtitulo}>
                Bem vindo ao PeTinder!
            </Text>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        alignItems: 'center',
        marginTop: 16,
        zIndex: 1,
    },
    row: {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 12,
    },
    logo: {
        width: 60,
        height: 64,
        marginRight: 10,
        borderRadius: 8,
    },
    titulo: {
        fontSize: 40,
        fontWeight: '700',
        color: '#000000',
    },
    subtitulo: {
        fontSize: 22,
        color: '#000000',
        textAlign: 'center',
        fontWeight: '700',
    },
});
