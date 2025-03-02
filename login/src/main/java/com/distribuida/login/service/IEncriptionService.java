package com.distribuida.login.service;

public interface IEncriptionService {

    public String encriptPass(String pass);

    public Boolean verificarEncriptedText(String passEncripted, String passReceived);

}
