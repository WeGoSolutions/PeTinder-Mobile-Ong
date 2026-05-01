import { useEffect, useMemo, useState } from 'react';
import { View, Text, StyleSheet, Pressable } from 'react-native';
import { PieChart } from 'react-native-gifted-charts';
import { colors, typography, scaleWidth, scaleHeight, scaleFont } from '../../constants/theme';

/**
 * Donut Chart component showing adoption statistics
 * 
 * @param {number} adoptedCount - Number of adopted pets
 * @param {number} notAdoptedCount - Number of non-adopted pets
 */
export default function DonutChart({ 
  adoptedCount = 0, 
  notAdoptedCount = 0,
  resetSignal = 0,
}) {
  const [selectedSlice, setSelectedSlice] = useState(null);
  const total = adoptedCount + notAdoptedCount;

  const centerValue =
    selectedSlice === 'adopted'
      ? adoptedCount
      : selectedSlice === 'notAdopted'
        ? notAdoptedCount
        : total;

  const pieData = useMemo(() => {
    const adoptedValue = total === 0 ? 1 : adoptedCount;
    const notAdoptedValue = total === 0 ? 1 : notAdoptedCount;

    return [
      {
        key: 'adopted',
        value: adoptedValue,
        color: selectedSlice && selectedSlice !== 'adopted' ? '#E2447699' : colors.primaryPink,
      },
      {
        key: 'notAdopted',
        value: notAdoptedValue,
        color: selectedSlice && selectedSlice !== 'notAdopted' ? '#1E1E1E99' : colors.black,
      },
    ];
  }, [selectedSlice, adoptedCount, notAdoptedCount, total]);

  const chartSize = scaleWidth(170);
  const chartRadius = chartSize / 2;
  const innerRadius = chartRadius * 0.52;

  const handleSlicePress = (index) => {
    if (index === 0) {
      setSelectedSlice('adopted');
      return;
    }

    setSelectedSlice('notAdopted');
  };

  useEffect(() => {
    setSelectedSlice(null);
  }, [resetSignal]);

  return (
    <Pressable style={styles.container} onPress={() => setSelectedSlice(null)}>
      <Text style={styles.title}>Quantidade de pets adotados</Text>
      
      <View style={styles.chartContainer}>
        <Pressable
          style={styles.chartWrapper}
          onPress={(event) => event.stopPropagation()}
        >
          <PieChart
            data={pieData}
            donut
            radius={chartRadius}
            innerRadius={innerRadius}
            innerCircleColor={colors.white}
            extraRadius={scaleWidth(7)}
            onPress={(_, index) => handleSlicePress(index)}
            centerLabelComponent={() => (
              <View style={styles.centerLabel}>
                <Text style={styles.centerNumber}>{centerValue}</Text>
              </View>
            )}
            showText={false}
          />
        </Pressable>
        
        <View style={styles.legendContainer}>
          <Pressable
            style={styles.legendItem}
            onPress={(event) => {
              event.stopPropagation();
              setSelectedSlice('adopted');
            }}
          >
            <View style={[styles.legendDot, { backgroundColor: colors.primaryPink }]} />
            <Text style={[styles.legendText, selectedSlice === 'adopted' && styles.legendTextActive]}>
              Adotados
            </Text>
          </Pressable>
          
          <Pressable
            style={styles.legendItem}
            onPress={(event) => {
              event.stopPropagation();
              setSelectedSlice('notAdopted');
            }}
          >
            <View style={[styles.legendDot, { backgroundColor: colors.black }]} />
            <Text style={[styles.legendText, selectedSlice === 'notAdopted' && styles.legendTextActive]}>
              Não adotados
            </Text>
          </Pressable>
        </View>
      </View>
    </Pressable>
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
    marginBottom: scaleHeight(16),
  },
  chartContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'flex-start',
  },
  chartWrapper: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  centerLabel: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  centerNumber: {
    fontSize: scaleFont(typography.fontSize.donutNumber),
    fontFamily: typography.fontFamily.poppins.semiBold,
    fontWeight: '600',
    color: colors.black,
  },
  legendContainer: {
    marginLeft: scaleWidth(12),
    justifyContent: 'center',
  },
  legendItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: scaleHeight(12),
  },
  legendDot: {
    width: scaleWidth(12),
    height: scaleWidth(12),
    borderRadius: scaleWidth(6),
    marginRight: scaleWidth(8),
  },
  legendText: {
    fontSize: scaleFont(typography.fontSize.donutLegend),
    fontFamily: typography.fontFamily.inter.regular,
    fontWeight: '400',
    color: colors.black,
  },
  legendTextActive: {
    fontWeight: '700',
  },
});
