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
    const variantButtonStyle = variant === 'modal-primary'
        ? styles.modalPrimaryButton
        : variant === 'tertiary'
            ? styles.tertiaryButton
            : styles[`${variant}Button`] || styles.primaryButton;

    const variantTextStyle = variant === 'modal-primary'
        ? styles.modalPrimaryButtonText
        : variant === 'modal-secondary'
            ? styles.modalSecondaryButtonText
            : variant === 'tertiary'
                ? styles.tertiaryButtonText
                : styles[`${variant}ButtonText`] || styles.primaryButtonText;

    const buttonStyles = [
        styles.button,
        variantButtonStyle,
        disabled && styles.disabledButton,
        style,
    ];

    const textStyles = [
        styles.buttonText,
        variantTextStyle,
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
    if (variant === 'secondary' || variant === 'modal-secondary') {
        return (
            <TouchableOpacity
                style={[
                    variant === 'modal-secondary' ? styles.modalSecondaryTouchable : styles.secondaryTouchable,
                    disabled && styles.disabledButton,
                    style,
                ]}
                onPress={onPress}
                disabled={disabled || isLoading}
                activeOpacity={0.7}
            >
                <LinearGradient
                    colors={['#80465D', '#B86184']}
                    start={{ x: 0, y: 0 }}
                    end={{ x: 1, y: 0 }}
                    style={variant === 'modal-secondary' ? styles.modalSecondaryButton : styles.secondaryButton}
                >
                    <View style={variant === 'modal-secondary' ? styles.modalSecondaryInner : styles.secondaryInner}>
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
        borderWidth: 4,
        borderColor: '#FE5497',
        paddingVertical: 14,
        marginBottom: 16,
    },
    secondaryButton: {
        padding: 4,
        borderRadius: 12,
        marginBottom: 16,
    },
    secondaryTouchable: {
        borderRadius: 12,
    },
    secondaryInner: {
        borderRadius: 8,
        backgroundColor: '#FFFFFF',
        paddingVertical: 14,
        paddingHorizontal: 24,
        alignItems: 'center',
        justifyContent: 'center',
    },
    tertiaryButton: {
        backgroundColor: 'transparent',
        marginTop: 8,
        paddingVertical: 0,
        paddingHorizontal: 0,
        alignSelf: 'center',
    },
    modalPrimaryButton: {
        backgroundColor: '#E8A0BF',
        borderWidth: 4,
        borderColor: '#FE5497',
        marginBottom: 0,
        paddingVertical: 10,
        paddingHorizontal: 14,
    },
    modalSecondaryButton: {
        padding: 4,
        borderRadius: 12,
        marginBottom: 0,
    },
    modalSecondaryTouchable: {
        borderRadius: 12,
    },
    modalSecondaryInner: {
        borderRadius: 8,
        backgroundColor: '#FFFFFF',
        paddingVertical: 10,
        paddingHorizontal: 14,
        alignItems: 'center',
        justifyContent: 'center',
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
    modalPrimaryButtonText: {
        color: '#1A1A1A',
        fontSize: 14,
        fontWeight: '700',
    },
    modalSecondaryButtonText: {
        color: '#1A1A1A',
        fontSize: 14,
        fontWeight: '700',
    },
    tertiaryButtonText: {
        fontSize: 18,
        color: '#1A1A1A',
        textDecorationLine: 'underline',
        textDecorationStyle: 'solid',
    },
});

export default DynamicButton;
