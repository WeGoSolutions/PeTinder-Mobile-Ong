import api from '../api';
import mockData from '../data/db.json';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as FileSystem from 'expo-file-system/legacy';

const useBackend = String(process.env.EXPO_PUBLIC_UTILIZAR_BACKEND ?? '').toLowerCase() === 'true';
const ONG_IMAGE_CACHE_PREFIX = 'ong_image_cache:';

const getString = (value) => {
  if (typeof value !== 'string') {
    return '';
  }

  return value;
};

const trimOrEmpty = (value) => getString(value).trim();

const digitsOnly = (value) => getString(value).replace(/\D/g, '');

const nullWhenEmpty = (value) => {
  const normalized = trimOrEmpty(value);
  return normalized.length > 0 ? normalized : null;
};

const normalizeImageUrl = (value) => {
  const normalized = trimOrEmpty(value);
  return normalized.length > 0 ? normalized : null;
};

const getOngImageCacheKey = (ongId) => `${ONG_IMAGE_CACHE_PREFIX}${trimOrEmpty(String(ongId ?? ''))}`;

const getCachedOngImage = async (ongId) => {
  const normalizedOngId = trimOrEmpty(String(ongId ?? ''));

  if (!normalizedOngId) {
    return null;
  }

  try {
    const cached = await AsyncStorage.getItem(getOngImageCacheKey(normalizedOngId));
    return normalizeImageUrl(cached);
  } catch (error) {
    return null;
  }
};

const saveCachedOngImage = async (ongId, imageUri) => {
  const normalizedOngId = trimOrEmpty(String(ongId ?? ''));
  const normalizedImageUri = trimOrEmpty(imageUri);

  if (!normalizedOngId || !normalizedImageUri) {
    return;
  }

  try {
    await AsyncStorage.setItem(getOngImageCacheKey(normalizedOngId), normalizedImageUri);
  } catch (error) {
    // O cache local nao deve bloquear o fluxo de salvamento do perfil.
  }
};

// Persiste a imagem como ARQUIVO no diretorio do app e devolve a URI do arquivo.
// Guardar base64 grande direto no AsyncStorage estoura o limite por linha no
// Android ("Row too big to fit into CursorWindow") e falha em silencio — por
// isso a imagem "nao aparecia". Salvando em arquivo, o AsyncStorage guarda so o
// caminho (string pequena). Se algo falhar, devolve o data URL original como
// fallback para ao menos funcionar na sessao atual.
const saveOngImageToFile = async (ongId, imageDataUrl) => {
  const dataUrlMatch = /^data:(image\/[\w.+-]+);base64,(.*)$/s.exec(imageDataUrl);

  if (!dataUrlMatch) {
    // Nao e um data URL base64 (ja e uma uri de arquivo/remota): usa como esta.
    return imageDataUrl;
  }

  const baseDir = FileSystem.documentDirectory || FileSystem.cacheDirectory;
  if (!baseDir) {
    return imageDataUrl;
  }

  try {
    const mimeType = dataUrlMatch[1];
    const base64 = dataUrlMatch[2];
    const extension = (mimeType.split('/')[1] || 'jpg').split('+')[0];
    const fileUri = `${baseDir}ong-avatar-${ongId}-${Date.now()}.${extension}`;

    await FileSystem.writeAsStringAsync(fileUri, base64, {
      encoding: FileSystem.EncodingType.Base64,
    });

    return fileUri;
  } catch (error) {
    return imageDataUrl;
  }
};

const normalizeProfileResponse = (data, fallbackName = '') => {
  const endereco = data?.endereco ?? {};

  return {
    id: getString(data?.id),
    cnpj: digitsOnly(data?.cnpj),
    cpf: digitsOnly(data?.cpf),
    nome: trimOrEmpty(data?.nome) || fallbackName,
    razaoSocial: trimOrEmpty(data?.razaoSocial),
    email: trimOrEmpty(data?.email),
    link: trimOrEmpty(data?.link),
    cep: trimOrEmpty(endereco?.cep),
    rua: trimOrEmpty(endereco?.rua),
    numero: trimOrEmpty(endereco?.numero),
    cidade: trimOrEmpty(endereco?.cidade),
    uf: trimOrEmpty(endereco?.uf).toUpperCase(),
    complemento: trimOrEmpty(endereco?.complemento),
  };
};

const buildUpdatePayload = (profile) => ({
  cnpj: nullWhenEmpty(digitsOnly(profile.cnpj)),
  cpf: nullWhenEmpty(digitsOnly(profile.cpf)),
  nome: trimOrEmpty(profile.nome),
  razaoSocial: nullWhenEmpty(profile.razaoSocial),
  email: trimOrEmpty(profile.email),
  link: trimOrEmpty(profile.link),
  endereco: {
    cep: trimOrEmpty(profile.cep),
    rua: trimOrEmpty(profile.rua),
    numero: trimOrEmpty(profile.numero),
    cidade: trimOrEmpty(profile.cidade),
    uf: trimOrEmpty(profile.uf).toUpperCase(),
    complemento: trimOrEmpty(profile.complemento),
  },
});

