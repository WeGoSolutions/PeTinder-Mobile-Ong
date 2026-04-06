import { StyleSheet, Text, TextInput, View } from 'react-native';
import { colors } from '../../constants/theme';

export default function PetFormInput({
    label,
    value,
    onChangeText,
    placeholder,
    keyboardType = 'default',
    multiline = false,
    numberOfLines = 1,
    maxLength,
    error,
}) {
    return (
        <View style={styles.container}>
            <Text style={styles.label}>{label}</Text>
            <TextInput
                value={value}
                onChangeText={onChangeText}
                placeholder={placeholder}
                placeholderTextColor={colors.lightMauve}
                keyboardType={keyboardType}
                multiline={multiline}
                numberOfLines={numberOfLines}
                maxLength={maxLength}
                style={[styles.input, multiline && styles.textArea, error && styles.inputError]}
                textAlignVertical={multiline ? 'top' : 'center'}
            />
            {error ? <Text style={styles.errorText}>{error}</Text> : null}
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        gap: 8,
        width: '100%',
    },
    label: {
        fontSize: 14,
        fontWeight: '600',
        color: colors.textStrong,
    },
    input: {
        minHeight: 44,
        borderWidth: 1,
        borderColor: colors.roseBorder,
        borderRadius: 12,
        paddingHorizontal: 12,
        paddingVertical: 10,
        fontSize: 15,
        color: colors.textStrong,
        backgroundColor: colors.white,
    },
    textArea: {
        minHeight: 96,
    },
    inputError: {
        borderColor: '#D14343',
    },
    errorText: {
        fontSize: 12,
        color: '#D14343',
        marginTop: 2,
    },
});
