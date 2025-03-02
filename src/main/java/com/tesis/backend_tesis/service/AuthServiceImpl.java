package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.IEstudiantesRepository;
import com.tesis.backend_tesis.repository.IUsuariosRepository;
import com.tesis.backend_tesis.repository.modelo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
                            .fechaCreacion(LocalDateTime.now())//cambie del original
                            .rol("estudiante")
                            .activo(false)
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

    public AuthResponse loginUsuario(LoginRequest loginRequest) {
        Usuarios usua = this.usuariosRepository.buscarPorEmail(loginRequest.getCorreo());

        // Validar si el usuario existe
        if (usua == null) {
            throw new RuntimeException("El usuario no está registrado.");
        }

        // Verificar la contraseña encriptada
        if (!this.encriptionService.verificarEncriptedText(usua.getPassword(), loginRequest.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas.");
        }

        // Determinar el rol y obtener la información correspondiente
        String rol = usua.getRol();
        if ("estudiante".equals(rol)) {
            Estudiantes estu = this.estudiantesRepository.findByIdUsuario(usua.getId());

            return AuthResponse.builder()
                    .id(estu.getId())
                    .primer_nombre(estu.getPrimer_nombre())
                    .segundo_nombre(estu.getSegundo_nombre())
                    .primer_apellido(estu.getPrimer_apellido())
                    .segundo_apellido(estu.getSegundo_apellido())
                    .rol("estudiante")
                    .activo(true)
                    .build();

        } else if ("docente".equals(rol)) {
            /*
            Docentes doc = this.docentesRepository.findByIdUsuario(usua.getId());

            return AuthResponse.builder()
                    .id(doc.getId())
                    .primer_nombre(doc.getPrimer_nombre())
                    .segundo_nombre(doc.getSegundo_nombre())
                    .primer_apellido(doc.getPrimer_apellido())
                    .segundo_apellido(doc.getSegundo_apellido())
                    .rol("docente")
                    .activo(true)
                    .build();

            */

        } else if ("administrativo".equals(rol)) {

            /*
            Administrativos admin = this.administrativosRepository.findByIdUsuario(usua.getId());

            return AuthResponse.builder()
                    .id(admin.getId())
                    .primer_nombre(admin.getPrimer_nombre())
                    .segundo_nombre(admin.getSegundo_nombre())
                    .primer_apellido(admin.getPrimer_apellido())
                    .segundo_apellido(admin.getSegundo_apellido())
                    .rol("administrativo")
                    .activo(true)
                    .build();

             */
        }

        // Si el rol no es reconocido, lanzar excepción
        throw new RuntimeException("Rol de usuario desconocido.");
    }

}