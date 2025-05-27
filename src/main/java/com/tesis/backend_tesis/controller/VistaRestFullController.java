package com.tesis.backend_tesis.controller;

import com.tesis.backend_tesis.repository.modelo.*;
import com.tesis.backend_tesis.service.IVistasEntidadesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/vistas")
public class VistaRestFullController {


    @Autowired
    private IVistasEntidadesService vistasEntidadesService;

    private static final Logger logger = LogManager.getLogger(VistaRestFullController.class);


    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/estudiante/usuario/{idUsuario}")
    //@PreAuthorize("hasRole('estudiante')")
    public VistaEstudiante buscarEstudiantePorIdUsuario(@PathVariable Integer idUsuario) {
        return this.vistasEntidadesService.buscarEstudiantePorIdUsuario(idUsuario);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/estudiante/{idEstudiante}")
    public VistaEstudiante buscarEstudiantePorIdEstudiante(@PathVariable Integer idEstudiante) {
        return this.vistasEntidadesService.buscarEstudiantePorIdEstudiante(idEstudiante);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/estudiantes")
    public List<VistaEstudiante> buscarTodosEstudiantes() {
        return this.vistasEntidadesService.buscarTodosEstudiantes();
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/estudiantes/estado/{estado}")
    public List<VistaEstudiante> buscarEstudiantesPorEstadoActivacion(@PathVariable Boolean estado) {
        return this.vistasEntidadesService.buscarEstudiantesPorEstadoActivacion(estado);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/estudiantes/correo")
    public VistaEstudiante buscarEstudiantePorCorreo(@RequestParam("correo") String correo){
        return this.vistasEntidadesService.buscarPorCorreoEstudainte(correo);
    }

    //------------------------ Rutas para VistaDocente ------------------------

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/docente/usuario/{idUsuario}")
    public VistaDocente buscarDocentePorIdUsuario(@PathVariable Integer idUsuario) {
        return this.vistasEntidadesService.buscarDocentePorIdUsuario(idUsuario);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/docente/{idDocente}")
    public VistaDocente buscarDocentePorIdDocente(@PathVariable Integer idDocente) {
        return this.vistasEntidadesService.buscarDocentePorIdDocente(idDocente);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/docentes")
    public List<VistaDocente> buscarTodosDocentes() {
        return this.vistasEntidadesService.buscarTodosDocente();
    }

    @PreAuthorize("hasAnyRole('estudiante','docente', 'secretaria', 'direccion') ")
    @GetMapping("/docentes/estado/{activo}")
    public List<VistaDocente> buscarDocentesPorEstado(@PathVariable Boolean activo) {
        return this.vistasEntidadesService.buscarDocentesPorEstado(activo);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/docentes/facultad")
    public List<VistaDocente> buscarDocentesPorFacultad(@RequestParam("nombreFacultad") String nombreFacultad) {
        return this.vistasEntidadesService.buscarDocentesPorFacultad(nombreFacultad);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/docentes/correo")
    public VistaDocente buscarDocentePorCorreo(@RequestParam("correo") String correoDocente){
        return this.vistasEntidadesService.buscarPorCorreoDocente(correoDocente);
    }

    //------------------------ Rutas para VistaSecretaria ------------------------

    @PreAuthorize("hasAnyRole('secretaria', 'direccion') ")
    @GetMapping("/secretaria/usuario/{idUsuario}")
    public VistaSecretaria buscarSecretariaPorIdUsuario(@PathVariable Integer idUsuario) {
        return this.vistasEntidadesService.buscarSecretariaPorIdUsuario(idUsuario);
    }

    @PreAuthorize("hasAnyRole('secretaria', 'direccion') ")
    @GetMapping("/secretaria/{idSecretaria}")
    public VistaSecretaria buscarSecretariaPorIdSecretaria(@PathVariable Integer idSecretaria) {
        return this.vistasEntidadesService.buscarSecretariaPorIdSecretaria(idSecretaria);
    }

    @PreAuthorize("hasAnyRole('secretaria', 'direccion') ")
    @GetMapping("/secretarias")
    public List<VistaSecretaria> buscarTodosSecretarias() {
        return this.vistasEntidadesService.buscarTodosSecretarias();
    }

    @PreAuthorize("hasAnyRole('secretaria', 'direccion') ")
    @GetMapping("/secretarias/estado/{activo}")
    public List<VistaSecretaria> buscarSecretariasPorEstado(@PathVariable Boolean activo) {
        return this.vistasEntidadesService.buscarSecretariasPorEstado(activo);
    }

    //------------------------ Rutas para VistaCarrera ------------------------

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/carrera/{idCarrera}")
    public VistaCarrera buscarCarreraPorIdCarrera(@PathVariable Integer idCarrera) {
        return this.vistasEntidadesService.buscarCarreraPorIdCarrera(idCarrera);
    }


    @GetMapping("/facultad/{idFacultad}/carreras")
    public List<VistaCarrera> buscarCarreraIdFacultad(@PathVariable Integer idFacultad) {
        return this.vistasEntidadesService.buscarCarreraIdFacultad(idFacultad);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/carreras")
    public List<VistaCarrera> buscarTodasCarreras() {
        return this.vistasEntidadesService.buscarTodasCarreras();
    }
    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/carrera")
    public VistaCarrera buscarCarreraPorNombreCarrera(@RequestParam ("nombreCarrera") String nombreCarrera) {
        return this.vistasEntidadesService.buscarCarreraPorNombreCarrera(nombreCarrera);
    }

    //------------------------ Rutas para VistaUsuarioRol ------------------------

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion', 'ADMIN') ")
    @GetMapping("/usuarioRol/{idUsuarioRol}")
    public VistaUsuarioRol buscarUsuarioRolPorIdUsuarioRol(@PathVariable Integer idUsuarioRol) {
        return this.vistasEntidadesService.buscarUsuarioRolPorIdUsuarioRol(idUsuarioRol);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/usuarioRol/correo")
    public VistaUsuarioRol buscarUsuarioRolPorCorreo(@RequestParam("correo") String correo) {
        return this.vistasEntidadesService.buscarUsuarioRolPorCorreo(correo);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion', 'ADMIN') ")
    @GetMapping("/usuarioRol/rol/{idRol}")
    public List<VistaUsuarioRol> buscarUsuarioRolPorIdRol(@PathVariable Integer idRol) {
        return this.vistasEntidadesService.buscarUsuarioRolPorIdRol(idRol);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion', 'ADMIN') ")
    @GetMapping("/usuarioRol/rol/nombre/{nombreRol}")
    public List<VistaUsuarioRol> buscarUsuarioRolPorNombreRol(@PathVariable String nombreRol) {
        return this.vistasEntidadesService.buscarUsuarioRolPorNombreRol(nombreRol);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/usuarioRol/usuario/{idUsuario}")
    public List<VistaUsuarioRol> buscarUsuarioRolPorIdUsuario(@PathVariable Integer idUsuario) {
        return this.vistasEntidadesService.buscarUsuarioRolPorIdUsuario(idUsuario);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/usuarioRol/apellidos")
    public List<VistaUsuarioRol> buscarUsuarioRolPorApellidos(@RequestParam("apellidos") String apellidos) {
        return this.vistasEntidadesService.buscarUsuarioRolPorApellidos(apellidos);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/usuarioRol/nombres")
    public List<VistaUsuarioRol> buscarUsuarioRolPorNombres(@RequestParam("nombres")  String nombres) {
        return this.vistasEntidadesService.buscarUsuarioRolPorNombres(nombres);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/usuariosRoles")
    public List<VistaUsuarioRol> buscarTodosUsariosRoles() {
        return this.vistasEntidadesService.buscarTodosUsuarioRol();
    }

    //@PreAuthorize("hasAnyRole('docente', 'secretaria', 'dirección', 'ADMIN') ")
    @GetMapping("/usuarioRol/rol/nombre/{nombreRol}/estado/{estado}")
    public List<VistaUsuarioRol> buscarUsuarioRolPorNombreRolyEstado(@PathVariable String nombreRol,@PathVariable Boolean estado) {
        return this.vistasEntidadesService.buscarUsuarioRolPorNombreRolYEstado(nombreRol, estado);
    }

//------------------- Vista Propuestas-----------------------//


    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion', 'estudiante') ")
    @GetMapping("/propuestas/{idPropuesta}")
    public VistaPropuesta buscarPropuestaPorIdPropuesta(@PathVariable Integer idPropuesta) {
        return this.vistasEntidadesService.buscarPropuestaPorIdPropuesta(idPropuesta).getFirst();
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/propuestas/carrera")
    public List<VistaPropuesta> buscarPropuestaPorCarrera(@RequestParam("carrera")  String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorCarrera(carrera);
    }


    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/propuestas/validacion/{numero}")
    public List<VistaPropuesta> buscarPropuestaPorEstadoValidacion(@PathVariable Integer numero,
                                                                   @RequestParam("carrera") String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorEstadoValidacion(numero, carrera);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/propuestas/aprobacion/{numero}")
    public List<VistaPropuesta> buscarPropuestaPorEstadoAprobacion(@PathVariable Integer numero,
                                                                   @RequestParam("carrera") String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorEstadoAprobacion(numero, carrera);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/propuestas/periodo/{valor}")
    public List<VistaPropuesta> buscarPropuestaPorPeriodo(@PathVariable String valor,
                                                          @RequestParam("carrera") String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorPeriodo(valor, carrera);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/propuestas/tipo")
    public List<VistaPropuesta> buscarPropuestaPorTipo(@RequestParam("nombre") String nombre,
                                                       @RequestParam("carrera") String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorTipo(nombre, carrera);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/propuestas/por-categoria")
    public List<VistaPropuesta> buscarPropuestaPorCategoria(@RequestParam("categoria") String categoria,
                                                            @RequestParam("carrera") String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorCategoria(categoria, carrera);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/propuestas/tipo-categoria")
    public List<VistaPropuesta> buscarPropuestaPorCategoriaTipo(@RequestParam("tipo") String tipo,
                                                            @RequestParam("categoria") String categoria,
                                                            @RequestParam("carrera") String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorTipoCategoria(tipo,categoria, carrera);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/propuestas/tema/{nombre}")
    public List<VistaPropuesta> buscarPropuestaPorTema(@PathVariable String nombre,
                                                       @RequestParam("carrera") String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorTema(nombre, carrera);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/propuestas/tutor/usuario/{idUsuario}")
    public List<VistaPropuesta> buscarPropuestaPorTutor(@PathVariable Integer idUsuario,
                                                       @RequestParam("facultad") String facultad ) {
        return this.vistasEntidadesService.buscarPropuestaPorTutor(idUsuario, facultad);
    }

    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion') ")
    @GetMapping("/propuestas/revisor/usuario/{idUsuario}")
    public List<VistaPropuesta> buscarPropuestaPorRevisor(@PathVariable Integer idUsuario,
                                                        @RequestParam("facultad") String facultad ) {
        return this.vistasEntidadesService.buscarPropuestaPorRevisor(idUsuario, facultad);
    }


    @PreAuthorize("hasAnyRole('docente', 'estudiante', 'secretaria', 'direccion') ")
    @GetMapping("/propuestas/estudiante/usuario/{idUsuario}")
    public List<VistaPropuesta> buscarPropuestaPorEstudiante(@PathVariable("idUsuario") Integer idUsuario) {
        return this.vistasEntidadesService.buscarPropuestaPorEstudiante(idUsuario);
    }

}
