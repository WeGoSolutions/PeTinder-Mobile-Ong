import React from 'react';
import { StyleSheet, View } from 'react-native';
import { Colors, Layout, Sizes, Spacing } from '../../theme';
import AvatarPlaceholder from '../atoms/AvatarPlaceholder';
import PillButton from '../atoms/PillButton';
import AppText from '../atoms/AppText';

/**
 * @typedef {Object} ProfileHeroProps
 * @property {string} orgName
 * @property {boolean} isEditMode
 * @property {() => void} onEditPress
 */

/**
 * @param {ProfileHeroProps} props
 */
export default function ProfileHero({ orgName, isEditMode, onEditPress }) {
  const displayName = typeof orgName === 'string' && orgName.trim().length > 0 ? orgName : 'ONG';

  return (
    <View style={styles.container}>
      <AvatarPlaceholder size={Sizes.avatar} editable={isEditMode} />

      <View style={styles.content}>
        <AppText variant="greeting">Olá, {displayName}</AppText>

        {!isEditMode ? (
          <View style={styles.buttonWrapper}>
            <PillButton label="Editar Perfil" variant="outline" onPress={onEditPress} fullWidth />
          </View>
        ) : null}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: Colors.primaryMid,
    minHeight: Layout.heroMinHeight,
    paddingHorizontal: Spacing.screenHorizontal,
    paddingVertical: Spacing.lg,
    flexDirection: 'row',
    alignItems: 'center',
    gap: Spacing.lg,
  },
  content: {
    flex: 1,
    gap: Spacing.lg,
  },
  buttonWrapper: {
    width: '100%',
    maxWidth: Sizes.avatar * 2,
  },
});
