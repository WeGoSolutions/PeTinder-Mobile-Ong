import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { ActivityIndicator, StyleSheet, View } from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import * as ImagePicker from 'expo-image-picker';
import { Colors, Spacing } from '../../theme';
import { getSession, saveSession } from '../../services/sessionService';
import { logout } from '../../services/logoutService';
import {
  getOngImage,
  getOngProfile,
  normalizeBackendError,
  updateOngImage,
  updateOngProfile,
} from '../../services/ongSettingsService';
import { validateCEP, validateEmail, validateURL } from '../../utils/validators';
import { resolveImageUri } from '../../utils/imageUri';
import AppText from '../../components/atoms/AppText';
import FooterActions from '../../components/molecules/FooterActions';
import FormSection from '../../components/molecules/FormSection';
import ProfileHero from '../../components/molecules/ProfileHero';
import TabBar from '../../components/molecules/TabBar';
import Toast from '../../components/Toast';
import GenericModal from '../../components/GenericModal';
import PillButton from '../../components/atoms/PillButton';
import SwipeBackGesture from '../../components/SwipeBackGesture';

const MAIN_TABS = ['Conta', 'Segurança'];
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

const getStringParam = (value) => {
  if (typeof value === 'string') {
    return value;
  }

  if (Array.isArray(value) && typeof value[0] === 'string') {
    return value[0];
  }

  return '';
};

