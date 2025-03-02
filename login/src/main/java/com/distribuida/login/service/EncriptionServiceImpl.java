package com.distribuida.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncriptionServiceImpl implements IEncriptionService {

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public EncriptionServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encriptPass(String pass) {

        return passwordEncoder.encode(pass);
    }

    @Override
    public Boolean verificarEncriptedText(String passEncripted, String passReceived) {

        return passwordEncoder.matches(passReceived,passEncripted);
    }

}
