import React from 'react';
import { StyleSheet, Text } from 'react-native';
import { Colors, Typography } from '../../theme';

const VARIANT_MAP = {
  navTitle: 'navTitle',
  greeting: 'greeting',
  tabLabel: 'tabLabel',
  subTabLabel: 'subTabLabel',
  fieldLabel: 'fieldLabel',
  fieldValue: 'fieldValue',
  buttonLabel: 'buttonLabel',
};

/**
 * @typedef {Object} AppTextProps
 * @property {'navTitle'|'greeting'|'tabLabel'|'subTabLabel'|'fieldLabel'|'fieldValue'|'buttonLabel'} [variant]
 * @property {string} [color]
 * @property {boolean} [secondary]
 * @property {import('react-native').StyleProp<import('react-native').TextStyle>} [style]
 * @property {React.ReactNode} children
 */

/**
 * @param {AppTextProps & import('react-native').TextProps} props
 */
export default function AppText({
  variant = 'fieldValue',
  color,
  secondary = false,
  style,
  children,
  ...rest
}) {
  const resolvedVariant = VARIANT_MAP[variant] ?? VARIANT_MAP.fieldValue;
  const resolvedColor = color || (secondary ? Colors.textSecondary : Colors.textPrimary);

  return (
    <Text
      {...rest}
      style={[
        styles.base,
        styles[resolvedVariant],
        { color: resolvedColor },
        style,
      ]}
    >
      {children}
    </Text>
  );
}

const styles = StyleSheet.create({
  base: {
    fontFamily: Typography.fontFamily,
  },
  navTitle: {
    fontSize: Typography.sizes.navTitle,
    fontWeight: Typography.weights.bold,
  },
  greeting: {
    fontSize: Typography.sizes.greeting,
    fontWeight: Typography.weights.bold,
  },
  tabLabel: {
    fontSize: Typography.sizes.tabLabel,
    fontWeight: Typography.weights.regular,
  },
  subTabLabel: {
    fontSize: Typography.sizes.subTabLabel,
    fontWeight: Typography.weights.regular,
  },
  fieldLabel: {
    fontSize: Typography.sizes.fieldLabel,
    fontWeight: Typography.weights.regular,
  },
  fieldValue: {
    fontSize: Typography.sizes.fieldValue,
    fontWeight: Typography.weights.regular,
  },
  buttonLabel: {
    fontSize: Typography.sizes.buttonLabel,
    fontWeight: Typography.weights.bold,
  },
});
