import { Dimensions, PixelRatio } from 'react-native';

const BASE_WIDTH = 402;
const BASE_HEIGHT = 874;

const getWindowDimensions = () => Dimensions.get('window');

export const scaleWidth = (size) => (getWindowDimensions().width / BASE_WIDTH) * size;
export const scaleHeight = (size) => (getWindowDimensions().height / BASE_HEIGHT) * size;
export const scaleFont = (size) => {
  const scale = getWindowDimensions().width / BASE_WIDTH;
  const newSize = size * scale;
  return Math.round(PixelRatio.roundToNearestPixel(newSize));
};

export const moderateScale = (size, factor = 0.5) => {
  return size + (scaleWidth(size) - size) * factor;
};

// Colors
export const colors = {
  // Primary colors
  primaryPink: '#E24476',
  lightPink: '#FFC0D9',
  
  // Neutrals
  black: '#1E1E1E',
  scrollGray: '#D9D9D9',
  white: '#FFFFFF',

  // Legacy colors from existing app (for consistency)
  mauve: '#80465D',
  lightMauve: '#B86184',
  buttonPink: '#E8A0BF',
};

// Typography
export const typography = {
  // Font families
  fontFamily: {
    poppins: {
      regular: 'Poppins_400Regular',
      medium: 'Poppins_500Medium',
      semiBold: 'Poppins_600SemiBold',
      bold: 'Poppins_700Bold',
    },
    inter: {
      regular: 'Inter_400Regular',
    },
  },
  
  // Font sizes (base values - use scaleFont for responsiveness)
  fontSize: {
    title: 20,
    donutNumber: 22,
    donutLegend: 12,
    likesValue: 14,
    petName: 14,
  },
};

// Spacing
export const spacing = {
  xs: 4,
  sm: 8,
  md: 12,
  lg: 16,
  xl: 20,
  xxl: 24,
  xxxl: 32,
};

// Icon sizes
export const iconSizes = {
  default: 35,
  settings: 30,
  tabIcon: 35,
};

// Layout dimensions (base values)
export const layout = {
  header: {
    height: 133,
  },
  footer: {
    height: 91,
  },
  donutChart: {
    width: 195.19,
    height: 203.27,
  },
  barChart: {
    barWidth: 45,
    maxVisibleBars: 6,
  },
};

// Responsive layout functions
export const getResponsiveLayout = () => ({
  header: {
    height: scaleHeight(layout.header.height),
  },
  footer: {
    height: scaleHeight(layout.footer.height),
  },
  donutChart: {
    width: scaleWidth(layout.donutChart.width),
    height: scaleHeight(layout.donutChart.height),
  },
  barChart: {
    barWidth: scaleWidth(layout.barChart.barWidth),
  },
});

// Common styles
export const commonStyles = {
  shadow: {
    shadowColor: colors.black,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
};

export default {
  colors,
  typography,
  spacing,
  iconSizes,
  layout,
  scaleWidth,
  scaleHeight,
  scaleFont,
  moderateScale,
  getResponsiveLayout,
  commonStyles,
};
