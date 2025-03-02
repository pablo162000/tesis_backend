package com.distribuida.alumno.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "estudianteClient", url = "http://localhost:4040/estudoiante/authors")
public interface DocenteRestClient {


}
