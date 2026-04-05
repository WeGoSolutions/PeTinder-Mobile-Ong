import {
  View,
  StyleSheet,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  Alert
} from 'react-native';
import { useState } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';
import LogoHome from '../components/LogoHome';
import DynamicInput from '../components/DynamicInput';
import DynamicButton from '../components/DynamicButton';
import { useRouter } from 'expo-router'
import { login } from '../services/loginService';
import { saveSession } from '../services/sessionService';

export default function Home() {
  const router = useRouter()

  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const showInvalidDataAlert = () => {
    if (Platform.OS === 'web' && typeof window !== 'undefined' && window.alert) {
      window.alert('Dados inválidos');
      return;
    }

    Alert.alert('Erro de login', 'Dados inválidos');
  };

  const handleLogin = async () => {
    if (isLoading) {
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
      await saveSession(null, email);
      router.push('/ong');
      return;
    }

    try {
      setIsLoading(true);
      const response = await login(email, senha);
      const token = response?.token ?? response?.jwt ?? null; //arrumar isso, adicionar o jwt correto
      const nome = response?.nome;

      if (nome) {
        await saveSession(token, nome);
      }

      router.push('/ong');
    } catch (error) {
      showInvalidDataAlert();
    } finally {
      setIsLoading(false);
    }
  };



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
  }
});
