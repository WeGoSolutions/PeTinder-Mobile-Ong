import { Ionicons } from '@expo/vector-icons';
import { Image, ScrollView, StyleSheet, Text, View } from 'react-native';
import { colors, scaleFont, scaleHeight, scaleWidth, typography } from '../../constants/theme';

const normalizeLabel = (value = '') =>
    value
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
        .toLowerCase();

const getNeedColor = (value) => {
    const normalized = normalizeLabel(value);

    if (normalized.includes('castr')) {
        return '#C83434';
    }

    if (normalized.includes('vermif')) {
        return '#118E2D';
    }

    if (normalized.includes('vacin')) {
        return '#E8A20D';
    }

    return colors.black;
};

const renderNeedParts = (needs) => {
    if (!Array.isArray(needs) || needs.length === 0) {
        return <Text style={styles.needText}>Nenhuma pendencia</Text>;
    }

    const parts = [];

    needs.forEach((need, index) => {
        if (index > 0) {
            const isLast = index === needs.length - 1;
            parts.push(
                <Text key={`sep-${index}`} style={styles.separatorText}>
                    {isLast ? ' e ' : ', '}
                </Text>
            );
        }

        parts.push(
            <Text key={`need-${index}`} style={[styles.needText, { color: getNeedColor(need) }]}>
                {need}
            </Text>
        );
    });

    return parts;
};

export default function OngPendenciasList({ pets = [] }) {
    return (
        <View style={styles.container}>
            <Text style={styles.title}>Pets nao vacinados / vermifugados / castrados:</Text>

            <View style={styles.listShell}>
                {pets.length === 0 ? (
                    <View style={styles.emptyContainer}>
                        <Text style={styles.emptyText}>Nenhum pet com pendencias no momento.</Text>
                    </View>
                ) : (
                    <ScrollView
                        style={styles.scroll}
                        contentContainerStyle={styles.scrollContent}
                        showsVerticalScrollIndicator={true}
                        nestedScrollEnabled={true}
                    >
                        {pets.map((pet, index) => (
                            <View key={String(pet?.id ?? `${pet?.name}-${index}`)} style={styles.card}>
                                {pet?.imageUrl ? (
                                    <Image source={{ uri: pet.imageUrl }} style={styles.petImage} />
                                ) : (
                                    <View style={styles.placeholderImage}>
                                        <Ionicons name="paw-outline" size={26} color={colors.mauve} />
                                    </View>
                                )}

                                <Text style={styles.cardText}>
                                    <Text style={styles.petNameText}>{pet?.name ?? 'Pet'}</Text>
                                    <Text style={styles.baseText}> necessita de: </Text>
                                    {renderNeedParts(pet?.needs)}
                                </Text>
                            </View>
                        ))}
                    </ScrollView>
                )}
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        paddingHorizontal: scaleWidth(20),
        paddingTop: scaleHeight(8),
        paddingBottom: scaleHeight(6),
    },
    title: {
        fontSize: scaleFont(36),
        lineHeight: scaleFont(42),
        fontFamily: typography.fontFamily.poppins.bold,
        color: colors.black,
        marginBottom: scaleHeight(14),
    },
    listShell: {
        maxHeight: scaleHeight(330),
        borderRadius: scaleWidth(10),
        paddingRight: scaleWidth(2),
    },
    scroll: {
        flex: 1,
    },
    scrollContent: {
        gap: scaleHeight(14),
        paddingBottom: scaleHeight(8),
    },
    card: {
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: '#D5CBD0',
        borderRadius: scaleWidth(10),
        paddingHorizontal: scaleWidth(14),
        paddingVertical: scaleHeight(14),
        gap: scaleWidth(12),
    },
    petImage: {
        width: scaleWidth(68),
        height: scaleWidth(68),
        borderRadius: scaleWidth(34),
        borderWidth: scaleWidth(3),
        borderColor: '#FF76B0',
        backgroundColor: '#EFE8EC',
    },
    placeholderImage: {
        width: scaleWidth(68),
        height: scaleWidth(68),
        borderRadius: scaleWidth(34),
        borderWidth: scaleWidth(3),
        borderColor: '#FF76B0',
        backgroundColor: '#EFE8EC',
        alignItems: 'center',
        justifyContent: 'center',
    },
    cardText: {
        flex: 1,
        flexWrap: 'wrap',
        fontSize: scaleFont(17),
        lineHeight: scaleFont(24),
        color: colors.black,
    },
    petNameText: {
        fontFamily: typography.fontFamily.poppins.semiBold,
        color: colors.black,
    },
    baseText: {
        fontFamily: typography.fontFamily.poppins.regular,
        color: colors.black,
    },
    needText: {
        fontFamily: typography.fontFamily.poppins.medium,
    },
    separatorText: {
        color: colors.black,
        fontFamily: typography.fontFamily.poppins.regular,
    },
    emptyContainer: {
        borderRadius: scaleWidth(10),
        backgroundColor: '#EEE6EA',
        paddingHorizontal: scaleWidth(16),
        paddingVertical: scaleHeight(18),
    },
    emptyText: {
        color: colors.black,
        fontSize: scaleFont(16),
        textAlign: 'center',
    },
});
