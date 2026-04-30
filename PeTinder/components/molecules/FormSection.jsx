import React from 'react';
import { ScrollView, StyleSheet, View } from 'react-native';
import { Colors, Layout, Spacing } from '../../theme';
import UnderlineInput from '../atoms/UnderlineInput';
import TwoColumnRow from './TwoColumnRow';

/**
 * @typedef {Object} FieldConfig
 * @property {string} [type]
 * @property {string} [label]
 * @property {string} [value]
 * @property {boolean} [masked]
 * @property {boolean} [isLink]
 * @property {(text: string) => void} [onChangeText]
 * @property {string} [error]
 * @property {import('./TwoColumnRow').FieldConfig} [leftField]
 * @property {import('./TwoColumnRow').FieldConfig} [rightField]
 */

/**
 * @typedef {Object} FormSectionProps
 * @property {FieldConfig[]} fields
 * @property {boolean} editable
 */

/**
 * @param {FormSectionProps} props
 */
export default function FormSection({ fields, editable }) {
  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
      showsVerticalScrollIndicator={false}
    >
      {fields.map((field, index) => {
        if (field.type === 'two-column') {
          return (
            <TwoColumnRow
              key={`row-${index}`}
              leftField={field.leftField}
              rightField={field.rightField}
              editable={editable}
            />
          );
        }

        return (
          <UnderlineInput
            key={`${field.label}-${index}`}
            label={field.label}
            value={field.value}
            masked={field.masked}
            isLink={field.isLink}
            error={field.error}
            editable={editable && typeof field.onChangeText === 'function'}
            onChangeText={field.onChangeText}
          />
        );
      })}

      <View style={styles.bottomSpacer} />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.primaryLight,
  },
  content: {
    paddingHorizontal: Spacing.screenHorizontal,
    paddingTop: Spacing.lg,
    paddingBottom: Layout.formBottomPadding,
  },
  bottomSpacer: {
    height: Spacing.md,
  },
});
