package com.tesis.backend_tesis.service;

import java.io.File;
import java.io.IOException;


public interface IFileService {

    public File downFileToTemp(String nombre) throws IOException;


}



