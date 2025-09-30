package com.example.bankcards.util;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CardNumberEncryptionService {
    private static final String AES = "AES";
    private static final String AES_GCM_NOPADDING = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128; // bits
    private static final int IV_LENGTH_BYTES = 12; // recommended 12 for GCM

    private final SecretKey key;
    private final SecureRandom secureRandom = new SecureRandom();

    public CardNumberEncryptionService() throws Exception {
        byte[] rawKey = generateRandomAesKey(256);
        this.key = new SecretKeySpec(rawKey, AES);
    }

    // Генерация ключа
    public static byte[] generateRandomAesKey(int bits) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(AES);
        kg.init(bits);
        SecretKey sk = kg.generateKey();
        return sk.getEncoded();
    }

    public String encrypt(String plaintext) throws Exception {
        byte[] iv = new byte[IV_LENGTH_BYTES];
        secureRandom.nextBytes(iv);

        Cipher cipher = Cipher.getInstance(AES_GCM_NOPADDING);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));

        // Сохраняем: IV || ciphertext
        byte[] combined = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public String decrypt(String base64IvAndCiphertext) throws Exception {
        byte[] combined = Base64.getDecoder().decode(base64IvAndCiphertext);

        byte[] iv = new byte[IV_LENGTH_BYTES];
        System.arraycopy(combined, 0, iv, 0, IV_LENGTH_BYTES);
        int ctLength = combined.length - IV_LENGTH_BYTES;
        byte[] ciphertext = new byte[ctLength];
        System.arraycopy(combined, IV_LENGTH_BYTES, ciphertext, 0, ctLength);

        Cipher cipher = Cipher.getInstance(AES_GCM_NOPADDING);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] plain = cipher.doFinal(ciphertext);
        return new String(plain, "UTF-8");
    }
}
