import React, { useCallback } from 'react';
import {
  Alert,
  Linking,
  StyleSheet,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { Colors, Opacity, Sizes, Spacing, Typography } from '../../theme';
import AppText from './AppText';

const HIT_SLOP = {
  top: Spacing.sm,
  right: Spacing.sm,
  bottom: Spacing.sm,
  left: Spacing.sm,
};

/**
 * @typedef {Object} UnderlineInputProps
 * @property {string} label
 * @property {string} value
 * @property {boolean} [editable]
 * @property {(text: string) => void} [onChangeText]
 * @property {boolean} [masked]
 * @property {boolean} [isLink]
 * @property {string} [error]
 */

/**
 * @param {UnderlineInputProps} props
 */
export default function UnderlineInput({
  label,
  value,
  editable = false,
  onChangeText,
  masked = false,
  isLink = false,
  error,
}) {
  const displayValue = typeof value === 'string' ? value : '';

  const handleOpenLink = useCallback(async () => {
    if (!displayValue) {
      return;
    }

    try {
      await Linking.openURL(displayValue);
    } catch (openError) {
      Alert.alert('Link invalido', 'Nao foi possivel abrir o link.');
    }
  }, [displayValue]);

  const canEdit = editable && typeof onChangeText === 'function';
  const showLink = isLink && !canEdit;

  return (
    <View style={styles.container}>
      <AppText variant="fieldLabel" secondary>
        {label}
      </AppText>

      {canEdit ? (
        <TextInput
          style={styles.input}
          value={displayValue}
          onChangeText={onChangeText}
          placeholderTextColor={Colors.textSecondary}
        />
      ) : showLink ? (
        <TouchableOpacity
          onPress={handleOpenLink}
          activeOpacity={Opacity.active}
          style={styles.linkTouch}
          accessibilityRole="link"
          hitSlop={HIT_SLOP}
        >
          <AppText variant="fieldValue" color={Colors.link} style={styles.linkText}>
            {displayValue}
          </AppText>
        </TouchableOpacity>
      ) : (
        <AppText variant="fieldValue" style={styles.valueText}>
          {masked ? displayValue : displayValue}
        </AppText>
      )}

      <View style={styles.bottomBorder} />

      {error ? (
        <AppText variant="fieldLabel" color={Colors.error} style={styles.errorText}>
          {error}
        </AppText>
      ) : null}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginBottom: Spacing.fieldGap,
  },
  input: {
    minHeight: Sizes.touchMinSize,
    paddingVertical: Spacing.sm,
    fontFamily: Typography.fontFamily,
    fontSize: Typography.sizes.fieldValue,
    fontWeight: Typography.weights.regular,
    color: Colors.textPrimary,
  },
  valueText: {
    minHeight: Sizes.touchMinSize,
    textAlignVertical: 'center',
    paddingVertical: Spacing.sm,
  },
  linkTouch: {
    minHeight: Sizes.touchMinSize,
    justifyContent: 'center',
    paddingVertical: Spacing.sm,
  },
  linkText: {
    textDecorationLine: 'underline',
  },
  bottomBorder: {
    borderBottomWidth: Sizes.fieldBorder,
    borderBottomColor: Colors.borderField,
  },
  errorText: {
    marginTop: Spacing.xs,
  },
});