const buildMockProfile = (ongId, fallbackName = '') => {
  const mockOng = mockData?.ong ?? {};

  return {
    id: String(ongId ?? mockOng?.id ?? ''),
    cnpj: '00000000000000',
    cpf: '34061800000',
    nome: trimOrEmpty(mockOng?.name) || fallbackName || 'ONG',
    razaoSocial: 'Associacao Aumigos do Bem',
    email: trimOrEmpty(mockOng?.email) || 'example@gmail.com',
    link: 'https://wa.me/5599999999999',
    cep: '08290-001',
    rua: 'R. Victorio Santim',
    numero: '2776',
    cidade: 'Sao Paulo',
    uf: 'SP',
    complemento: 'Apartamento 8A',
  };
};

export const getOngProfile = async (ongId, fallbackName = '') => {
  const normalizedOngId = trimOrEmpty(String(ongId ?? ''));

  if (!normalizedOngId) {
    throw new Error('ONG nao encontrada na sessao.');
  }

  if (!useBackend) {
    return buildMockProfile(normalizedOngId, fallbackName);
  }

  const response = await api.get(`/ongs/${normalizedOngId}`);
  return normalizeProfileResponse(response?.data, fallbackName);
};

export const getOngImage = async (ongId) => {
  const normalizedOngId = trimOrEmpty(String(ongId ?? ''));

  if (!normalizedOngId) {
    throw new Error('ONG nao encontrada na sessao.');
  }

  const cachedImage = await getCachedOngImage(normalizedOngId);
  if (cachedImage) {
    return cachedImage;
  }

  if (!useBackend) {
    return null;
  }

  const response = await api.get(`/ongs/${normalizedOngId}/imagem/arquivo`);
  return normalizeImageUrl(response?.data?.imageUrl);
};

export const updateOngImage = async (ongId, imageDataUrl) => {
  const normalizedOngId = trimOrEmpty(String(ongId ?? ''));

  if (!normalizedOngId) {
    throw new Error('ONG nao encontrada na sessao.');
  }

  const normalizedImageDataUrl = trimOrEmpty(imageDataUrl);

  if (!normalizedImageDataUrl) {
    throw new Error('Imagem da ONG invalida.');
  }

  // Salva a imagem em arquivo e guarda apenas o caminho (string pequena) no
  // cache local — assim funciona com QUALQUER tamanho/tipo de imagem.
  const persistedUri = await saveOngImageToFile(normalizedOngId, normalizedImageDataUrl);
  await saveCachedOngImage(normalizedOngId, persistedUri);

  // Contorno temporario: upload no backend desativado para evitar erro 500.
  return persistedUri;
};

export const updateOngProfile = async (ongId, profile, fallbackName = '') => {
  const normalizedOngId = trimOrEmpty(String(ongId ?? ''));

  if (!normalizedOngId) {
    throw new Error('ONG nao encontrada na sessao.');
  }

  const payload = buildUpdatePayload(profile);

  if (!useBackend) {
    return normalizeProfileResponse({
      ...payload,
      id: normalizedOngId,
      endereco: payload.endereco,
    }, fallbackName);
  }

  const response = await api.patch(`/ongs/${normalizedOngId}`, payload);
  return normalizeProfileResponse(response?.data, fallbackName);
};

export const normalizeBackendError = (error) => {
  const status = error?.response?.status;
  const backendMessage = error?.response?.data?.error || error?.response?.data?.message;

  if (typeof backendMessage === 'string' && backendMessage.trim().length > 0) {
    return backendMessage;
  }

  if (status === 400) {
    return 'Os dados informados sao invalidos.';
  }

  if (status === 401) {
    return 'Sessao invalida. Faca login novamente.';
  }

  if (status === 404) {
    return 'Cadastro da ONG nao encontrado.';
  }

  if (status === 409) {
    return 'Ja existe uma ONG com esses dados.';
  }

  return 'Nao foi possivel salvar as informacoes. Tente novamente.';
};

export const changeOngPassword = async (ongId, senhaAtual, novaSenha) => {
  const normalizedOngId = trimOrEmpty(String(ongId ?? ''));

  if (!normalizedOngId) {
    throw new Error('ONG nao encontrada na sessao.');
  }

  if (!useBackend) {
    return { success: true };
  }

  const response = await api.patch(`/ongs/${normalizedOngId}/senha`, { senhaAtual, novaSenha });
  return response?.data;
};

export default {
  getOngProfile,
  getOngImage,
  updateOngImage,
  updateOngProfile,
  changeOngPassword,
  normalizeBackendError,
};
