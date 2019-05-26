package app.controller.services;

public interface ICryptoService {
    String encrypt(String data);
    String decrypt(String data);
}