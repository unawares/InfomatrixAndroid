package com.example.infomatrix.backend;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class ServiceBackend {

    private static ServiceBackend instance;

    private String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    private ServiceBackend() {

    }

    public static ServiceBackend getInstance() {
        if (instance == null) {
            instance = new ServiceBackend();
        }
        return instance;
    }

}
