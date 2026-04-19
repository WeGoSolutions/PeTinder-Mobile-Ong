package cruds.common.cryptography;

import cruds.Users.V2.core.adapter.CriptografiaImagemGateway;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

@Component
public class AesCriptografiaAdapter implements CriptografiaImagemGateway {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;
    private static final byte[] KEY = "1234567890123456".getBytes();

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public byte[] criptografarImagem(byte[] dados) {
        if (dados == null || dados.length == 0) {
            throw new IllegalArgumentException("Imagem não pode ser nula ou vazia ao criptografar");
        }
        try {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec keySpec = new SecretKeySpec(KEY, ALGORITHM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            byte[] cipherText = cipher.doFinal(dados);

            // junta IV + cipherText para poder descriptografar depois
            byte[] resultado = new byte[IV_LENGTH + cipherText.length];
            System.arraycopy(iv, 0, resultado, 0, IV_LENGTH);
            System.arraycopy(cipherText, 0, resultado, IV_LENGTH, cipherText.length);

            return resultado;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar imagem", e);
        }
    }

    @Override
    public byte[] descriptografarImagem(byte[] dadosCriptografados) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(dadosCriptografados, 0, iv, 0, IV_LENGTH);

            byte[] cipherText = new byte[dadosCriptografados.length - IV_LENGTH];
            System.arraycopy(dadosCriptografados, IV_LENGTH, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec keySpec = new SecretKeySpec(KEY, ALGORITHM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

            return cipher.doFinal(cipherText);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar imagem", e);
        }
    }
}