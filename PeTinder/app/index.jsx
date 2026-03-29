import {
  View,
  StyleSheet,
  KeyboardAvoidingView,
  Platform,
  ScrollView
} from 'react-native';
import { useState } from 'react';
import LogoHome from '../components/LogoHome';
import DynamicInput from '../components/DynamicInput';
import DynamicButton from '../components/DynamicButton';
import { useRouter } from 'expo-router'

export default function Home() {
  const router = useRouter()

  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');



  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      style={styles.containerHome}
      keyboardVerticalOffset={Platform.OS === 'ios' ? 24 : 0}
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
              onPress={() => router.push('/ong')}
            >
              Entrar
            </DynamicButton>
            <View style={styles.bottomButtons}>
              <DynamicButton
                variant="forgotPassword"
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
  );
}

const styles = StyleSheet.create({
  containerHome: {
    flex: 1,
    backgroundColor: '#ffffff',
  },
  content: {
    flexGrow: 1,
    justifyContent: 'center',
    paddingHorizontal: 16,
    paddingVertical: 24,
  },
  container: {
    marginTop: '30%',
    width: '100%',
    alignSelf: 'center',
    paddingHorizontal: 24,
    zIndex: 10,
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
