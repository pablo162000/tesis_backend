package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.IEstudiantesRepository;
import com.tesis.backend_tesis.repository.IUsuariosRepository;
import com.tesis.backend_tesis.repository.modelo.Estudiantes;
import com.tesis.backend_tesis.repository.modelo.RegistroRequest;
import com.tesis.backend_tesis.repository.modelo.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private IEstudiantesRepository estudiantesRepository;
    @Autowired
    private IUsuariosRepository usuariosRepository;

    @Autowired
    private IEncriptionService encriptionService;

    @Override
    public Integer registroEstudiante(RegistroRequest registroRequest) {
        var flag = 0;
        if (registroRequest.getCorreo() != null && !registroRequest.getCorreo().isEmpty()
                && registroRequest.getPassword() != null && !registroRequest.getPassword().isEmpty()) {
            if (!this.usuariosRepository.existeUsuarioConEmail(registroRequest.getCorreo())) {
                try {

                    Usuarios usua = Usuarios.builder()
                            .username(registroRequest.getPrimer_nombre() + ' ' + registroRequest.getPrimer_apellido())
                            .correo(registroRequest.getCorreo())
                            .password(this.encriptionService.encriptPass(registroRequest.getPassword()))
                            .usua_rol(registroRequest.getRol())
                            .fechaCreacion(registroRequest.getFecha_registro())
                            .activo(registroRequest.getActivo())
                            .build();
                    Usuarios usuarioGuardado = this.usuariosRepository.insertar(usua);

                    if (usuarioGuardado == null || usuarioGuardado.getId() == null) {
                        throw new RuntimeException("Error al guardar el usuario");
                    }

                    Estudiantes estu = Estudiantes.builder()
                            .primer_nombre(registroRequest.getPrimer_nombre())
                            .segundo_nombre(registroRequest.getSegundo_nombre())
                            .primer_apellido(registroRequest.getPrimer_apellido())
                            .segundo_apellido(registroRequest.getSegundo_apellido())
                            .cedula(registroRequest.getCedula())
                            .activo(registroRequest.getActivo())
                            .usuario(usuarioGuardado)  // ASIGNAR ID DEL USUARIO
                            .build();


                    this.estudiantesRepository.insertar(estu);
                    flag = usua.getId();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return flag;
    }
}
