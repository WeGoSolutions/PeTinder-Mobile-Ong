import React from 'react';
import { Image, Pressable, StyleSheet, View } from 'react-native';
import { Feather } from '@expo/vector-icons';
import { Colors, Opacity, Radius, Sizes, Spacing } from '../../theme';

const HIT_SLOP = {
  top: Spacing.sm,
  right: Spacing.sm,
  bottom: Spacing.sm,
  left: Spacing.sm,
};

/**
 * @typedef {Object} AvatarPlaceholderProps
 * @property {number} [size]
 * @property {boolean} [editable]
 * @property {string} [imageUri]
 * @property {() => void} [onPress]
 * @property {boolean} [disabled]
 */

/**
 * @param {AvatarPlaceholderProps} props
 */
export default function AvatarPlaceholder({
  size = Sizes.avatar,
  editable = false,
  imageUri = '',
  onPress,
  disabled = false,
}) {
  const hasImage = typeof imageUri === 'string' && imageUri.trim().length > 0;

  const avatar = (
    <View style={[styles.container, { width: size, height: size, borderRadius: Radius.avatar }]}>
      {hasImage ? (
        <Image source={{ uri: imageUri }} style={styles.image} resizeMode="cover" />
      ) : (
        <Feather name="user" size={Sizes.avatarIcon} color={Colors.textPrimary} />
      )}

      {editable ? (
        <View style={styles.cameraBadge}>
          <Feather name="camera" size={Sizes.cameraIcon} color={Colors.textPrimary} />
        </View>
      ) : null}
    </View>
  );

  if (editable && typeof onPress === 'function') {
    return (
      <Pressable
        onPress={onPress}
        disabled={disabled}
        hitSlop={HIT_SLOP}
        style={disabled ? styles.disabled : null}
        accessibilityRole="button"
        accessibilityLabel="Alterar imagem da ONG"
      >
        {avatar}
      </Pressable>
    );
  }

  return avatar;
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: Colors.avatarBg,
    alignItems: 'center',
    justifyContent: 'center',
    overflow: 'hidden',
  },
  image: {
    width: '100%',
    height: '100%',
  },
  cameraBadge: {
    position: 'absolute',
    right: Spacing.xs,
    bottom: Spacing.xs,
    width: Sizes.cameraBadge,
    height: Sizes.cameraBadge,
    borderRadius: Radius.avatar,
    backgroundColor: Colors.surface,
    alignItems: 'center',
    justifyContent: 'center',
  },
  disabled: {
    opacity: Opacity.disabled,
  },
});
