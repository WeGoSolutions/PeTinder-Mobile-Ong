import { View, Text, StyleSheet, Image, TouchableOpacity } from 'react-native';
import { colors, typography, scaleHeight, scaleWidth, scaleFont, layout } from '../constants/theme';

export default function Header({ 
  title = 'Olá {ONG}', 
  showSettingsIcon = true, 
  onSettingsPress 
}) {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>{title}</Text>
      
      {showSettingsIcon && (
        <TouchableOpacity 
          style={styles.settingsButton} 
          onPress={onSettingsPress}
          activeOpacity={0.7}
        >
          <Image 
            source={require('../assets/icon-config.svg')} 
            style={styles.settingsIcon}
          />
        </TouchableOpacity>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: colors.lightPink,
    height: scaleHeight(layout.header.height),
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: scaleWidth(20),
    paddingTop: scaleHeight(40),
  },
  title: {
    fontSize: scaleFont(typography.fontSize.title),
    lineHeight: scaleFont(typography.fontSize.title + 4),
    fontFamily: typography.fontFamily.poppins.medium,
    fontWeight: '500',
    color: colors.black,
    includeFontPadding: false,
  },
  settingsButton: {
    padding: scaleWidth(8),
  },
  settingsIcon: {
    width: scaleWidth(30),
    height: scaleWidth(30),
  },
});
