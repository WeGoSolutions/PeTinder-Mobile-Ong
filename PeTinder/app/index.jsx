import {
  View,
  StyleSheet,
  KeyboardAvoidingView,
  Platform
} from 'react-native';
import { useState } from 'react';
import LogoHome from '../components/LogoHome';
import DynamicInput from '../components/EmailInput';
import DynamicButton from '../components/DynamicButton';

export default function Home() {
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');

  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      style={styles.containerHome}
    >
      <LogoHome />
      <View style={styles.container}>
        <View>
          <DynamicInput
            placeholder="Email"
            keyboardType="email-address"
            autoCapitalize="none"
            autoCorrect={false}
            autoComplete="email"
            value={email}
            onChangeText={setEmail}
          />
          <DynamicInput
            placeholder="Senha"
            autoCapitalize="none"
            secureTextEntry={true}
            autoComplete="password"
            value={senha}
            onChangeText={setSenha}
          />
        </View>
        <View style={styles.buttonContainer}>
          <DynamicButton
            variant="secondary"
            onPress={() => console.log('Sign up')}
          >
            Criar Conta
          </DynamicButton>
          <DynamicButton
            variant="forgotPasswordButton"
            onPress={() => console.log('Forgot password')}
          >
            Esqueceu a senha?
          </DynamicButton>
        </View>
      </View>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  containerHome: {
    flex: 1,
    backgroundColor: '#fff',
  },
  container: {
    flex: 1,
    justifyContent: 'flex-end',
    paddingHorizontal: 24,
    zIndex: 10,
  },
  buttonContainer: {
    width: '100%',
    paddingBottom: 32,
    paddingHorizontal: 0,
  },
});
