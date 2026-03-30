import { Stack } from 'expo-router';
import { 
    useFonts, 
    Poppins_400Regular,
    Poppins_500Medium,
    Poppins_600SemiBold,
    Poppins_700Bold,
} from '@expo-google-fonts/poppins';
import { Inter_400Regular } from '@expo-google-fonts/inter';
import { Text, TextInput } from 'react-native';

let globalFontApplied = false;

function applyGlobalPoppins() {
    if (globalFontApplied) {
        return;
    }

    Text.defaultProps = Text.defaultProps || {};
    Text.defaultProps.style = [
        Text.defaultProps.style,
        { fontFamily: 'Poppins_400Regular' },
    ];

    TextInput.defaultProps = TextInput.defaultProps || {};
    TextInput.defaultProps.style = [
        TextInput.defaultProps.style,
        { fontFamily: 'Poppins_400Regular' },
    ];

    globalFontApplied = true;
}

export default function RootLayout() {
    const [fontsLoaded] = useFonts({
        Poppins_400Regular,
        Poppins_500Medium,
        Poppins_600SemiBold,
        Poppins_700Bold,
        Inter_400Regular,
    });

    if (!fontsLoaded) {
        return null;
    }

    applyGlobalPoppins();

    return <Stack screenOptions={{ headerShown: false }} />;
}
