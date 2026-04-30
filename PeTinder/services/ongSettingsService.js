import api from '../api';
import mockData from '../data/db.json';

const useBackend = String(process.env.EXPO_PUBLIC_UTILIZAR_BACKEND ?? '').toLowerCase() === 'true';

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

export default {
  getOngProfile,
  updateOngProfile,
  normalizeBackendError,
};
