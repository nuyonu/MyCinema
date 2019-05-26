package app.controller.services;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Service;

@Service
public class CryptoService implements ICryptoService {

    public CryptoService() {
        encryptor.setPassword(pas);
        encryptor.setAlgorithm(alg);
    }

    @Override
    public String encrypt(String data) {
        return encryptor.encrypt(data);
    }

    @Override
    public String decrypt(String data) {
        return encryptor.decrypt(data);
    }

    private StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    private String pas="ZhG5Abs1Z7Cgwnz8etJB";
    private String alg="PBEWithMD5AndTripleDES";
}
