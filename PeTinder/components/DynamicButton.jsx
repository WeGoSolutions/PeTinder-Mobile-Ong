import React from 'react';
import { TouchableOpacity, Text, StyleSheet, ActivityIndicator, View } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';

const DynamicButton = ({
    variant = 'primary',
    onPress,
    disabled = false,
    isLoading = false,
    children,
    style,
    textStyle,
}) => {
    const buttonStyles = [
        styles.button,
        styles[`${variant}Button`],
        disabled && styles.disabledButton,
        style,
    ];

    const textStyles = [
        styles.buttonText,
        styles[`${variant}ButtonText`],
        textStyle,
    ];

    const loaderColor = '#1A1A1A';

    const renderButtonContent = () => (
        <>
            {isLoading ? (
                <ActivityIndicator size="small" color={loaderColor} />
            ) : (
                <Text style={textStyles}>{children}</Text>
            )}
        </>
    );

    // Secondary button vazado com borda em gradiente
    if (variant === 'secondary') {
        return (
            <TouchableOpacity
                style={[styles.secondaryTouchable, disabled && styles.disabledButton, style]}
                onPress={onPress}
                disabled={disabled || isLoading}
                activeOpacity={0.7}
            >
                <LinearGradient
                    colors={['#80465D', '#B86184']}
                    start={{ x: 0, y: 0 }}
                    end={{ x: 1, y: 0 }}
                    style={styles.secondaryButton}
                >
                    <View style={styles.secondaryInner}>
                        {renderButtonContent()}
                    </View>
                </LinearGradient>
            </TouchableOpacity>
        );
    }

    return (
        <TouchableOpacity
            style={buttonStyles}
            onPress={onPress}
            disabled={disabled || isLoading}
            activeOpacity={0.7}
        >
            {renderButtonContent()}
        </TouchableOpacity>
    );
};

const styles = StyleSheet.create({
    button: {
        paddingVertical: 16,
        borderRadius: 12,
        alignItems: 'center',
        justifyContent: 'center',
    },
    primaryButton: {
        backgroundColor: '#E8A0BF',
        marginBottom: 16,
    },
    secondaryButton: {
        padding: 2,
        borderRadius: 12,
        marginBottom: 16,
    },
    secondaryTouchable: {
        borderRadius: 12,
    },
    secondaryInner: {
        borderRadius: 10,
        backgroundColor: '#FFFFFF',
        paddingVertical: 14,
        paddingHorizontal: 24,
        alignItems: 'center',
        justifyContent: 'center',
    },
    forgotPasswordButton: {
        backgroundColor: 'transparent',
        marginTop: 8,
        paddingVertical: 12,
    },
    needHelpButton: {
        backgroundColor: 'transparent',
        marginTop: 8,
        paddingVertical: 12,
    },
    disabledButton: {
        opacity: 0.6,
    },
    buttonText: {
        fontSize: 16,
        fontFamily: 'System',
        fontWeight: '600',
    },
    primaryButtonText: {
        color: '#1A1A1A',
    },
    secondaryButtonText: {
        color: '#1A1A1A',
    },
    forgotPasswordButtonText: {
        fontSize: 18,
        color: '#1A1A1A',
    },
    needHelpButtonText: {
        fontSize: 18,
        color: '#1A1A1A',
    },
});

export default DynamicButton;
