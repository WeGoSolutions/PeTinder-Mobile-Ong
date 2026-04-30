import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { ActivityIndicator, Alert, StyleSheet, View } from 'react-native';
import { useRouter } from 'expo-router';
import { Colors, Spacing } from '../theme';
import { getSession, saveSession } from '../services/sessionService';
import { logout } from '../services/logoutService';
import {
  getOngProfile,
  normalizeBackendError,
  updateOngProfile,
} from '../services/ongSettingsService';
import { validateCEP, validateEmail, validateURL } from '../utils/validators';
import AppText from '../components/atoms/AppText';
import FooterActions from '../components/molecules/FooterActions';
import FormSection from '../components/molecules/FormSection';
import ProfileHero from '../components/molecules/ProfileHero';
import TabBar from '../components/molecules/TabBar';

const MAIN_TABS = ['Conta', 'Configurações'];
const SUB_TABS = ['Informações Pessoais', 'Endereço'];

const EMPTY_PROFILE = {
  id: '',
  cnpj: '',
  cpf: '',
  nome: '',
  razaoSocial: '',
  email: '',
  link: '',
  cep: '',
  rua: '',
  numero: '',
  complemento: '',
  cidade: '',
  uf: '',
};

const toDigits = (value) => String(value ?? '').replace(/\D/g, '');

const formatCepInput = (value) => {
  const digits = toDigits(value).slice(0, 8);

  if (digits.length <= 5) {
    return digits;
  }

  return `${digits.slice(0, 5)}-${digits.slice(5)}`;
};

const maskCpf = (value) => {
  const digits = toDigits(value);

  if (digits.length < 11) {
    return digits;
  }

  return `***.${digits.slice(3, 9)}-**`;
};

