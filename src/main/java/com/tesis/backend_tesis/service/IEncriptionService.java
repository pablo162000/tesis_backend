package com.tesis.backend_tesis.service;

public interface IEncriptionService {


 public String encriptPass(String pass);

 public Boolean verificarEncriptedText(String passEncripted, String passReceived);

}
