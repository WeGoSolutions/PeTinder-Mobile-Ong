import React, { useState, useRef, useEffect } from 'react';
import {
    View,
    TextInput,
    StyleSheet,
    Animated,
} from 'react-native';

export default function DynamicInput({
    label,
    labelColor,
    value: controlledValue,
    defaultValue = '',
    onChangeText,
    secureTextEntry,
    type = 'text',
    keyboardType,
    autoCapitalize = 'sentences',
    autoCorrect = true,
    autoComplete,
    error,
    placeholder,
    readOnly = false,
    disabled = false,
    editable,
    maxLength,
    style,
    inputStyle,
    containerStyle,
    useFloatingLabel = true,
    borderColor = "#80465D",
    focusBorderColor = "#80465D",
    errorBorderColor = '#FF6B6B',
    textColor = '#1A1A1A',
    defaultLabelColor = '#1A1A1A',
    activeLabelColor = '#1A1A1A',
    placeholderTextColor = '#9A9A9A',
    ...props
}) {
    const [isFocused, setIsFocused] = useState(false);
    const [uncontrolledValue, setUncontrolledValue] = useState(defaultValue);

    const currentValue = controlledValue !== undefined ? controlledValue : uncontrolledValue;
    const inputType = type;
    const resolvedSecureTextEntry = secureTextEntry ?? inputType === 'password';
    const resolvedKeyboardType = keyboardType ?? (inputType === 'email' ? 'email-address' : 'default');
    const isReadOnly = readOnly || disabled || editable === false;
    const displayValue = currentValue ?? '';
    const floatingLabel = label || placeholder || '';
    const animatedValue = useRef(new Animated.Value(displayValue ? 1 : 0)).current;

    useEffect(() => {
        Animated.timing(animatedValue, {
            toValue: isFocused || displayValue ? 1 : 0,
            duration: 200,
            useNativeDriver: false,
        }).start();
    }, [animatedValue, isFocused, displayValue]);

    const labelStyle = {
        position: 'absolute',
        left: 0,
        top: animatedValue.interpolate({
            inputRange: [0, 1],
            outputRange: [18, 0],
        }),
        fontSize: animatedValue.interpolate({
            inputRange: [0, 1],
            outputRange: [16, 12],
        }),
    };

    const resolvedLabelColor = labelColor || (isFocused ? activeLabelColor : defaultLabelColor);
    const dynamicInputStyle = {
        borderBottomColor: error
            ? errorBorderColor
            : isFocused
                ? focusBorderColor
                : borderColor,
        color: textColor,
    };

    const baseInputStyles = [
        styles.input,
        dynamicInputStyle,
        error && styles.inputError,
        inputStyle,
        style,
    ];

    const handleChangeText = (nextValue) => {
        if (controlledValue === undefined) {
            setUncontrolledValue(nextValue);
        }
        onChangeText?.(nextValue);
    };

    return (
        <View style={[styles.container, containerStyle]}>
            {!!floatingLabel && useFloatingLabel && (
                <Animated.Text style={[styles.label, labelStyle, { color: resolvedLabelColor }]}>
                    {floatingLabel}
                </Animated.Text>
            )}

            <TextInput
                style={baseInputStyles}
                value={currentValue}
                onChangeText={handleChangeText}
                onFocus={() => setIsFocused(true)}
                onBlur={() => setIsFocused(false)}
                secureTextEntry={resolvedSecureTextEntry}
                keyboardType={resolvedKeyboardType}
                autoCapitalize={autoCapitalize}
                autoCorrect={autoCorrect}
                autoComplete={autoComplete}
                maxLength={maxLength}
                placeholder={useFloatingLabel && floatingLabel ? '' : placeholder}
                placeholderTextColor={placeholderTextColor}
                editable={!isReadOnly}
                {...props}
            />
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        paddingTop: 18,
        marginBottom: 20,
    },
    label: {
        fontFamily: 'System',
    },
    input: {
        borderBottomWidth: 2,
        borderBottomColor: '#80465D',
        paddingVertical: 10,
        fontSize: 16,
        fontFamily: 'System',
        color: '#1A1A1A',
    },
    inputError: {
        borderBottomColor: '#FF6B6B',
    },
});
