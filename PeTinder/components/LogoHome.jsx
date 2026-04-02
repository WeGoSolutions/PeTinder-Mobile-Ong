import { View, Text, StyleSheet } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { scaleFont, scaleHeight } from '../constants/theme';

export default function LogoHome() {
    return (
        <View style={styles.container}>
            <View style={styles.row}>
                <Ionicons name="paw" size={scaleFont(52)} color="#80465D" style={styles.logoIcon} />
                <Text style={styles.titulo} maxFontSizeMultiplier={1.1}>PeTinder</Text>
            </View>

            <Text style={styles.subtitulo} maxFontSizeMultiplier={1.2}>
                Bem vindo ao PeTinder!
            </Text>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        alignItems: 'center',
        width: '100%',
        marginBottom: scaleHeight(20),
    },
    row: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        marginBottom: 12,
    },
    logoIcon: {
        marginRight: 10,
    },
    titulo: {
        fontSize: scaleFont(36),
        fontWeight: '700',
        color: '#000000',
    },
    subtitulo: {
        fontSize: scaleFont(18),
        color: '#000000',
        textAlign: 'center',
        fontWeight: '700',
    },
});
