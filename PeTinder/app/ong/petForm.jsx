import { useCallback, useEffect, useMemo, useState } from 'react';
import { Image, Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import { useFocusEffect, useLocalSearchParams, useRouter } from 'expo-router';
import * as ImagePicker from 'expo-image-picker';
import { colors } from '../../constants/theme';
import PetFormInput from '../../components/ong/PetFormInput';
import Tag from '../../components/ong/Tag';
import Toast from '../../components/Toast';
import DynamicButton from '../../components/DynamicButton';
import { getSession } from '../../services/sessionService';
import { atualizarPet, criarPet } from '../../services/petApiService';

const MAX_IMAGES = 5;
const MAX_TAGS = 7;
const UUID_REGEX = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
const ALL_TAGS = [
    ['Ativo', 'Calmo', 'Brincalhão'],
    ['Carinhoso', 'Curioso', 'Independente'],
    ['Protetor', 'Sociável', 'Medroso'],
    ['Territorial', 'Obediente', 'Teimoso'],
];

const formatAssetName = (asset, index) => {
    if (asset?.fileName && asset.fileName.trim().length > 0) {
        return asset.fileName;
    }

    const extension = asset?.mimeType?.split('/')[1] || 'jpg';
    return `pet-image-${Date.now()}-${index + 1}.${extension}`;
};

export default function PetForm() {
    const { mode, petId, nome: paramNome, idade: paramIdade, porte: paramPorte, tags: paramTags, descricao: paramDescricao, isCastrado: paramIsCastrado, isVermifugo: paramIsVermifugo, isVacinado: paramIsVacinado, sexo: paramSexo, imageUrls: paramImageUrls } = useLocalSearchParams();
    const router = useRouter();
    const normalizedMode = typeof mode === 'string' ? mode.trim().toLowerCase() : '';
    const isEdit = normalizedMode === 'edit';
    const resolvedPetId = typeof petId === 'string' ? petId.trim() : '';

    const [nome, setNome] = useState('');
    const [idadeInput, setIdadeInput] = useState('');
    const [idadeUnidade, setIdadeUnidade] = useState('anos');
    const [porte, setPorte] = useState('');
    const [selectedTags, setSelectedTags] = useState([]);
    const [descricao, setDescricao] = useState('');
    const [isCastrado, setIsCastrado] = useState(false);
    const [isVermifugo, setIsVermifugo] = useState(false);
    const [isVacinado, setIsVacinado] = useState(false);
    const [sexo, setSexo] = useState('');
    const [ongId, setOngId] = useState('');
    const [images, setImages] = useState([]);
    const [errors, setErrors] = useState({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [toast, setToast] = useState({ visible: false, title: '', message: '', type: 'info' });

    const showToast = useCallback((title, message, type = 'info', duration = 2400) => {
        setToast({ visible: true, title, message, type });

        setTimeout(() => {
            setToast((prev) => ({ ...prev, visible: false }));
        }, duration);
    }, []);

    const resetForm = useCallback(() => {
        setNome('');
        setIdadeInput('');
        setIdadeUnidade('anos');
        setPorte('');
        setSelectedTags([]);
        setDescricao('');
        setIsCastrado(false);
        setIsVermifugo(false);
        setIsVacinado(false);
        setSexo('');
        setImages([]);
        setErrors({});
        setIsSubmitting(false);
    }, []);

    useFocusEffect(
        useCallback(() => {
            return () => {
                resetForm();
            };
        }, [resetForm])
    );

    const navigateToPets = useCallback(() => {
        resetForm();
        router.replace({
            pathname: '/ong/pets',
            params: { refresh: String(Date.now()) },
        });
    }, [resetForm, router]);

    useEffect(() => {
        const loadOngId = async () => {
            const session = await getSession();
            setOngId(String(session?.ongId ?? ''));
        };

        loadOngId();
    }, []);

    useEffect(() => {
        if (!isEdit) {
            return;
        }

        if (typeof paramNome === 'string') {
            setNome(paramNome);
        }

        if (typeof paramPorte === 'string') {
            setPorte(paramPorte);
        }

        if (typeof paramDescricao === 'string') {
            setDescricao(paramDescricao);
        }

        if (typeof paramSexo === 'string') {
            setSexo(paramSexo.toUpperCase());
        }

        if (typeof paramIsCastrado === 'string') {
            setIsCastrado(paramIsCastrado === 'true');
        }

        if (typeof paramIsVermifugo === 'string') {
            setIsVermifugo(paramIsVermifugo === 'true');
        }

        if (typeof paramIsVacinado === 'string') {
            setIsVacinado(paramIsVacinado === 'true');
        }

        if (typeof paramTags === 'string') {
            setSelectedTags(paramTags.split('|').map((tag) => tag.trim()).filter(Boolean));
        }

        if (typeof paramIdade === 'string') {
            const parsedAge = Number.parseFloat(paramIdade.replace(',', '.'));
            if (Number.isFinite(parsedAge) && parsedAge > 0) {
                if (parsedAge < 1) {
                    setIdadeUnidade('meses');
                    setIdadeInput(String(Math.round(parsedAge * 12)));
                } else {
                    setIdadeUnidade('anos');
                    setIdadeInput(String(parsedAge));
                }
            }
        }

        if (typeof paramImageUrls === 'string' && paramImageUrls.trim().length > 0) {
            const urls = paramImageUrls.split('|').map((url) => url.trim()).filter(Boolean);
            setImages(urls.slice(0, MAX_IMAGES).map((uri, index) => ({
                uri,
                base64: '',
                fileName: `imagem-${index + 1}.jpg`,
            })));
        }
    }, [
        isEdit,
        paramNome,
        paramPorte,
        paramDescricao,
        paramSexo,
        paramIsCastrado,
        paramIsVermifugo,
        paramIsVacinado,
        paramTags,
        paramIdade,
        paramImageUrls,
    ]);

    const payload = useMemo(() => {
        const idadeNumerica = Number.parseFloat(String(idadeInput).replace(',', '.'));
        const idadeEmAnos = Number.isFinite(idadeNumerica)
            ? (idadeUnidade === 'meses' ? idadeNumerica / 12 : idadeNumerica)
            : null;
        const newImages = images.filter((item) => typeof item?.base64 === 'string' && item.base64.trim().length > 0);

        return {
            nome: nome,
            idade: idadeEmAnos,
            porte: porte || null,
            tags: selectedTags,
            descricao: descricao,
            isCastrado,
            isVermifugo,
            isVacinado,
            sexo: sexo || null,
            ongId: ongId || null,
            imagensBase64: newImages.map((item) => item.base64),
            nomesArquivos: newImages.map((item) => item.fileName),
        };
    }, [
        nome,
        idadeInput,
        idadeUnidade,
        porte,
        selectedTags,
        descricao,
        isCastrado,
        isVermifugo,
        isVacinado,
        sexo,
        ongId,
        images,
    ]);

    const validateForm = () => {
        const validationErrors = {};
        const tags = payload.tags;

        if (!payload.nome) {
            validationErrors.nome = 'Nome é obrigatório';
        } else if (payload.nome.length < 2 || payload.nome.length > 50) {
            validationErrors.nome = 'Nome deve ter entre 2 e 50 caracteres';
        }

        if (payload.idade === null) {
            validationErrors.idade = 'Idade é obrigatória';
        } else if (payload.idade <= 0) {
            validationErrors.idade = 'Idade deve ser maior que zero';
        }

        if (!payload.porte || !['Pequeno', 'Médio', 'Grande'].includes(payload.porte)) {
            validationErrors.porte = 'Porte deve ser Pequeno, Médio ou Grande';
        }

        if (!tags.length) {
            validationErrors.tags = 'Pet deve ter pelo menos uma tag';
        } else if (tags.length > MAX_TAGS) {
            validationErrors.tags = 'Máximo de 7 tags permitidas';
        }

        if (!payload.descricao) {
            validationErrors.descricao = 'Descrição é obrigatória';
        } else if (payload.descricao.length > 500) {
            validationErrors.descricao = 'Descrição deve ter no máximo 500 caracteres';
        }

        if (!payload.sexo || !['MACHO', 'FEMEA'].includes(payload.sexo)) {
            validationErrors.sexo = 'Sexo deve ser MACHO ou FEMEA';
        }

        if (!payload.ongId || !UUID_REGEX.test(payload.ongId)) {
            validationErrors.ongId = 'ONG é obrigatória';
        }

        if (images.length > MAX_IMAGES) {
            validationErrors.images = 'Máximo de 5 imagens permitidas';
        }

        setErrors(validationErrors);
        return Object.keys(validationErrors).length === 0;
    };

    const handleAddImages = async () => {
        const remaining = MAX_IMAGES - images.length;
        if (remaining <= 0) {
            showToast('Limite atingido', 'Você pode adicionar no máximo 5 imagens.', 'warning');
            return;
        }

        const permission = await ImagePicker.requestMediaLibraryPermissionsAsync();
        if (!permission.granted) {
            showToast('Permissão necessária', 'Permita acesso à galeria para selecionar imagens.', 'warning');
            return;
        }

        const result = await ImagePicker.launchImageLibraryAsync({
            mediaTypes: ImagePicker.MediaTypeOptions.Images,
            allowsMultipleSelection: true,
            selectionLimit: remaining,
            quality: 0.7,
            base64: true,
        });

        if (result.canceled || !result.assets?.length) {
            return;
        }

        const formatted = result.assets
            .filter((asset) => Boolean(asset.base64))
            .map((asset, index) => ({
                uri: asset.uri,
                base64: asset.base64,
                fileName: formatAssetName(asset, index),
            }));

        setImages((prev) => [...prev, ...formatted].slice(0, MAX_IMAGES));
    };

    const handleRemoveImage = (indexToRemove) => {
        setImages((prev) => prev.filter((_, index) => index !== indexToRemove));
    };

    const handleTagToggle = (tagName) => {
        setSelectedTags((prev) => {
            if (prev.includes(tagName)) {
                return prev.filter((tag) => tag !== tagName);
            }

            if (prev.length >= MAX_TAGS) {
                showToast('Limite de tags', 'Você pode selecionar no máximo 7 tags.', 'warning');
                return prev;
            }

            return [...prev, tagName];
        });
    };

    const handleSave = async () => {
        if (!validateForm()) {
            showToast('Campos inválidos', 'Revise os campos obrigatórios do formulário.', 'error');
            return;
        }

        // Validação rápida do caminho antes de enviar ao backend.
        if (isEdit && !resolvedPetId) {
            showToast('Rota inválida', 'Modo edição sem ID do pet. Abra a edição novamente.', 'error');
            return;
        }

        try {
            setIsSubmitting(true);

            if (isEdit) {
                await atualizarPet(resolvedPetId, payload);
            } else {
                await criarPet(payload);
            }

            showToast(
                'Sucesso',
                isEdit ? 'Pet atualizado com sucesso.' : 'Pet cadastrado com sucesso.',
                'success',
                1200
            );
            setTimeout(() => {
                navigateToPets();
            }, 1200);
        } catch (error) {
            const apiMessage =
                error?.response?.data?.message ||
                error?.response?.data?.error ||
                null;

            showToast(
                'Erro ao salvar pet',
                apiMessage || 'Não foi possível salvar o pet. Tente novamente.',
                'error',
                3200
            );
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <View style={styles.section}>
                <PetFormInput
                    label="Nome do Pet"
                    value={nome}
                    onChangeText={setNome}
                    placeholder="Ex: Luna"
                    error={errors.nome}
                />

                <View style={styles.ageRow}>
                    <View style={styles.ageValue}>
                        <PetFormInput
                            label="Idade"
                            value={idadeInput}
                            onChangeText={setIdadeInput}
                            placeholder="Ex: 2"
                            keyboardType="decimal-pad"
                            error={errors.idade}
                        />
                    </View>

                    <View style={styles.ageUnit}>
                        <Text style={styles.groupLabel}>Unidade</Text>
                        <View style={styles.optionRow}>
                            {['anos', 'meses'].map((item) => (
                                <Pressable
                                    key={item}
                                    style={[styles.optionChip, idadeUnidade === item && styles.optionChipActive]}
                                    onPress={() => setIdadeUnidade(item)}
                                >
                                    <Text style={[styles.optionText, idadeUnidade === item && styles.optionTextActive]}>{item}</Text>
                                </Pressable>
                            ))}
                        </View>
                    </View>
                </View>

                <View style={styles.group}>
                    <Text style={styles.groupLabel}>Porte</Text>
                    <View style={styles.optionRow}>
                        {['Pequeno', 'Médio', 'Grande'].map((item) => (
                            <Pressable
                                key={item}
                                style={[styles.optionChip, porte === item && styles.optionChipActive]}
                                onPress={() => setPorte(item)}
                            >
                                <Text style={[styles.optionText, porte === item && styles.optionTextActive]}>{item}</Text>
                            </Pressable>
                        ))}
                    </View>
                    {errors.porte ? <Text style={styles.errorText}>{errors.porte}</Text> : null}
                </View>

                <View style={styles.group}>
                    <Text style={styles.groupLabel}>Tags ({selectedTags.length}/{MAX_TAGS})</Text>
                    {ALL_TAGS.map((row, rowIndex) => (
                        <View key={`tag-row-${rowIndex}`} style={styles.tagRow}>
                            {row.map((tag) => {
                                const isSelected = selectedTags.includes(tag);
                                return (
                                    <Tag
                                        key={tag}
                                        tagName={tag}
                                        isDisabled={!isSelected}
                                        onPress={() => handleTagToggle(tag)}
                                    />
                                );
                            })}
                        </View>
                    ))}
                    {errors.tags ? <Text style={styles.errorText}>{errors.tags}</Text> : null}
                </View>

                <PetFormInput
                    label="Descrição"
                    value={descricao}
                    onChangeText={setDescricao}
                    placeholder="Conte sobre o pet"
                    multiline
                    numberOfLines={4}
                    maxLength={500}
                    error={errors.descricao}
                />

                <View style={styles.group}>
                    <Text style={styles.groupLabel}>Sexo</Text>
                    <View style={styles.optionRow}>
                        {[
                            { label: 'Fêmea', value: 'FEMEA' },
                            { label: 'Macho', value: 'MACHO' },
                        ].map((item) => (
                            <Pressable
                                key={item.value}
                                style={[styles.optionChip, sexo === item.value && styles.optionChipActive]}
                                onPress={() => setSexo(item.value)}
                            >
                                <Text style={[styles.optionText, sexo === item.value && styles.optionTextActive]}>{item.label}</Text>
                            </Pressable>
                        ))}
                    </View>
                    {errors.sexo ? <Text style={styles.errorText}>{errors.sexo}</Text> : null}
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

                <View style={styles.group}>
                    <Text style={styles.groupLabel}>Imagens do pet ({images.length}/{MAX_IMAGES})</Text>
                    <Pressable style={styles.addImageButton} onPress={handleAddImages}>
                        <Text style={styles.addImageButtonText}>Adicionar imagens</Text>
                    </Pressable>

                    {errors.images ? <Text style={styles.errorText}>{errors.images}</Text> : null}

                    <View style={styles.imagesGrid}>
                        {images.map((item, index) => (
                            <View key={`${item.uri}-${index}`} style={styles.imageItem}>
                                <Image source={{ uri: item.uri }} style={styles.imagePreview} />
                                <Pressable
                                    style={styles.removeImageButton}
                                    onPress={() => handleRemoveImage(index)}
                                >
                                    <Text style={styles.removeImageText}>X</Text>
                                </Pressable>
                            </View>
                        ))}
                    </View>
                </View>

                {errors.ongId ? <Text style={styles.errorText}>{errors.ongId}</Text> : null}

                <DynamicButton variant="primary" onPress={handleSave} isLoading={isSubmitting} disabled={isSubmitting}>
                    {isEdit ? 'Salvar alterações' : 'Cadastrar pet'}
                </DynamicButton>
            </View>

            <Toast
                visible={toast.visible}
                title={toast.title}
                message={toast.message}
                type={toast.type}
            />
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
    tagRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: 8,
    },
    optionChip: {
        borderWidth: 2,
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
    ageRow: {
        flexDirection: 'row',
        alignItems: 'flex-start',
        gap: 10,
    },
    ageValue: {
        flex: 1,
    },
    ageUnit: {
        flex: 1,
        gap: 8,
    },
    addImageButton: {
        borderWidth: 1,
        borderColor: colors.mauve,
        borderRadius: 10,
        paddingVertical: 10,
        alignItems: 'center',
        backgroundColor: colors.white,
    },
    addImageButtonText: {
        color: colors.mauve,
        fontWeight: '600',
        fontSize: 14,
    },
    imagesGrid: {
        flexDirection: 'row',
        flexWrap: 'wrap',
        gap: 8,
    },
    imageItem: {
        width: 90,
        height: 90,
        borderRadius: 10,
        overflow: 'hidden',
        position: 'relative',
        borderWidth: 1,
        borderColor: colors.roseBorder,
    },
    imagePreview: {
        width: '100%',
        height: '100%',
    },
    removeImageButton: {
        position: 'absolute',
        top: 4,
        right: 4,
        width: 22,
        height: 22,
        borderRadius: 11,
        backgroundColor: 'rgba(0,0,0,0.65)',
        alignItems: 'center',
        justifyContent: 'center',
    },
    removeImageText: {
        color: colors.white,
        fontSize: 11,
        fontWeight: '700',
    },
    errorText: {
        fontSize: 12,
        color: '#D14343',
    },
});
