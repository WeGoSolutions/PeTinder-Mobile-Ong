import { useMemo } from 'react';
import { PanResponder, Platform, View } from 'react-native';

const EDGE_WIDTH = 24;
const ACTIVATE_DX = 14;
const TRIGGER_DX = 72;

export default function SwipeBackGesture({ onSwipeBack, children, style }) {
    const panResponder = useMemo(
        () =>
            PanResponder.create({
                onMoveShouldSetPanResponder: (_, gestureState) => {
                    if (Platform.OS === 'web' || typeof onSwipeBack !== 'function') {
                        return false;
                    }

                    const movedHorizontally = gestureState.dx > ACTIVATE_DX;
                    const isMostlyHorizontal = Math.abs(gestureState.dx) > Math.abs(gestureState.dy) * 1.2;
                    const startedFromLeftEdge = gestureState.x0 <= EDGE_WIDTH;

                    return startedFromLeftEdge && movedHorizontally && isMostlyHorizontal;
                },
                onPanResponderRelease: (_, gestureState) => {
                    if (gestureState.dx >= TRIGGER_DX && gestureState.vx > 0) {
                        onSwipeBack();
                    }
                },
            }),
        [onSwipeBack]
    );

    return (
        <View style={[{ flex: 1 }, style]} {...panResponder.panHandlers}>
            {children}
        </View>
    );
}
