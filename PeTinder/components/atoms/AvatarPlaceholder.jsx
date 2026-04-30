import React from 'react';
import { StyleSheet, View } from 'react-native';
import { Feather } from '@expo/vector-icons';
import { Colors, Radius, Sizes, Spacing } from '../../theme';

/**
 * @typedef {Object} AvatarPlaceholderProps
 * @property {number} [size]
 * @property {boolean} [editable]
 */

/**
 * @param {AvatarPlaceholderProps} props
 */
export default function AvatarPlaceholder({ size = Sizes.avatar, editable = false }) {
  return (
    <View style={[styles.container, { width: size, height: size, borderRadius: Radius.avatar }]}>
      <Feather name="user" size={Sizes.avatarIcon} color={Colors.textPrimary} />

      {editable ? (
        <View style={styles.cameraBadge}>
          <Feather name="camera" size={Sizes.cameraIcon} color={Colors.textPrimary} />
        </View>
      ) : null}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: Colors.avatarBg,
    alignItems: 'center',
    justifyContent: 'center',
    overflow: 'hidden',
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
});
