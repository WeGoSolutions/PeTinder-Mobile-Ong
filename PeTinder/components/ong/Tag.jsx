import { Pressable, StyleSheet, Text } from 'react-native';
import { colors } from '../../constants/theme';

const tagColors = {
    Ativo: { border: '#C04646', color: '#C04646', background: '#EDA2A2' },
    Calmo: { border: '#6578B7', color: '#6578B7', background: '#AFD2FF' },
    Brincalhao: { border: '#C18E00', color: '#C18E00', background: '#FFDA9F' },
    Carinhoso: { border: '#E24476', color: '#E24476', background: '#FFAFAF' },
    Sociavel: { border: '#92B765', color: '#92B765', background: '#DEFFAF' },
    Protetor: { border: '#783E00', color: '#783E00', background: '#FFC280' },
    Independente: { border: '#00A8BB', color: '#00A8BB', background: '#CBFAFF' },
    Curioso: { border: '#D56E00', color: '#D56E00', background: '#FFC485' },
    Medroso: { border: '#5B4680', color: '#5B4680', background: '#E2C0FF' },
    Territorial: { border: '#92375D', color: '#92375D', background: '#FFA2C9' },
    Obediente: { border: '#0577D4', color: '#0577D4', background: '#9FD4FF' },
    Teimoso: { border: '#016401', color: '#016401', background: '#C5FFC5' },
    'Brincalhão': { border: '#C18E00', color: '#C18E00', background: '#FFDA9F' },
    'Sociável': { border: '#92B765', color: '#92B765', background: '#DEFFAF' },
};

export default function Tag({ tagName, isDisabled, onPress }) {
    const selectedStyle = tagColors[tagName] || {};

    const style = isDisabled
        ? {
            borderColor: '#979797',
            color: '#979797',
            backgroundColor: colors.white,
        }
        : {
            borderColor: selectedStyle.border,
            color: selectedStyle.color,
            backgroundColor: selectedStyle.background,
        };

    return (
        <Pressable
            style={[
                styles.containerTag,
                {
                    borderColor: style.borderColor,
                    backgroundColor: style.backgroundColor,
                },
            ]}
            onPress={onPress}
        >
            <Text style={[styles.tagText, { color: style.color }]}>{tagName}</Text>
        </Pressable>
    );
}

const styles = StyleSheet.create({
    containerTag: {
        width: '32%',
        minHeight: 35,
        borderWidth: 2,
        borderRadius: 999,
        paddingHorizontal: 6,
        paddingVertical: 8,
        alignItems: 'center',
        justifyContent: 'center',
    },
    tagText: {
        fontSize: 13,
        fontWeight: '600',
        textAlign: 'center',
    },
});