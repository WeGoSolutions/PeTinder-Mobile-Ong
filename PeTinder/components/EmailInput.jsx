import { TextInput, StyleSheet } from 'react-native';
import { useState } from 'react';

export default function DynamicInput({
    placeholder = 'Digite algo...',
    keyboardType = 'default',
    autoCapitalize = 'sentences',
    autoCorrect = true,
    autoComplete = 'off',
    value: externalValue,
    onChangeText,
    secureTextEntry = false,
    maxLength,
    editable = true,
    placeholderTextColor = '#999',
    style,
    ...props
}) {
    const [internalValue, setInternalValue] = useState(externalValue || '');

    const value = externalValue !== undefined ? externalValue : internalValue;

    const handleChangeText = (text) => {
        if (externalValue === undefined) {
            setInternalValue(text);
        }
        onChangeText?.(text);
    };

    return (
        <TextInput
            style={[styles.input, style]}
            placeholder={placeholder}
            placeholderTextColor={placeholderTextColor}
            keyboardType={keyboardType}
            autoCapitalize={autoCapitalize}
            autoCorrect={autoCorrect}
            autoComplete={autoComplete}
            secureTextEntry={secureTextEntry}
            maxLength={maxLength}
            editable={editable}
            value={value}
            onChangeText={handleChangeText}
            {...props}
        />
    );
}

const styles = StyleSheet.create({
    input: {
        width: '100%',
        paddingVertical: 12,
        paddingHorizontal: 16,
        borderBottomWidth: 1,
        borderBottomColor: '#ddd',
        fontSize: 16,
        fontFamily: 'System',
        color: '#333',
        marginBottom: 16,
    },
});
