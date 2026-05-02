import React from 'react';
import { StyleSheet, View } from 'react-native';
import { Colors, Layout, Sizes, Spacing } from '../../theme';
import AvatarPlaceholder from '../atoms/AvatarPlaceholder';
import PillButton from '../atoms/PillButton';
import AppText from '../atoms/AppText';

/**
 * @typedef {Object} ProfileHeroProps
 * @property {string} orgName
 * @property {string} [imageUri]
 * @property {boolean} isEditMode
 * @property {() => void} onEditPress
 * @property {() => void} [onImagePress]
 * @property {boolean} [disableImagePress]
 */

/**
 * @param {ProfileHeroProps} props
 */
export default function ProfileHero({
  orgName,
  imageUri = '',
  isEditMode,
  onEditPress,
  onImagePress,
  disableImagePress = false,
}) {
  const displayName = typeof orgName === 'string' && orgName.trim().length > 0 ? orgName : 'ONG';

  return (
    <View style={styles.container}>
      <AvatarPlaceholder
        size={Sizes.avatar}
        editable={isEditMode}
        imageUri={imageUri}
        onPress={isEditMode ? onImagePress : undefined}
        disabled={disableImagePress}
      />

      <View style={styles.content}>
        <AppText variant="greeting">Olá, {displayName}</AppText>

        {isEditMode ? (
          <AppText variant="fieldLabel" secondary>
            Toque na imagem para alterar a foto da ONG
          </AppText>
        ) : (
          <View style={styles.buttonWrapper}>
            <PillButton label="Editar Perfil" variant="outline" onPress={onEditPress} fullWidth />
          </View>
        )}
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
