import { useMemo, useRef, useState } from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity, Animated } from 'react-native';
import { colors, typography, scaleWidth, scaleHeight, scaleFont, layout } from '../../constants/theme';

/**
 * Bar Chart component showing pets sorted by likes
 * 
 * @param {Array} pets - Array of pet objects with { id, name, likes }
 * @param {number} maxVisibleBars - Maximum bars visible at once (default: 6)
 */
export default function LikesBarChart({
  pets = [],
  maxVisibleBars = 6
}) {
  const [selectedPetId, setSelectedPetId] = useState(null);
  const [viewportWidth, setViewportWidth] = useState(0);
  const [contentWidth, setContentWidth] = useState(0);
  const [trackWidth, setTrackWidth] = useState(0);
  const scrollX = useRef(new Animated.Value(0)).current;

  // Sort pets by likes descending
  const sortedPets = [...pets].sort((a, b) => b.likes - a.likes);

  // Calculate max likes for proportional bar heights
  const maxLikes = Math.max(...sortedPets.map(p => p.likes), 1);

  // Bar dimensions
  const barWidth = scaleWidth(layout.barChart.barWidth);
  const maxBarHeight = scaleHeight(150);
  const barGap = scaleWidth(16);

  // Calculate container width based on visible bars
  const visibleWidth = (barWidth + barGap) * maxVisibleBars;

  const handleBarPress = (petId) => {
    setSelectedPetId(selectedPetId === petId ? null : petId);
  };

  const canScroll = contentWidth > viewportWidth && sortedPets.length > maxVisibleBars;
  const minThumbWidth = scaleWidth(28);

  const thumbWidth = useMemo(() => {
    if (!canScroll || trackWidth <= 0 || contentWidth <= 0) {
      return 0;
    }

    return Math.max(minThumbWidth, (viewportWidth / contentWidth) * trackWidth);
  }, [canScroll, trackWidth, contentWidth, viewportWidth, minThumbWidth]);

  const maxScroll = Math.max(contentWidth - viewportWidth, 1);
  const maxThumbTravel = Math.max(trackWidth - thumbWidth, 0);

  const translateX = useMemo(
    () =>
      scrollX.interpolate({
        inputRange: [0, maxScroll],
        outputRange: [0, maxThumbTravel],
        extrapolate: 'clamp',
      }),
    [scrollX, maxScroll, maxThumbTravel]
  );

  const renderBar = (pet) => {
    const isSelected = selectedPetId === pet.id;
    const barHeight = (pet.likes / maxLikes) * maxBarHeight;

    return (
      <TouchableOpacity
        key={pet.id}
        style={styles.barContainer}
        onPress={() => handleBarPress(pet.id)}
        activeOpacity={0.8}
      >
        {/* Likes value (shown only when selected) */}
        <View style={styles.likesContainer}>
          {isSelected && (
            <Text style={styles.likesText}>{pet.likes}</Text>
          )}
        </View>

        {/* Bar */}
        <View
          style={[
            styles.bar,
            {
              height: Math.max(barHeight, scaleHeight(20)),
              width: barWidth,
              backgroundColor: isSelected ? colors.primaryPink : colors.lightPink,
            }
          ]}
        />

        {/* Pet name */}
        <Text
          style={[
            styles.petName,
            isSelected && styles.petNameSelected
          ]}
          numberOfLines={1}
        >
          {pet.name}
        </Text>
      </TouchableOpacity>
    );
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Pets mais curtidos</Text>

      <View style={styles.chartContainer}>
        <ScrollView
          horizontal
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={[
            styles.barsContainer,
            { minWidth: visibleWidth }
          ]}
          style={styles.scrollView}
          onLayout={(event) => setViewportWidth(event.nativeEvent.layout.width)}
          onContentSizeChange={(width) => setContentWidth(width)}
          onScroll={Animated.event(
            [{ nativeEvent: { contentOffset: { x: scrollX } } }],
            { useNativeDriver: false }
          )}
          scrollEventThrottle={16}
        >
          {sortedPets.map((pet) => renderBar(pet))}
        </ScrollView>

        {/* Scroll indicator */}
        {canScroll && (
          <View
            style={styles.scrollIndicatorContainer}
            onLayout={(event) => setTrackWidth(event.nativeEvent.layout.width)}
          >
            <View style={styles.scrollIndicatorTrack} />
            <Animated.View
              style={[
                styles.scrollIndicatorThumb,
                {
                  width: thumbWidth,
                  transform: [{ translateX }],
                },
              ]}
            />
          </View>
        )}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: scaleWidth(20),
    paddingVertical: scaleHeight(16),
  },
  title: {
    fontSize: scaleFont(typography.fontSize.title),
    fontFamily: typography.fontFamily.poppins.medium,
    fontWeight: '500',
    color: colors.black,
    marginBottom: scaleHeight(24),
  },
  chartContainer: {
    alignItems: 'center',
  },
  scrollView: {
    maxWidth: '100%',
  },
  barsContainer: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    paddingHorizontal: scaleWidth(8),
    paddingBottom: scaleHeight(8),
  },
  barContainer: {
    alignItems: 'center',
    marginHorizontal: scaleWidth(8),
  },
  likesContainer: {
    height: scaleHeight(24),
    justifyContent: 'flex-end',
    marginBottom: scaleHeight(4),
  },
  likesText: {
    fontSize: scaleFont(typography.fontSize.likesValue),
    fontFamily: typography.fontFamily.poppins.medium,
    fontWeight: '500',
    color: colors.black,
  },
  bar: {
    borderRadius: scaleWidth(8),
  },
  petName: {
    fontSize: scaleFont(typography.fontSize.petName),
    fontFamily: typography.fontFamily.poppins.regular,
    fontWeight: '400',
    color: colors.black,
    marginTop: scaleHeight(8),
    textAlign: 'center',
    width: scaleWidth(layout.barChart.barWidth + 10),
  },
  petNameSelected: {
    fontFamily: typography.fontFamily.poppins.bold,
    fontWeight: '700',
  },
  scrollIndicatorContainer: {
    width: '80%',
    marginTop: scaleHeight(16),
    height: scaleHeight(6),
    justifyContent: 'center',
    borderRadius: scaleHeight(3),
    overflow: 'hidden',
  },
  scrollIndicatorTrack: {
    ...StyleSheet.absoluteFillObject,
    width: '100%',
    height: scaleHeight(6),
    backgroundColor: colors.scrollGray,
    borderRadius: scaleHeight(3),
  },
  scrollIndicatorThumb: {
    height: scaleHeight(6),
    borderRadius: scaleHeight(3),
    backgroundColor: colors.mauve,
  },
});
