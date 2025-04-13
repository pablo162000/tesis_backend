package com.tesis.backend_tesis.controller;

import com.tesis.backend_tesis.repository.modelo.*;
import com.tesis.backend_tesis.service.IVistasEntidadesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/vistas")
public class VistaRestFullController {


    @Autowired
    private IVistasEntidadesService vistasEntidadesService;

    private static final Logger logger = LogManager.getLogger(VistaRestFullController.class);


    @GetMapping("/estudiante/usuario/{idUsuario}")
    public VistaEstudiante buscarEstudiantePorIdUsuario(@PathVariable Integer idUsuario) {
        return this.vistasEntidadesService.buscarEstudiantePorIdUsuario(idUsuario);
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public VistaEstudiante buscarEstudiantePorIdEstudiante(@PathVariable Integer idEstudiante) {
        return this.vistasEntidadesService.buscarEstudiantePorIdEstudiante(idEstudiante);
    }

    @GetMapping("/estudiantes")
    public List<VistaEstudiante> buscarTodosEstudiantes() {
        return this.vistasEntidadesService.buscarTodosEstudiantes();
    }

    @GetMapping("/estudiantes/estado/{estado}")
    public List<VistaEstudiante> buscarEstudiantesPorEstadoActivacion(@PathVariable Boolean estado) {
        return this.vistasEntidadesService.buscarEstudiantesPorEstadoActivacion(estado);
    }

    @GetMapping("/estudiantes/correo")
    public VistaEstudiante buscarEstudiantePorCorreo(@RequestParam("correo") String correo){
        return this.vistasEntidadesService.buscarPorCorreoEstudainte(correo);
    }

    //------------------------ Rutas para VistaDocente ------------------------

    @GetMapping("/docente/usuario/{idUsuario}")
    public VistaDocente buscarDocentePorIdUsuario(@PathVariable Integer idUsuario) {
        return this.vistasEntidadesService.buscarDocentePorIdUsuario(idUsuario);
    }

    @GetMapping("/docente/{idDocente}")
    public VistaDocente buscarDocentePorIdDocente(@PathVariable Integer idDocente) {
        return this.vistasEntidadesService.buscarDocentePorIdDocente(idDocente);
    }

    @GetMapping("/docentes")
    public List<VistaDocente> buscarTodosDocentes() {
        return this.vistasEntidadesService.buscarTodosDocente();
    }

    @GetMapping("/docentes/estado/{activo}")
    public List<VistaDocente> buscarDocentesPorEstado(@PathVariable Boolean activo) {
        return this.vistasEntidadesService.buscarDocentesPorEstado(activo);
    }

    @GetMapping("/docentes/facultad")
    public List<VistaDocente> buscarDocentesPorFacultad(@RequestParam("nombreFacultad") String nombreFacultad) {
        return this.vistasEntidadesService.buscarDocentesPorFacultad(nombreFacultad);
    }
    @GetMapping("/docentes/correo")
    public VistaDocente buscarDocentePorCorreo(@RequestParam("correo") String correoDocente){
        return this.vistasEntidadesService.buscarPorCorreoDocente(correoDocente);
    }

    //------------------------ Rutas para VistaSecretaria ------------------------

    @GetMapping("/secretaria/usuario/{idUsuario}")
    public VistaSecretaria buscarSecretariaPorIdUsuario(@PathVariable Integer idUsuario) {
        return this.vistasEntidadesService.buscarSecretariaPorIdUsuario(idUsuario);
    }

    @GetMapping("/secretaria/{idSecretaria}")
    public VistaSecretaria buscarSecretariaPorIdSecretaria(@PathVariable Integer idSecretaria) {
        return this.vistasEntidadesService.buscarSecretariaPorIdSecretaria(idSecretaria);
    }

    @GetMapping("/secretarias")
    public List<VistaSecretaria> buscarTodosSecretarias() {
        return this.vistasEntidadesService.buscarTodosSecretarias();
    }

    @GetMapping("/secretarias/estado/{activo}")
    public List<VistaSecretaria> buscarSecretariasPorEstado(@PathVariable Boolean activo) {
        return this.vistasEntidadesService.buscarSecretariasPorEstado(activo);
    }

    //------------------------ Rutas para VistaCarrera ------------------------

    @GetMapping("/carrera/{idCarrera}")
    public VistaCarrera buscarCarreraPorIdCarrera(@PathVariable Integer idCarrera) {
        return this.vistasEntidadesService.buscarCarreraPorIdCarrera(idCarrera);
    }

    @GetMapping("/facultad/{idFacultad}/carreras")
    public List<VistaCarrera> buscarCarreraIdFacultad(@PathVariable Integer idFacultad) {
        return this.vistasEntidadesService.buscarCarreraIdFacultad(idFacultad);
    }

    @GetMapping("/carreras")
    public List<VistaCarrera> buscarTodasCarreras() {
        return this.vistasEntidadesService.buscarTodasCarreras();
    }

    @GetMapping("/carrera")
    public VistaCarrera buscarCarreraPorNombreCarrera(@RequestParam ("nombreCarrera") String nombreCarrera) {
        return this.vistasEntidadesService.buscarCarreraPorNombreCarrera(nombreCarrera);
    }

    //------------------------ Rutas para VistaUsuarioRol ------------------------

    @GetMapping("/usuarioRol/{idUsuarioRol}")
    public VistaUsuarioRol buscarUsuarioRolPorIdUsuarioRol(@PathVariable Integer idUsuarioRol) {
        return this.vistasEntidadesService.buscarUsuarioRolPorIdUsuarioRol(idUsuarioRol);
    }

    @GetMapping("/usuarioRol/correo")
    public VistaUsuarioRol buscarUsuarioRolPorCorreo(@RequestParam("correo") String correo) {
        return this.vistasEntidadesService.buscarUsuarioRolPorCorreo(correo);
    }

    @GetMapping("/usuarioRol/rol/{idRol}")
    public List<VistaUsuarioRol> buscarUsuarioRolPorIdRol(@PathVariable Integer idRol) {
        return this.vistasEntidadesService.buscarUsuarioRolPorIdRol(idRol);
    }

    @GetMapping("/usuarioRol/rol/nombre/{nombreRol}")
    public List<VistaUsuarioRol> buscarUsuarioRolPorNombreRol(@PathVariable String nombreRol) {
        return this.vistasEntidadesService.buscarUsuarioRolPorNombreRol(nombreRol);
    }

    @GetMapping("/usuarioRol/usuario/{idUsuario}")
    public List<VistaUsuarioRol> buscarUsuarioRolPorIdUsuario(@PathVariable Integer idUsuario) {
        return this.vistasEntidadesService.buscarUsuarioRolPorIdUsuario(idUsuario);
    }

    @GetMapping("/usuarioRol/apellidos")
    public List<VistaUsuarioRol> buscarUsuarioRolPorApellidos(@RequestParam("apellidos") String apellidos) {
        return this.vistasEntidadesService.buscarUsuarioRolPorApellidos(apellidos);
    }

    @GetMapping("/usuarioRol/nombres")
    public List<VistaUsuarioRol> buscarUsuarioRolPorNombres(@RequestParam("nombres")  String nombres) {
        return this.vistasEntidadesService.buscarUsuarioRolPorNombres(nombres);
    }

    @GetMapping("/usuariosRoles")
    public List<VistaUsuarioRol> buscarTodosUsariosRoles() {
        return this.vistasEntidadesService.buscarTodosUsuarioRol();
    }


    @GetMapping("propuestas/{idPropuesta}")
    public VistaPropuesta buscarPropuestaPorIdPropuesta(@PathVariable Integer idPropuesta) {
        return this.vistasEntidadesService.buscarPropuestaPorIdPropuesta(idPropuesta).getFirst();
    }

    @GetMapping("propuestas/carrera")
    public List<VistaPropuesta> buscarPropuestaPorCarrera(@RequestParam("carrera")  String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorCarrera(carrera);
    }

    @GetMapping("propuestas/validacion/{numero}")
    public List<VistaPropuesta> buscarPropuestaPorEstadoValidacion(@PathVariable Integer numero,
                                                                   @RequestParam("carrera") String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorEstadoValidacion(numero, carrera);
    }

    @GetMapping("propuesta/aprobacion/{numero}")
    public List<VistaPropuesta> buscarPropuestaPorEstadoAprobacion(@PathVariable Integer numero,
                                                                   @RequestParam("carrera") String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorEstadoAprobacion(numero, carrera);
    }

    @GetMapping("propuesta/periodo/{valor}")
    public List<VistaPropuesta> buscarPropuestaPorPeriodo(@PathVariable String valor,
                                                          @RequestParam("carrera") String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorPeriodo(valor, carrera);
    }

    @GetMapping("propuesta/tipo")
    public List<VistaPropuesta> buscarPropuestaPorTipo(@RequestParam("nombre") String nombre,
                                                       @RequestParam("carrera") String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorTipo(nombre, carrera);
    }

    @GetMapping("propuesta/por-categoria")
    public List<VistaPropuesta> buscarPropuestaPorCategoria(@RequestParam("categoria") String categoria,
                                                            @RequestParam("carrera") String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorCategoria(categoria, carrera);
    }

    @GetMapping("propuesta/tipo-categoria")
    public List<VistaPropuesta> buscarPropuestaPorCategoriaTipo(@RequestParam("tipo") String tipo,
                                                            @RequestParam("categoria") String categoria,
                                                            @RequestParam("carrera") String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorTipoCategoria(tipo,categoria, carrera);
    }

    @GetMapping("propuesta/tema/{nombre}")
    public List<VistaPropuesta> buscarPropuestaPorTema(@PathVariable String nombre,
                                                       @RequestParam("carrera") String carrera ) {
        return this.vistasEntidadesService.buscarPropuestaPorTema(nombre, carrera);
    }

    @GetMapping("propuesta/tutor/usuario/{idUsuario}")
    public List<VistaPropuesta> buscarPropuestaPorTutor(@PathVariable Integer idUsuario,
                                                       @RequestParam("facultad") String facultad ) {
        return this.vistasEntidadesService.buscarPropuestaPorTutor(idUsuario, facultad);
    }

    @GetMapping("propuesta/revisor/usuario/{idUsuario}")
    public List<VistaPropuesta> buscarPropuestaPorRevisor(@PathVariable Integer idUsuario,
                                                        @RequestParam("facultad") String facultad ) {
        return this.vistasEntidadesService.buscarPropuestaPorRevisor(idUsuario, facultad);
    }


    @GetMapping("propuesta/estudiante/usuario/{idUsuario}")
    public List<VistaPropuesta> buscarPropuestaPorEstudiante(@PathVariable("idUsuario") Integer idUsuario) {
        return this.vistasEntidadesService.buscarPropuestaPorEstudiante(idUsuario);
    }

}
