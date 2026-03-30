import { View, StyleSheet, TouchableOpacity, Image } from 'react-native';
import { colors, scaleHeight, scaleWidth, layout, iconSizes } from '../constants/theme';

export default function BottomTabBar({ 
  activeTab = 'chart', 
  onTabPress 
}) {
  const tabs = [
    { id: 'home', icon: require('../assets/icon-home.svg') },
    { id: 'users', icon: require('../assets/icon-users.svg') },
    { id: 'chart', icon: require('../assets/icon-chart.svg') },
    { id: 'pet', icon: require('../assets/icon-pet.svg') },
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
              <Image 
                source={tab.icon}
                style={[
                  styles.icon,
                  isActive && styles.iconActive
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
    width: scaleWidth(iconSizes.tabIcon),
    height: scaleWidth(iconSizes.tabIcon),
    opacity: 0.6,
  },
  iconActive: {
    opacity: 1,
    tintColor: colors.black,
  },
});
