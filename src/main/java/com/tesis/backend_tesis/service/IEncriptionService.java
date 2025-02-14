package com.tesis.backend_tesis.service;

public interface IEncriptionService {


 String encriptPass(String pass);

 public Boolean verificarEncriptedText(String passEncripted, String passReceived);

}
