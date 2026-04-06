import { useState } from 'react';
import { View, Text, StyleSheet, Alert } from 'react-native';
import { useRouter } from 'expo-router';
import DynamicButton from '../../components/DynamicButton';
import { logout } from '../../services/logoutService';

export default function Configuracoes() {
  const router = useRouter();
  const [isLoggingOut, setIsLoggingOut] = useState(false);

  const handleLogout = async () => {
    if (isLoggingOut) {
      return;
    }

    try {
      setIsLoggingOut(true);
      await logout();
      router.replace('/');
    } catch (error) {
      Alert.alert('Erro', 'Nao foi possivel encerrar a sessao.');
    } finally {
      setIsLoggingOut(false);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Configurações</Text>
      <Text style={styles.subtitle}>Ajustes da conta da ONG</Text>
      <View style={styles.buttonWrapper}>
        <DynamicButton
          variant="secondary"
          onPress={handleLogout}
          isLoading={isLoggingOut}
          disabled={isLoggingOut}
        >
          Sair da sessão
        </DynamicButton>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
    padding: 24,
  },
  title: {
    fontSize: 24,
    fontWeight: '700',
    color: '#1A1A1A',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 14,
    color: '#6E6E6E',
  },
  buttonWrapper: {
    width: '100%',
    maxWidth: 320,
    marginTop: 24,
  },
});
