import React, { useCallback } from 'react';
import { Alert, Platform, StyleSheet, View } from 'react-native';
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
  const confirmLogout = useCallback(() => {
    if (Platform.OS === 'web' && typeof window !== 'undefined' && window.confirm) {
      const shouldLogout = window.confirm('Deseja realmente encerrar sua sessao?');
      if (shouldLogout) {
        onLogout();
      }
      return;
    }

    Alert.alert('Sair da conta', 'Deseja realmente encerrar sua sessao?', [
      {
        text: 'Cancelar',
        style: 'cancel',
      },
      {
        text: 'Sair',
        style: 'destructive',
        onPress: onLogout,
      },
    ]);
  }, [onLogout]);

  return (
    <View style={styles.container}>
      {mode === 'view' ? (
        <PillButton
          label="Sair da conta"
          variant="destructive"
          fullWidth
          onPress={confirmLogout}
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