export default function ConfiguracoesRoute() {
  const router = useRouter();
  const { from, backTo } = useLocalSearchParams();

  const [activeTab, setActiveTab] = useState(0);
  const [activeSubTab, setActiveSubTab] = useState(0);
  const [isEditMode, setIsEditMode] = useState(false);
  const [formData, setFormData] = useState(EMPTY_PROFILE);
  const [savedData, setSavedData] = useState(EMPTY_PROFILE);
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [profileImageUri, setProfileImageUri] = useState('');
  const [savedImageUri, setSavedImageUri] = useState('');
  const [pendingImageDataUrl, setPendingImageDataUrl] = useState('');
  const [sessionState, setSessionState] = useState({ token: null, ongId: '', name: '' });
  const [toast, setToast] = useState({ visible: false, title: '', message: '', type: 'info' });
  const [isLogoutModalVisible, setIsLogoutModalVisible] = useState(false);
  const toastTimeoutRef = useRef(null);

  const showToast = useCallback((title, message, type = 'info', duration = 2400) => {
    if (toastTimeoutRef.current) {
      clearTimeout(toastTimeoutRef.current);
    }

    setToast({ visible: true, title, message, type });

    toastTimeoutRef.current = setTimeout(() => {
      setToast((prev) => ({ ...prev, visible: false }));
      toastTimeoutRef.current = null;
    }, duration);
  }, []);

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

      const [profile, imageUrl] = await Promise.all([
        getOngProfile(normalizedSession.ongId, normalizedSession.name),
        getOngImage(normalizedSession.ongId).catch(() => null),
      ]);
      const merged = {
        ...EMPTY_PROFILE,
        ...profile,
      };

      const normalizedImageUri = resolveImageUri(imageUrl);

      setFormData(merged);
      setSavedData(merged);
      setProfileImageUri(normalizedImageUri);
      setSavedImageUri(normalizedImageUri);
      setPendingImageDataUrl('');
    } catch (error) {
      showToast('Erro', normalizeBackendError(error), 'error');
    } finally {
      setIsLoading(false);
    }
  }, [showToast]);

  useEffect(() => {
    loadProfile();
  }, [loadProfile]);

  useEffect(
    () => () => {
      if (toastTimeoutRef.current) {
        clearTimeout(toastTimeoutRef.current);
      }
    },
    []
  );

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

  const handleImagePress = useCallback(async () => {
    if (!isEditMode || isSaving || isLoading) {
      return;
    }

    try {
      const permission = await ImagePicker.requestMediaLibraryPermissionsAsync();

      if (!permission.granted) {
        showToast('Permissão necessária', 'Permita acesso à galeria para alterar a imagem da ONG.', 'warning');
        return;
      }

      const result = await ImagePicker.launchImageLibraryAsync({
        mediaTypes: ImagePicker.MediaTypeOptions.Images,
        allowsEditing: true,
        aspect: [1, 1],
        quality: 0.7,
        base64: true,
      });

      if (result.canceled || !result.assets?.length) {
        return;
      }

      const asset = result.assets[0];
      const imageBase64 = String(asset?.base64 ?? '').trim();

      if (!imageBase64) {
        showToast('Erro', 'Não foi possível processar a imagem selecionada.', 'error');
        return;
      }

      const mimeType = String(asset?.mimeType ?? '').trim() || 'image/jpeg';
      const imageDataUrl = `data:${mimeType};base64,${imageBase64}`;
      const previewUri = String(asset?.uri ?? '').trim() || imageDataUrl;

      setProfileImageUri(previewUri);
      setPendingImageDataUrl(imageDataUrl);
    } catch (error) {
      showToast('Erro', 'Não foi possível selecionar a imagem.', 'error');
    }
  }, [isEditMode, isLoading, isSaving, showToast]);

  const handleCancel = useCallback(() => {
    if (isSaving) {
      return;
    }

    setFormData(savedData);
    setProfileImageUri(savedImageUri);
    setPendingImageDataUrl('');
    setErrors({});
    setIsEditMode(false);
  }, [isSaving, savedData, savedImageUri]);

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

      let nextImageUri = savedImageUri;

      if (pendingImageDataUrl) {
        const uploadedImageUrl = await updateOngImage(sessionState.ongId, pendingImageDataUrl);
        nextImageUri = resolveImageUri(uploadedImageUrl) || pendingImageDataUrl;
      }

      setFormData(updated);
      setSavedData(updated);
      setProfileImageUri(nextImageUri);
      setSavedImageUri(nextImageUri);
      setPendingImageDataUrl('');
      setIsEditMode(false);

      await saveSession(sessionState.token, updated.nome, sessionState.ongId);
      setSessionState((prev) => ({
        ...prev,
        name: updated.nome,
      }));

      showToast('Sucesso', 'Dados salvos com sucesso.', 'success');
    } catch (error) {
      showToast('Erro', normalizeBackendError(error), 'error');
    } finally {
      setIsSaving(false);
    }
  }, [
    formData,
    isLoading,
    isSaving,
    pendingImageDataUrl,
    savedImageUri,
    sessionState,
    showToast,
    validateForm,
  ]);

  const handleRequestLogout = useCallback(() => {
    if (isSaving) {
      return;
    }

    setIsLogoutModalVisible(true);
  }, [isSaving]);

  const handleCancelLogout = useCallback(() => {
    setIsLogoutModalVisible(false);
  }, []);

  const handleConfirmLogout = useCallback(async () => {
    if (isSaving) {
      return;
    }

    setIsLogoutModalVisible(false);

    try {
      await logout();
      router.replace('/');
    } catch (error) {
      showToast('Erro', 'Não foi possível encerrar a sessão.', 'error');
    }
  }, [isSaving, router, showToast]);

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

  const handleGestureBack = useCallback(() => {
    const backToPath = getStringParam(backTo).trim();
    const fromPath = getStringParam(from).trim();

    if (backToPath.length > 0) {
      router.replace(backToPath);
      return;
    }

    if (fromPath.length > 0) {
      router.replace(fromPath);
      return;
    }

    if (typeof router.canGoBack === 'function' && router.canGoBack()) {
      router.back();
      return;
    }

    router.replace('/ong');
  }, [from, backTo, router]);

  if (isLoading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color={Colors.primary} />
      </View>
    );
  }

  return (
    <SwipeBackGesture onSwipeBack={handleGestureBack}>
      <View style={styles.screen}>
        <ProfileHero
          orgName={profileName}
          imageUri={profileImageUri}
          isEditMode={isEditMode}
          onEditPress={handleEditPress}
          onImagePress={handleImagePress}
          disableImagePress={isSaving}
        />

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
          onLogout={handleRequestLogout}
          onCancel={handleCancel}
          onSave={handleSave}
        />

        <GenericModal
          visible={isLogoutModalVisible}
          title="Sair da conta"
          onClose={handleCancelLogout}
        >
          <AppText variant="fieldValue">Deseja realmente encerrar sua sessão?</AppText>

          <View style={styles.logoutModalActions}>
            <View style={styles.logoutModalButtonColumn}>
              <PillButton label="Cancelar" variant="outline" onPress={handleCancelLogout} fullWidth />
            </View>
            <View style={styles.logoutModalButtonColumn}>
              <PillButton label="Sair" variant="destructive" onPress={handleConfirmLogout} fullWidth />
            </View>
          </View>
        </GenericModal>

        <Toast visible={toast.visible} title={toast.title} message={toast.message} type={toast.type} />
      </View>
    </SwipeBackGesture>
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
  logoutModalActions: {
    flexDirection: 'row',
    gap: 10,
    marginTop: 6,
  },
  logoutModalButtonColumn: {
    flex: 1,
  },
});
