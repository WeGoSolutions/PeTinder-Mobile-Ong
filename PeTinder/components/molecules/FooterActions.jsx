import React from 'react';
import { StyleSheet, View } from 'react-native';
import { Colors, Sizes, Spacing } from '../../theme';
import PillButton from '../atoms/PillButton';

/**
 * @typedef {Object} FooterActionsProps
 * @property {'view'|'edit'} mode
 * @property {() => void} onLogout
 * @property {() => void} onCancel
 * @property {() => void} onSave
 */

/**
 * @param {FooterActionsProps} props
 */
export default function FooterActions({ mode, onLogout, onCancel, onSave }) {
  return (
    <View style={styles.container}>
      {mode === 'view' ? (
        <PillButton
          label="Sair da conta"
          variant="destructive"
          fullWidth
          onPress={onLogout}
        />
      ) : (
        <View style={styles.editRow}>
          <View style={styles.buttonColumn}>
            <PillButton label="Cancelar" variant="outline" onPress={onCancel} fullWidth />
          </View>

          <View style={styles.buttonColumn}>
            <PillButton label="Salvar" variant="filled" onPress={onSave} fullWidth />
          </View>
        </View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    left: 0,
    right: 0,
    bottom: 0,
    height: Spacing.footerHeight,
    backgroundColor: Colors.primaryLight,
    paddingHorizontal: Spacing.screenHorizontal,
    justifyContent: 'center',
  },
  editRow: {
    flexDirection: 'row',
    gap: Sizes.footerButtonGap,
  },
  buttonColumn: {
    flex: 1,
  },
});
