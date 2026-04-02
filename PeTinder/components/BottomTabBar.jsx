import { View, StyleSheet, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { colors, scaleHeight, scaleWidth, layout, iconSizes } from '../constants/theme';

export default function BottomTabBar({ 
  activeTab = 'chart', 
  onTabPress 
}) {
  const tabs = [
    { id: 'home', activeIcon: 'home', inactiveIcon: 'home-outline' },
    { id: 'users', activeIcon: 'people', inactiveIcon: 'people-outline' },
    { id: 'chart', activeIcon: 'stats-chart', inactiveIcon: 'stats-chart-outline' },
    { id: 'pet', activeIcon: 'paw', inactiveIcon: 'paw-outline' },
  ];

  const handleTabPress = (tabId) => {
    if (onTabPress) {
      onTabPress(tabId);
    }
    // TODO: Add navigation logic when routes are implemented
    console.log(`Tab pressed: ${tabId}`);
  };

  return (
    <View style={styles.container}>
      {tabs.map((tab) => {
        const isActive = activeTab === tab.id;
        
        return (
          <TouchableOpacity
            key={tab.id}
            style={styles.tabButton}
            onPress={() => handleTabPress(tab.id)}
            activeOpacity={0.7}
          >
            <View style={[
              styles.iconContainer,
              isActive && styles.iconContainerActive
            ]}>
              <Ionicons
                name={isActive ? tab.activeIcon : tab.inactiveIcon}
                size={scaleWidth(iconSizes.tabIcon)}
                color={colors.black}
                style={[
                  styles.icon,
                  isActive && styles.iconActive,
                ]}
              />
            </View>
          </TouchableOpacity>
        );
      })}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: colors.lightPink,
    height: scaleHeight(layout.footer.height),
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-around',
    paddingHorizontal: scaleWidth(20),
  },
  tabButton: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    height: '100%',
  },
  iconContainer: {
    padding: 8,
    borderRadius: 12,
  },
  iconContainerActive: {
    // Active state visual indicator
  },
  icon: {
    opacity: 0.6,
  },
  iconActive: {
    opacity: 1,
  },
});
