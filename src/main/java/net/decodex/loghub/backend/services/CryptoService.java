package net.decodex.loghub.backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import java.util.Arrays;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoService {
    @Value("${jwt.key}")
    String jwtKey;

    private final TextEncryptor textEncryptor = Encryptors.text("3ECBE52A5277FBB88323DA84899F7", "55FB46463D8A");

    public String encryptString(String stringToEncrypt) {
        var result = textEncryptor.encrypt(stringToEncrypt);
        log.info("Encripted To: "+result);
        return  result;
    }

    public String decryptString(String stringToEncrypt) {
        log.info("Decrypting: " + stringToEncrypt);
        return textEncryptor.decrypt(stringToEncrypt);
    }

    public String encryptStringToBase64(String stringToEncrypt) {
        log.info("Encrypting: "+stringToEncrypt);
        return Base64.getEncoder().encodeToString(encryptString(stringToEncrypt).getBytes());
    }

    public String decryptStringFromBase64(String base64EncriptedString) {
        return decryptString(new String(Base64.getDecoder().decode(base64EncriptedString)));
    }
}
