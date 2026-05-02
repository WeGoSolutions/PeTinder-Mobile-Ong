import React from 'react';
import { StyleSheet, TouchableOpacity } from 'react-native';
import { Colors, Opacity, Radius, Sizes, Spacing } from '../../theme';
import AppText from './AppText';

const HIT_SLOP = {
  top: Spacing.sm,
  right: Spacing.sm,
  bottom: Spacing.sm,
  left: Spacing.sm,
};

const VARIANTS = {
  filled: 'filled',
  outline: 'outline',
  destructive: 'destructive',
};

/**
 * @typedef {Object} PillButtonProps
 * @property {string} label
 * @property {'filled'|'outline'|'destructive'} [variant]
 * @property {() => void} onPress
 * @property {boolean} [fullWidth]
 * @property {boolean} [disabled]
 */

/**
 * @param {PillButtonProps} props
 */
export default function PillButton({
  label,
  variant = 'filled',
  onPress,
  fullWidth = false,
  disabled = false,
}) {
  const resolvedVariant = VARIANTS[variant] ?? VARIANTS.filled;

  return (
    <TouchableOpacity
      onPress={onPress}
      disabled={disabled}
      activeOpacity={Opacity.active}
      hitSlop={HIT_SLOP}
      style={[
        styles.base,
        styles[resolvedVariant],
        fullWidth ? styles.fullWidth : null,
        disabled ? styles.disabled : null,
      ]}
      accessibilityRole="button"
    >
      <AppText
        variant="buttonLabel"
        color={resolvedVariant === VARIANTS.outline ? Colors.primary : Colors.textOnPrimary}
      >
        {label}
      </AppText>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  base: {
    minHeight: Sizes.buttonMinHeight,
    paddingHorizontal: Spacing.xl,
    alignItems: 'center',
    justifyContent: 'center',
  },
  filled: {
    backgroundColor: Colors.primary,
    borderRadius: Radius.pill,
  },
  outline: {
    backgroundColor: Colors.transparent,
    borderWidth: Sizes.outlineBorder,
    borderColor: Colors.primaryOutline,
    borderRadius: Radius.pillSm,
  },
  destructive: {
    backgroundColor: Colors.destructive,
    borderRadius: Radius.pill,
  },
  fullWidth: {
    width: '100%',
  },
  disabled: {
    opacity: Opacity.disabled,
  },
});