const normalizeLinkValue = (value) => {
  const trimmed = String(value ?? '').trim();

  if (!trimmed) {
    return '';
  }

  if (/^https?:\/\//i.test(trimmed)) {
    return trimmed.replace(/^http:\/\//i, 'https://');
  }

  return `https://${trimmed}`;
};

export default function ConfiguracoesScreen() {
  const router = useRouter();

  const [activeTab, setActiveTab] = useState(0);
  const [activeSubTab, setActiveSubTab] = useState(0);
  const [isEditMode, setIsEditMode] = useState(false);
  const [formData, setFormData] = useState(EMPTY_PROFILE);
  const [savedData, setSavedData] = useState(EMPTY_PROFILE);
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [sessionState, setSessionState] = useState({ token: null, ongId: '', name: '' });

  const updateField = useCallback((field, value) => {
    setFormData((prev) => {
      const nextValue =
        field === 'cep'
          ? formatCepInput(value)
          : field === 'uf'
            ? String(value ?? '').toUpperCase().slice(0, 2)
            : value;

      return {
        ...prev,
        [field]: nextValue,
      };
    });

    setErrors((prev) => ({
      ...prev,
      [field]: undefined,
    }));
  }, []);

  const loadProfile = useCallback(async () => {
    setIsLoading(true);

    try {
      const session = await getSession();
      const normalizedSession = {
        token: session?.token ?? null,
        ongId: String(session?.ongId ?? '').trim(),
        name: String(session?.name ?? '').trim(),
      };

      setSessionState(normalizedSession);

      const profile = await getOngProfile(normalizedSession.ongId, normalizedSession.name);
      const merged = {
        ...EMPTY_PROFILE,
        ...profile,
      };

      setFormData(merged);
      setSavedData(merged);
    } catch (error) {
      Alert.alert('Erro', normalizeBackendError(error));
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    loadProfile();
  }, [loadProfile]);

  const validateForm = useCallback(() => {
    const nextErrors = {};
    const normalizedLink = normalizeLinkValue(formData.link);

    if (String(formData.nome ?? '').trim().length < 3) {
      nextErrors.nome = 'Informe um nome valido com pelo menos 3 caracteres.';
    }

    if (!validateEmail(formData.email)) {
      nextErrors.email = 'Informe um email valido.';
    }

    if (!normalizedLink) {
      nextErrors.link = 'Informe um link de contato.';
    } else if (!validateURL(normalizedLink)) {
      nextErrors.link = 'O link deve iniciar com https://';
    }

    if (!validateCEP(formData.cep)) {
      nextErrors.cep = 'CEP invalido. Use o formato 00000-000.';
    }

    const ufValue = String(formData.uf ?? '').trim();
    if (ufValue.length > 0 && ufValue.length < 2) {
      nextErrors.uf = 'UF deve conter 2 letras.';
    }

    setErrors(nextErrors);
    return Object.keys(nextErrors).length === 0;
  }, [formData]);

  const handleEditPress = useCallback(() => {
    if (activeTab !== 0) {
      setActiveTab(0);
    }

    setIsEditMode(true);
  }, [activeTab]);

  const handleCancel = useCallback(() => {
    if (isSaving) {
      return;
    }

    setFormData(savedData);
    setErrors({});
    setIsEditMode(false);
  }, [isSaving, savedData]);

  const handleSave = useCallback(async () => {
    if (isSaving || isLoading) {
      return;
    }

    if (!validateForm()) {
      return;
    }

    try {
      setIsSaving(true);

      const normalizedLink = normalizeLinkValue(formData.link);
      const updated = await updateOngProfile(
        sessionState.ongId,
        {
          ...formData,
          link: normalizedLink,
        },
        sessionState.name
      );
      setFormData(updated);
      setSavedData(updated);
      setIsEditMode(false);

      await saveSession(sessionState.token, updated.nome, sessionState.ongId);
      setSessionState((prev) => ({
        ...prev,
        name: updated.nome,
      }));
    } catch (error) {
      Alert.alert('Erro', normalizeBackendError(error));
    } finally {
      setIsSaving(false);
    }
  }, [formData, isLoading, isSaving, sessionState, validateForm]);

  const handleLogout = useCallback(async () => {
    if (isSaving) {
      return;
    }

    try {
      await logout();
      router.replace('/');
    } catch (error) {
      Alert.alert('Erro', 'Nao foi possivel encerrar a sessao.');
    }
  }, [isSaving, router]);

  const handleMainTabPress = useCallback((index) => {
    setActiveTab(index);

    if (index !== 0) {
      setIsEditMode(false);
      setErrors({});
    }
  }, []);

  const handleSubTabPress = useCallback((index) => {
    setActiveSubTab(index);
  }, []);

  const personalFields = useMemo(
    () => [
      {
        label: 'Nome da ONG',
        value: formData.nome,
        onChangeText: (text) => updateField('nome', text),
        error: errors.nome,
      },
      {
        label: 'Email',
        value: formData.email,
        onChangeText: (text) => updateField('email', text),
        error: errors.email,
      },
      {
        label: 'CPF',
        value: maskCpf(formData.cpf),
        masked: true,
      },
      {
        label: 'Link de contato',
        value: formData.link,
        isLink: !isEditMode,
        onChangeText: isEditMode ? (text) => updateField('link', text) : undefined,
        error: errors.link,
      },
    ],
    [errors.email, errors.link, errors.nome, formData.cpf, formData.email, formData.link, formData.nome, isEditMode, updateField]
  );

  const addressFields = useMemo(
    () => [
      {
        label: 'CEP',
        value: formData.cep,
        onChangeText: (text) => updateField('cep', text),
        error: errors.cep,
      },
      {
        type: 'two-column',
        leftField: {
          label: 'Rua',
          value: formData.rua,
          onChangeText: (text) => updateField('rua', text),
          error: errors.rua,
        },
        rightField: {
          label: 'Numero',
          value: formData.numero,
          onChangeText: (text) => updateField('numero', text),
          error: errors.numero,
        },
      },
      {
        label: 'Complemento',
        value: formData.complemento,
        onChangeText: (text) => updateField('complemento', text),
        error: errors.complemento,
      },
      {
        type: 'two-column',
        leftField: {
          label: 'Cidade',
          value: formData.cidade,
          onChangeText: (text) => updateField('cidade', text),
          error: errors.cidade,
        },
        rightField: {
          label: 'UF',
          value: formData.uf,
          onChangeText: (text) => updateField('uf', text),
          error: errors.uf,
        },
      },
    ],
    [
      errors.cep,
      errors.cidade,
      errors.complemento,
      errors.numero,
      errors.rua,
      errors.uf,
      formData.cep,
      formData.cidade,
      formData.complemento,
      formData.numero,
      formData.rua,
      formData.uf,
      updateField,
    ]
  );

  const profileName = formData.nome || sessionState.name || 'ONG';

  if (isLoading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color={Colors.primary} />
      </View>
    );
  }

  return (
    <View style={styles.screen}>
      <ProfileHero orgName={profileName} isEditMode={isEditMode} onEditPress={handleEditPress} />

      <TabBar tabs={MAIN_TABS} activeIndex={activeTab} onTabPress={handleMainTabPress} />

      {activeTab === 0 ? (
        <View style={styles.contentWrapper}>
          <TabBar tabs={SUB_TABS} activeIndex={activeSubTab} onTabPress={handleSubTabPress} />
          <FormSection fields={activeSubTab === 0 ? personalFields : addressFields} editable={isEditMode} />
        </View>
      ) : (
        <View style={styles.placeholderContainer}>
          <AppText variant="fieldValue">Em breve</AppText>
        </View>
      )}

      <FooterActions
        mode={isEditMode ? 'edit' : 'view'}
        onLogout={handleLogout}
        onCancel={handleCancel}
        onSave={handleSave}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: Colors.primaryLight,
  },
  contentWrapper: {
    flex: 1,
  },
  loadingContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: Colors.primaryLight,
  },
  placeholderContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: Spacing.screenHorizontal,
    paddingBottom: Spacing.footerHeight,
  },
});
