import { Image, Pressable, StyleSheet, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';
import { colors } from '../../constants/theme';

export default function PetCard({
    id,
    name,
    imageUrl,
    imageUrls = [],
    idade,
    porte,
    tags = [],
    descricao,
    isCastrado = false,
    isVermifugo = false,
    isVacinado = false,
    sexo,
    isAdopted = false,
}) {
    const router = useRouter();

    const handleEdit = () => {
        router.push({
            pathname: '/ong/petForm',
            params: {
                mode: 'edit',
                petId: String(id),
                from: '/ong/pets',
                nome: name || '',
                idade: idade !== undefined && idade !== null ? String(idade) : '',
                porte: porte || '',
                tags: tags.join('|'),
                descricao: descricao || '',
                isCastrado: String(Boolean(isCastrado)),
                isVermifugo: String(Boolean(isVermifugo)),
                isVacinado: String(Boolean(isVacinado)),
                sexo: sexo || '',
                imageUrls: imageUrls.join('|'),
            },
        });
    };

    return (
        <Pressable
            style={[styles.card, isAdopted && styles.cardAdopted]}
            onPress={handleEdit}
            accessibilityRole="button"
            accessibilityLabel={`Editar pet ${name || 'sem nome'}`}
        >
            <View style={styles.imageContainer}>
                {imageUrl ? (
                    <Image
                        source={{ uri: imageUrl }}
                        style={styles.image}
                        resizeMode="cover"
                    />
                ) : (
                    <View style={styles.placeholderContainer}>
                        <Ionicons name="image-outline" size={32} color={colors.mauve} />
                    </View>
                )}
            </View>

            <View style={styles.footer}>
                <Text style={styles.name} numberOfLines={1}>
                    {name || 'Sem nome'}
                </Text>
            </View>
        </Pressable>
    );
}

const styles = StyleSheet.create({
    card: {
        borderWidth: 1,
        borderColor: colors.roseBorder,
        borderRadius: 12,
        overflow: 'hidden',
        backgroundColor: colors.white,
    },
    cardAdopted: {
        opacity: 0.75,
    },
    imageContainer: {
        height: 140,
        backgroundColor: colors.roseSurface,
    },
    image: {
        width: '100%',
        height: '100%',
    },
    placeholderContainer: {
        width: '100%',
        height: '100%',
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: colors.roseSurface,
    },
    footer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        paddingHorizontal: 12,
        paddingVertical: 10,
        gap: 10,
    },
    name: {
        flex: 1,
        fontSize: 15,
        fontWeight: '700',
        color: colors.textStrong,
    },
    actions: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 8,
    },
    actionButton: {
        width: 28,
        height: 28,
        borderRadius: 8,
        alignItems: 'center',
        justifyContent: 'center',
    },
    editButton: {
        backgroundColor: colors.mauve,
    },
    deleteButton: {
        backgroundColor: colors.primaryPink,
    },
});
