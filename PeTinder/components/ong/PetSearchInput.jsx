import { Image, Pressable, StyleSheet, TextInput, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { colors } from '../../constants/theme';
import { usePathname, useRouter } from 'expo-router';

export default function PetSearchInput({
    value,
    onChangeText,
    placeholder = 'Pesquisar por nome',
}) {
    const router = useRouter();
    const pathname = usePathname();


    const handleAddPress = () => {
        router.push({
            pathname: '/ong/petForm',
            params: {
                mode: 'add',
                from: pathname,
            },
        });
    };

    return (
        <View style={styles.container}>
            <View style={styles.inputWrapper}>
                <View style={styles.inputContainer}>
                    <Ionicons
                        name="search"
                        size={18}
                        color={colors.black}
                        style={styles.searchIcon}
                    />
                    <TextInput
                        value={value}
                        onChangeText={onChangeText}
                        placeholder={placeholder}
                        placeholderTextColor={colors.lightMauve}
                        style={styles.input}
                        autoCapitalize="none"
                        autoCorrect={false}
                    />
                </View>

                <Pressable
                    style={styles.addButton}
                    onPress={handleAddPress}
                    accessibilityRole="button"
                    accessibilityLabel="Adicionar pet"
                >
                    <Image
                        source={require('../../assets/plus.svg')}
                        style={styles.plusIcon}
                        resizeMode="contain"
                    />
                </Pressable>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        width: '100%',
        paddingHorizontal: 10,
        paddingVertical: 8,
        backgroundColor: colors.lightMauve,
        borderTopLeftRadius: 15,
        borderTopRightRadius: 15,
        justifyContent: 'center',
    },
    inputWrapper: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 8,
    },
    inputContainer: {
        flex: 1,
        position: 'relative',
        justifyContent: 'center',
    },
    searchIcon: {
        position: 'absolute',
        left: 12,
        zIndex: 1,
    },
    input: {
        borderRadius: 20,
        paddingLeft: 38,
        paddingRight: 14,
        paddingVertical: 8,
        fontSize: 15,
        color: colors.textStrong,
        backgroundColor: colors.white,
        minHeight: 36,
    },
    addButton: {
        width: 36,
        height: 36,
        borderRadius: 22,
        backgroundColor: colors.white,
        alignItems: 'center',
        justifyContent: 'center',
    },
    plusIcon: {
        width: 28,
        height: 28,
    },
});
