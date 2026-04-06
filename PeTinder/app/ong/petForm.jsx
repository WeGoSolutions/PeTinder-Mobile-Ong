import { useEffect, useMemo, useState } from 'react';
import { Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import { useLocalSearchParams } from 'expo-router';
import { colors } from '../../constants/theme';
import PetFormInput from '../../components/ong/PetFormInput';
import DynamicButton from '../../components/DynamicButton';
import { getSession } from '../../services/sessionService';

export default function PetForm() {
    const { mode } = useLocalSearchParams();
    const normalizedMode = typeof mode === 'string' ? mode.trim().toLowerCase() : '';
    const isEdit = normalizedMode === 'edit';

    const [nome, setNome] = useState('');
    const [idade, setIdade] = useState('');
    const [porte, setPorte] = useState('');
    const [tagsInput, setTagsInput] = useState('');
    const [descricao, setDescricao] = useState('');
    const [isCastrado, setIsCastrado] = useState(false);
    const [isVermifugo, setIsVermifugo] = useState(false);
    const [isVacinado, setIsVacinado] = useState(false);
    const [sexo, setSexo] = useState('');
    const [ongId, setOngId] = useState('');

    useEffect(() => {
        const loadOngId = async () => {
            const session = await getSession();
            setOngId(String(session?.ongId ?? ''));
        };

        loadOngId();
    }, []);

    const payload = useMemo(() => {
        const idadeNumerica = Number.parseFloat(String(idade).replace(',', '.'));
        const tags = tagsInput
            .split(',')
            .map((item) => item.trim())
            .filter(Boolean);

        return {
            nome: nome.trim(),
            idade: Number.isFinite(idadeNumerica) ? idadeNumerica : null,
            porte: porte || null,
            tags,
            descricao: descricao.trim(),
            isCastrado,
            isVermifugo,
            isVacinado,
            sexo: sexo || null,
            ongId: ongId || null,
        };
    }, [nome, idade, porte, tagsInput, descricao, isCastrado, isVermifugo, isVacinado, sexo, ongId]);

    const handleSave = () => {
        // TODO: integrar com service de criar/editar pet
        console.log('Payload Pet:', payload);
    };

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <View style={styles.section}>
                <PetFormInput
                    label="Nome do Pet"
                    value={nome}
                    onChangeText={setNome}
                    placeholder="Ex: Luna"
                />

                {/* arrumar aqui */}
                <PetFormInput
                    label="Idade"
                    value={idade}
                    onChangeText={setIdade}
                    placeholder="Ex: 2.5"
                    keyboardType="decimal-pad"
                />

                <View style={styles.group}>
                    <Text style={styles.groupLabel}>Porte</Text>
                    <View style={styles.optionRow}>
                        {['pequeno', 'medio', 'grande'].map((item) => (
                            <Pressable
                                key={item}
                                style={[styles.optionChip, porte === item && styles.optionChipActive]}
                                onPress={() => setPorte(item)}
                            >
                                <Text style={[styles.optionText, porte === item && styles.optionTextActive]}>{item}</Text>
                            </Pressable>
                        ))}
                    </View>
                </View>

                <PetFormInput
                    label="Tags"
                    value={tagsInput}
                    onChangeText={setTagsInput}
                    placeholder="Ex: dócil, brincalhão, sociável"
                />

                <PetFormInput
                    label="Descrição"
                    value={descricao}
                    onChangeText={setDescricao}
                    placeholder="Conte sobre o pet"
                    multiline
                    numberOfLines={4}
                />

                <View style={styles.group}>
                    <Text style={styles.groupLabel}>Sexo</Text>
                    <View style={styles.optionRow}>
                        {['femea', 'macho'].map((item) => (
                            <Pressable
                                key={item}
                                style={[styles.optionChip, sexo === item && styles.optionChipActive]}
                                onPress={() => setSexo(item)}
                            >
                                <Text style={[styles.optionText, sexo === item && styles.optionTextActive]}>{item}</Text>
                            </Pressable>
                        ))}
                    </View>
                </View>

                <View style={styles.group}>
                    <Text style={styles.groupLabel}>Cuidados</Text>
                    <View style={styles.optionRow}>
                        <Pressable
                            style={[styles.optionChip, isCastrado && styles.optionChipActive]}
                            onPress={() => setIsCastrado((prev) => !prev)}
                        >
                            <Text style={[styles.optionText, isCastrado && styles.optionTextActive]}>Castrado</Text>
                        </Pressable>

                        <Pressable
                            style={[styles.optionChip, isVermifugo && styles.optionChipActive]}
                            onPress={() => setIsVermifugo((prev) => !prev)}
                        >
                            <Text style={[styles.optionText, isVermifugo && styles.optionTextActive]}>Vermífugo</Text>
                        </Pressable>

                        <Pressable
                            style={[styles.optionChip, isVacinado && styles.optionChipActive]}
                            onPress={() => setIsVacinado((prev) => !prev)}
                        >
                            <Text style={[styles.optionText, isVacinado && styles.optionTextActive]}>Vacinado</Text>
                        </Pressable>
                    </View>
                </View>

                <DynamicButton variant="primary" onPress={handleSave}>
                    {isEdit ? 'Salvar alterações' : 'Cadastrar pet'}
                </DynamicButton>
            </View>
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    container: {
        backgroundColor: colors.white,
        paddingHorizontal: 16,
        paddingVertical: 16,
        gap: 10,
    },
    title: {
        fontSize: 24,
        fontWeight: '700',
        color: colors.textStrong,
    },
    subtitle: {
        fontSize: 14,
        color: colors.mauve,
        marginBottom: 8,
    },
    section: {
        borderWidth: 1,
        borderColor: colors.roseBorder,
        backgroundColor: colors.roseSurface,
        borderRadius: 14,
        padding: 12,
        gap: 14,
    },
    group: {
        gap: 8,
    },
    groupLabel: {
        fontSize: 14,
        fontWeight: '600',
        color: colors.textStrong,
    },
    optionRow: {
        flexDirection: 'row',
        flexWrap: 'wrap',
        gap: 8,
    },
    optionChip: {
        borderWidth: 1,
        borderColor: colors.roseBorder,
        borderRadius: 999,
        paddingHorizontal: 12,
        paddingVertical: 8,
        backgroundColor: colors.white,
    },
    optionChipActive: {
        backgroundColor: colors.mauve,
        borderColor: colors.mauve,
    },
    optionText: {
        fontSize: 13,
        color: colors.textStrong,
        fontWeight: '500',
        textTransform: 'capitalize',
    },
    optionTextActive: {
        color: colors.white,
    },
});
