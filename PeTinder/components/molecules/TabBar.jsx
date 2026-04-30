import React, { useCallback, useEffect, useRef } from 'react';
import { Animated, ScrollView, StyleSheet, TouchableOpacity, View } from 'react-native';
import { Colors, Layout, Motion, Opacity, Sizes, Spacing, Typography } from '../../theme';
import AppText from '../atoms/AppText';

const HIT_SLOP = {
  top: Spacing.sm,
  right: Spacing.sm,
  bottom: Spacing.sm,
  left: Spacing.sm,
};

/**
 * @typedef {Object} TabBarProps
 * @property {string[]} tabs
 * @property {number} activeIndex
 * @property {(index: number) => void} onTabPress
 */

/**
 * @param {TabBarProps} props
 */
export default function TabBar({ tabs, activeIndex, onTabPress }) {
  const tabLayouts = useRef([]);
  const underlineX = useRef(new Animated.Value(0)).current;
  const underlineWidth = useRef(new Animated.Value(0)).current;

  const animateUnderline = useCallback(() => {
    const target = tabLayouts.current[activeIndex];

    if (!target) {
      return;
    }

    Animated.parallel([
      Animated.timing(underlineX, {
        toValue: target.x,
        duration: Motion.fast,
        useNativeDriver: false,
      }),
      Animated.timing(underlineWidth, {
        toValue: target.width,
        duration: Motion.fast,
        useNativeDriver: false,
      }),
    ]).start();
  }, [activeIndex, underlineWidth, underlineX]);

  useEffect(() => {
    animateUnderline();
  }, [animateUnderline, tabs]);

  const handleTabLayout = useCallback(
    (index, event) => {
      const { x, width } = event.nativeEvent.layout;
      tabLayouts.current[index] = { x, width };

      if (index === activeIndex) {
        underlineX.setValue(x);
        underlineWidth.setValue(width);
      }
    },
    [activeIndex, underlineWidth, underlineX]
  );

  return (
    <View style={styles.wrapper}>
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={false}
        contentContainerStyle={styles.scrollContent}
      >
        {tabs.map((tabLabel, index) => {
          const isActive = activeIndex === index;

          return (
            <TouchableOpacity
              key={`${tabLabel}-${index}`}
              onPress={() => onTabPress(index)}
              onLayout={(event) => handleTabLayout(index, event)}
              style={styles.tabButton}
              activeOpacity={Opacity.active}
              hitSlop={HIT_SLOP}
              accessibilityRole="button"
            >
              <AppText
                variant="tabLabel"
                color={isActive ? Colors.textPrimary : Colors.textSecondary}
                style={isActive ? styles.activeLabel : styles.inactiveLabel}
              >
                {tabLabel}
              </AppText>
            </TouchableOpacity>
          );
        })}

        <Animated.View
          pointerEvents="none"
          style={[
            styles.underline,
            {
              width: underlineWidth,
              transform: [{ translateX: underlineX }],
            },
          ]}
        />
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  wrapper: {
    backgroundColor: Colors.tabStrip,
  },
  scrollContent: {
    paddingHorizontal: Spacing.screenHorizontal,
    position: 'relative',
  },
  tabButton: {
    minWidth: Layout.tabMinWidth,
    minHeight: Sizes.touchMinSize,
    justifyContent: 'center',
    paddingHorizontal: Spacing.sm,
    paddingVertical: Spacing.sm,
  },
  activeLabel: {
    fontWeight: Typography.weights.bold,
  },
  inactiveLabel: {
    fontWeight: Typography.weights.regular,
  },
  underline: {
    position: 'absolute',
    left: 0,
    bottom: Spacing.xxs,
    height: Sizes.tabUnderline,
    backgroundColor: Colors.textPrimary,
  },
});
