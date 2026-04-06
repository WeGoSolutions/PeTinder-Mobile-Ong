import {
  View,
  StyleSheet,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  Alert,
  ActivityIndicator,
} from 'react-native';
import { useEffect, useState } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';
import LogoHome from '../components/LogoHome';
import DynamicInput from '../components/DynamicInput';
import DynamicButton from '../components/DynamicButton';
import { useRouter } from 'expo-router'
import { login } from '../services/loginService';
import { getSession, saveSession } from '../services/sessionService';

export default function Home() {
  const router = useRouter()

  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isBootstrapping, setIsBootstrapping] = useState(true);

  useEffect(() => {
    let isMounted = true;

    const bootstrapSession = async () => {
      try {
        const { token, name, ongId } = await getSession();
        const hasPersistedSession = Boolean((typeof token === 'string' && token.trim()) || (typeof name === 'string' && name.trim()) || (typeof ongId === 'string' && ongId.trim()));

        if (isMounted && hasPersistedSession) {
          router.replace('/ong');
          return;
        }
      } catch (error) {
        console.error('Erro ao inicializar sessão:', error);
      } finally {
        if (isMounted) {
          setIsBootstrapping(false);
        }
      }
    };

    bootstrapSession();

    return () => {
      isMounted = false;
    };
  }, [router]);

  const showInvalidDataAlert = () => {
    if (Platform.OS === 'web' && typeof window !== 'undefined' && window.alert) {
      window.alert('Dados inválidos');
      return;
    }

    Alert.alert('Erro de login', 'Dados inválidos');
  };

  const handleLogin = async () => {
    if (isLoading || isBootstrapping) {
      return;
    }

    if (!email || !senha) {
      if (Platform.OS === 'web' && typeof window !== 'undefined' && window.alert) {
        window.alert('Campos obrigatórios', 'Preencha email e senha.');
        return;
      }

      Alert.alert('Campos obrigatórios', 'Preencha email e senha.');
      return;
    }

    //PARA TESTE SEM BACKEND
    const useBackend = String(process.env.EXPO_PUBLIC_UTILIZAR_BACKEND ?? '').toLowerCase() === 'true';
    if (!useBackend) {
      await saveSession(null, email, '1');
      router.replace('/ong');
      return;
    }

    try {
      setIsLoading(true);
      const response = await login(email, senha);
      const payload = response?.data ?? response;
      const token = payload?.token ?? payload?.jwt ?? payload?.accessToken ?? null;
      const ongId = payload?.id ?? payload?.ongId ?? payload?.ong?.id ?? null;
      const nome = payload?.nome ?? payload?.name ?? payload?.ong?.nome ?? payload?.ong?.name ?? email;

      await saveSession(token, nome, ongId);

      router.replace('/ong');
    } catch (error) {
      showInvalidDataAlert();
    } finally {
      setIsLoading(false);
    }
  };


  if (isBootstrapping) {
    return (
      <SafeAreaView style={styles.safeArea} edges={['top', 'left', 'right']}>
        <View style={styles.bootstrapContainer}>
          <ActivityIndicator size="large" color="#80465D" />
        </View>
      </SafeAreaView>
    );
  }


  return (
    <SafeAreaView style={styles.safeArea} edges={['top', 'left', 'right']}>
      <KeyboardAvoidingView
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        style={styles.containerHome}
        keyboardVerticalOffset={Platform.OS === 'ios' ? 16 : 0}
      >
        <ScrollView
          contentContainerStyle={styles.content}
          keyboardShouldPersistTaps="handled"
          showsVerticalScrollIndicator={false}
        >
          <LogoHome />
          <View style={styles.container}>
            <View>
              <DynamicInput
                type="email"
                label="Email"
                placeholder="Email"
                autoCapitalize="none"
                autoCorrect={false}
                autoComplete="email"
                value={email}
                onChangeText={setEmail}
              />
              <DynamicInput
                type="password"
                label="Senha"
                placeholder="Senha"
                autoCapitalize="none"
                autoComplete="password"
                value={senha}
                onChangeText={setSenha}
              />
            </View>
            <View style={styles.buttonContainer}>
              <DynamicButton
                variant="secondary"
                onPress={handleLogin}
                isLoading={isLoading}
                disabled={isLoading}
                textStyle={{ color: '#80465D' }}
              >
                Entrar
              </DynamicButton>
              <View style={styles.bottomButtons}>
                <DynamicButton
                  variant="tertiary"
                  textStyle={{ color: '#80465D' }}
                  onPress={() => console.log('Forgot password')}
                >
                  Esqueceu a senha?
                </DynamicButton>
              </View>
            </View>
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#ffffff',
  },
  containerHome: {
    flex: 1,
    backgroundColor: '#ffffff',
  },
  content: {
    flexGrow: 1,
    justifyContent: 'center',
    paddingHorizontal: 16,
    paddingTop: 24,
    paddingBottom: 24,
  },
  container: {
    marginTop: Platform.OS === 'web' ? 20 : 12,
    width: '100%',
    maxWidth: 460,
    alignSelf: 'center',
    paddingHorizontal: 24,
    backgroundColor: '#fff',
    borderRadius: 16,
    paddingVertical: 20,
  },
  buttonContainer: {
    width: '100%',
    marginTop: 20,
    paddingBottom: 8,
    paddingHorizontal: 0,
  },
  bottomButtons: {
    flexDirection: 'row',
    justifyContent: 'center',
  },
  bootstrapContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  }
});
