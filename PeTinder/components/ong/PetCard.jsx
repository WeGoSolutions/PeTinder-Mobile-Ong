import { useEffect, useMemo, useState } from 'react';
import { Image, Pressable, StyleSheet, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';
import { colors } from '../../constants/theme';
import { resolveImageUri } from '../../utils/imageUri';

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
    onPress,
}) {
    const router = useRouter();
    const [imageError, setImageError] = useState(false);
    const resolvedImageUrl = useMemo(() => resolveImageUri(imageUrl), [imageUrl]);

    useEffect(() => {
        setImageError(false);
    }, [resolvedImageUrl]);

    const handleEdit = () => {
        router.push({
            pathname: '/ong/petForm',
            params: {
                mode: 'edit',
                petId: String(id),
                backTo: '/ong/pets',
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
            onPress={onPress || handleEdit}
            accessibilityRole="button"
            accessibilityLabel={`Editar pet ${name || 'sem nome'}`}
        >
            <View style={styles.imageContainer}>
                {resolvedImageUrl && !imageError ? (
                    <Image
                        source={{ uri: resolvedImageUrl }}
                        style={styles.image}
                        resizeMode="cover"
                        onError={() => setImageError(true)}
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
        borderWidth: 6,
        borderRadius: 12,
        overflow: 'hidden',
        borderColor: colors.strongPink,
        backgroundColor: colors.strongPink,
    },
    cardAdopted: {
        borderColor: colors.mauve,
        backgroundColor: colors.mauve,
    },
    imageContainer: {
        height: 165,
        backgroundColor: colors.roseSurface,
        borderRadius: 12,
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
        paddingHorizontal: 6,
        paddingVertical: 6,
        gap: 10,
    },
    name: {
        flex: 1,
        fontSize: 20,
        fontWeight: '600',
        color: colors.white,
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
