import React from 'react';
import { StyleSheet, View } from 'react-native';
import { ColumnRatio, Spacing } from '../../theme';
import UnderlineInput from '../atoms/UnderlineInput';

/**
 * @typedef {Object} FieldConfig
 * @property {string} label
 * @property {string} value
 * @property {boolean} [masked]
 * @property {boolean} [isLink]
 * @property {(text: string) => void} [onChangeText]
 * @property {string} [error]
 */

/**
 * @typedef {Object} TwoColumnRowProps
 * @property {FieldConfig} leftField
 * @property {FieldConfig} rightField
 * @property {boolean} editable
 */

/**
 * @param {TwoColumnRowProps} props
 */
export default function TwoColumnRow({ leftField, rightField, editable }) {
  return (
    <View style={styles.container}>
      <View style={styles.largeColumn}>
        <UnderlineInput
          label={leftField.label}
          value={leftField.value}
          masked={leftField.masked}
          isLink={leftField.isLink}
          error={leftField.error}
          editable={editable && typeof leftField.onChangeText === 'function'}
          onChangeText={leftField.onChangeText}
        />
      </View>

      <View style={styles.smallColumn}>
        <UnderlineInput
          label={rightField.label}
          value={rightField.value}
          masked={rightField.masked}
          isLink={rightField.isLink}
          error={rightField.error}
          editable={editable && typeof rightField.onChangeText === 'function'}
          onChangeText={rightField.onChangeText}
        />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    gap: Spacing.columnGap,
  },
  largeColumn: {
    flex: ColumnRatio.large,
  },
  smallColumn: {
    flex: ColumnRatio.small,
  },
});
