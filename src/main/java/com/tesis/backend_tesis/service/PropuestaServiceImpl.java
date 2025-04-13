package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.clients.CorreoRestClient;
import com.tesis.backend_tesis.clients.MotorRestClient;
import com.tesis.backend_tesis.repository.IPropuestaRepository;
import com.tesis.backend_tesis.repository.IRevisionRepository;
import com.tesis.backend_tesis.repository.modelo.*;
import com.tesis.backend_tesis.service.dto.DocenteDTO;
import com.tesis.backend_tesis.service.dto.EstudianteDTO;
import com.tesis.backend_tesis.service.dto.UsuarioDTO;
import com.tesis.backend_tesis.service.dto.utils.Converter;
import com.tesis.backend_tesis.utilitarios.Validaciones;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static com.tesis.backend_tesis.utilitarios.Validaciones.esCorreoValido;


@Service
public class PropuestaServiceImpl implements IPropuestaService{

    private static final Logger logger = LogManager.getLogger(PropuestaServiceImpl.class);


    @Autowired
    private Converter converter;

    @Autowired
    private IPropuestaRepository propuestaRepository;

    @Autowired
    private IArchivoService archivoService;

    @Autowired
    private IEstudianteService estudianteService;

    @Autowired
    private IDocenteService docenteService;

    @Autowired
    private IVistasEntidadesService vistasEntidadesService;
    @Autowired
    private Validaciones validaciones;

    @Autowired
    private CorreoRestClient correoRestClient;

    @Autowired
    private IRevisionRepository revisionRepository;

    @Autowired
    private MotorRestClient motorRestClient;

    @Autowired
    private IUsuarioService usuarioService;

    @Override
    @Transactional
    public String guardar(String tipo,
                          String tema,
                          String categoria,
                          String primerCorreo,
                          String segundoCorreo,
                          String tercerCorreo,
                          Integer idDocenteTutor,
                          MultipartFile archivo)throws IOException {

        // 1. Validar si el archivo está presente
        this.validaciones.validarArchivo(archivo, "archivo propuesta", List.of("application/pdf"));


        // 2. Obtener el estudiante principal (obligatorio)

        if (!esCorreoValido(primerCorreo) || primerCorreo ==null ||primerCorreo.isEmpty()   ) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo del estudiante principal es obligatrorio y con dominio @uce.edu.ec.");
        }

        EstudianteDTO estudiantePrimero= null;
        VistaEstudiante vistaEstudiantePrimero = null;

         // variables para buscar el estudiante por el correo
        vistaEstudiantePrimero  = this.vistasEntidadesService.buscarPorCorreoEstudainte(primerCorreo);
        estudiantePrimero = this.estudianteService.buscarPorIdUsuario(vistaEstudiantePrimero.getIdUsuario());

        if (vistaEstudiantePrimero == null || !vistaEstudiantePrimero.getActivo() || !vistaEstudiantePrimero.getCorreoValido()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El primer estudiante con correo " + primerCorreo + " no existe, no está activo o no tiene rol de estudiante.");
        }

        // 3. Validar que los correos sean correctos y distintos entre sí

        if (segundoCorreo != null && !esCorreoValido(segundoCorreo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo del segundo estudiante debe tener el dominio @uce.edu.ec.");
        }

        if (tercerCorreo != null && !esCorreoValido(tercerCorreo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo del tercer estudiante debe tener el dominio @uce.edu.ec.");
        }

        // Validar que los correos no se repitan
        if ((segundoCorreo != null && primerCorreo.equalsIgnoreCase(segundoCorreo)) ||
                (tercerCorreo != null && primerCorreo.equalsIgnoreCase(tercerCorreo)) ||
                (segundoCorreo != null && tercerCorreo != null && segundoCorreo.equalsIgnoreCase(tercerCorreo))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los estudaintes no deben ser los mismos.");
        }

        VistaEstudiante vistaEstudianteSegundo = null;
        EstudianteDTO estudianteSegundo= null;
        String nombresSegundoEstudiante ="";
        if (segundoCorreo != null) {
            vistaEstudianteSegundo = this.vistasEntidadesService.buscarPorCorreoEstudainte(segundoCorreo);
            estudianteSegundo= this.estudianteService.buscarPorIdUsuario(vistaEstudianteSegundo.getIdUsuario());
            nombresSegundoEstudiante = vistaEstudianteSegundo.getApellidos() + " "+ vistaEstudianteSegundo.getNombres();
            if (vistaEstudianteSegundo == null || !vistaEstudianteSegundo.getActivo() || !vistaEstudianteSegundo.getCorreoValido()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El segundo estudiante con correo " + segundoCorreo + " no existe, no está activo o no tiene rol de estudiante.");
            }
            logger.info("Segundo estudiante: Correo: {}, ID Carrera: {}", segundoCorreo, vistaEstudianteSegundo.getCarrera());
        }


        VistaEstudiante vistaEstudianteTercero = null;
        EstudianteDTO estudianteTercero = null;
        String nombresTercerEstudiante ="";
        if (tercerCorreo != null) {
            vistaEstudianteTercero = this.vistasEntidadesService.buscarPorCorreoEstudainte(tercerCorreo);
            estudianteTercero = this.estudianteService.buscarPorIdUsuario(vistaEstudianteTercero.getIdUsuario());
            nombresTercerEstudiante = vistaEstudianteTercero.getApellidos() + " "+ vistaEstudianteTercero.getNombres();
            if (vistaEstudianteTercero == null || !vistaEstudianteTercero.getActivo() || !vistaEstudianteTercero.getCorreoValido()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El segundo estudiante con correo " + tercerCorreo + " no existe, no está activo o no tiene rol de estudiante.");
            }
            logger.info("Segundo estudiante: Correo: {}, ID Carrera: {}", segundoCorreo, vistaEstudianteTercero.getCarrera());
        }


        // 4. FILTRAR POR TIPO (SOLO DEBE EXISTIR EN MULTIMODAL Y UNIMODAL)

        Propuesta verificacionPropuestaexistente = null;


            if (tipo.equals("Proyecto de Investigación")) {

                if (estudiantePrimero != null && !puedeEnviarPropuestas(estudiantePrimero.getId(), "Proyecto de Integración")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El primer estudiante tiene propuestas vigentes en Proyecto de Integración.");

                }

                if (estudianteSegundo != null && !puedeEnviarPropuestas(estudianteSegundo.getId(), "Proyecto de Integración")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El segundo estudiante tiene propuestas vigentes en Proyecto de Integración.");

                }

                if (estudianteTercero != null && !puedeEnviarPropuestas(estudianteTercero.getId(), "Proyecto de Integración")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tercer estudiante tiene propuestas vigentes en Proyecto de Integración.");

                }


                if (categoria.equals("unimodal")) {


                    if (estudiantePrimero!=null && estudianteSegundo!=null && estudianteTercero != null){

                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Para la Unimodal solo es de 2 estudiantes de la misma carrera.");

                    }

                    if (estudiantePrimero!=null && estudianteSegundo!=null){


                        Set<Integer> carrerasUnicas = new HashSet<>(
                                Arrays.asList(
                                        estudiantePrimero.getIdCarrera(),
                                        estudianteSegundo.getIdCarrera()));

                        if (carrerasUnicas.size() != 1){
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Para la Unimodal solo es de 2 estudiantes de la misma carrera.");

                        }

                    }


                    if (estudiantePrimero != null && !puedeEnviarPropuestasTipoCategoria(estudiantePrimero.getId(), "Proyecto de Investigación", "unimodal")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "El primer estudiante tiene propuestas vigentes en Proyecto de Investigación categoria unimodal.");

                    }

                    if (estudianteSegundo != null && !puedeEnviarPropuestasTipoCategoria(estudianteSegundo.getId(), "Proyecto de Investigación", "unimodal")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "El segundo estudiante tiene propuestas vigentes en Proyecto de Investigación categoria unimodal.");

                    }


                } else if (categoria.equals("multimodal")) {


                    if (estudiantePrimero != null || estudianteSegundo != null || estudianteTercero != null) {

                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Se necesitan 3 estudiantes.");
                    }

                    if (!diferentesCarreras(estudiantePrimero.getIdCarrera(), estudianteSegundo.getIdCarrera(), estudianteTercero.getIdCarrera())) {

                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Dificultad de carreras 2 estudinates deben pertener a la misma carrera.");

                    }


                    if (!puedeEnviarPropuestasMultimodal(estudiantePrimero.getId(), estudianteSegundo.getId(), estudianteTercero.getId(), "Proyecto de Investigación")) {

                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Conflicto en los estudiantes solo puede existir dos propuestas una en cada carrera. Proyecto de Investigacion.");
                    }


                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            categoria + " no existente.");

                }

            } else if (tipo.equals("Proyecto de Integración")) {

                if (estudiantePrimero != null && !puedeEnviarPropuestas(estudiantePrimero.getId(), "Proyecto de Investigación")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El primer estudiante tiene propuestas vigentes en Proyecto de Investigación.");

                }

                if (estudianteSegundo != null && !puedeEnviarPropuestas(estudianteSegundo.getId(), "Proyecto de Integración")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El segundo estudiante tiene propuestas vigentes en Proyecto de Investigación.");

                }

                if (estudianteTercero != null && !puedeEnviarPropuestas(estudianteTercero.getId(), "Proyecto de Integración")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tercer estudiante tiene propuestas vigentes en Proyecto de Investigación.");

                }

                if (categoria.equals("unimodal")) {

                    if (estudiantePrimero != null && !puedeEnviarPropuestasTipoCategoria(estudiantePrimero.getId(), "Proyecto de Integración", "unimodal")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "El primer estudiante tiene propuestas vigentes en Proyecto de Integración categoria unimodal.");

                    }

                    if (estudianteSegundo != null && !puedeEnviarPropuestasTipoCategoria(estudianteSegundo.getId(), "Proyecto de Integración", "unimodal")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "El segundo estudiante tiene propuestas vigentes en Proyecto de Integración categoria unimodal.");

                    }

                    if (estudianteTercero != null && !puedeEnviarPropuestasTipoCategoria(estudianteTercero.getId(), "Proyecto de Integración", "unimodal")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "El tercer estudiante tiene propuestas vigentes en Proyecto de Integración categoria unimodal.");

                    }


                } else if (categoria.equals("multimodal")) {


                    if (primerCorreo != null || segundoCorreo != null || tercerCorreo != null) {

                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Se necesitan 3 estudiantes.");

                    }



                        if (!diferentesCarreras(estudiantePrimero.getId(), estudianteSegundo.getId(), estudianteTercero.getId())) {

                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Dificultad de carreras 2 estudinates deben pertener a la misma carrera. Proyecto de Integración.");

                        }

                        if (!puedeEnviarPropuestasMultimodal(estudiantePrimero.getId(), estudianteSegundo.getId(), estudianteTercero.getId(), "Proyecto de Investigación")) {

                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Conflicto en los estudiantes solo puede existir dos propuestas una en cada carrera. Proyecto de Integración.");
                        }


                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            categoria + " no existente.");

                }

            } else {

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        tipo + " no existente.");

            }

        VistaDocente vistaDocente = null;
        DocenteDTO tutor = null;
        if (idDocenteTutor != null) {
            vistaDocente = this.vistasEntidadesService.buscarDocentePorIdDocente(idDocenteTutor);
            if (vistaDocente == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El docente tutor no existe.");
            }
            tutor = this.docenteService.buscarPorIdUsuario(vistaDocente.getIdUsuario());
        }

        // 5. Guardar el archivo
        Archivo ar = this.archivoService.guardar(archivo, estudiantePrimero.getIdUsuario());
        if (Objects.isNull(ar)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el archivo.");
        }


        // 6. Construir la propuesta
        Propuesta propuesta = Propuesta.builder()
                .carrera(vistaEstudiantePrimero.getCarrera())
                .tipo(tipo)
                .categoria(categoria)
                .tema(tema)
                .estudiante1(this.converter.toEntity(estudiantePrimero))
                .estudiante2(estudianteSegundo != null ? this.converter.toEntity(estudianteSegundo) : null)
                .estudiante3(estudianteTercero != null ? this.converter.toEntity(estudianteTercero) : null)
                .tutor(tutor!= null ? this.converter.toEntity(tutor) : null)
                .estadoValidacion(EstadoValidacion.NO_REVISADO)
                .periodo("2024")
                .estadoAprobacion(EstadoAprobacion.EN_REVISON)
                .build();

        Propuesta guardada =this.propuestaRepository.insert(propuesta);

        Revision revision = Revision.builder()
                .archivoSubidoEstudiantes(ar)
                .propuesta(guardada)
                .numeroRevision(1)
                .build();

        Revision revisionGuardada = this.revisionRepository.insert(revision);


        List<String> ccEmails = new ArrayList<>();
        Stream.of(segundoCorreo, tercerCorreo)
                .filter(Objects::nonNull) // Filtra solo los que no son null
                .forEach(ccEmails::add);


        String nombresPrimerEstudiante =
                vistaEstudiantePrimero.getApellidos() + " "+ vistaEstudiantePrimero.getNombres();



        List<String> posiblesNombres = new ArrayList<String>();
        posiblesNombres.add(nombresPrimerEstudiante);
        posiblesNombres.add(nombresSegundoEstudiante);
        posiblesNombres.add(nombresTercerEstudiante);

        String nombres = this.validaciones.obtenerNombresEstudiantes(posiblesNombres);


        try {
            if (revisionGuardada == null || guardada == null ||
                    this.revisionRepository.findById(revisionGuardada.getId()) == null ||
                    this.propuestaRepository.buscarPorId(guardada.getId()) == null) {

                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar propuesta y revisión.");
            }

            String correoDireccion = this.vistasEntidadesService.buscarCarreraPorNombreCarrera(guardada.getCarrera()).getCorreoDireccion();


            this.correoRestClient.notificacionenviopropuestav2(primerCorreo, ccEmails, nombres, tema,
                    correoDireccion, archivo);
            logger.info("Correo enviado exitosamente a {}", primerCorreo);

        } catch (Exception e) {
            logger.error("Error en el proceso: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar propuesta.");
        }

        this.motorRestClient.iniciarProceso(guardada.getId(), guardada.getEstudiante1().getId(), 5);


        // 8. Respuesta exitosa
        return "guardada con exito";

    }

    @Override
    public Propuesta buscar(Integer id) {

        return null;



    }

    @Override
    public Boolean puedeEnviarPropuestas(Integer idEstudiante, String tipo) {
        List<Propuesta> propuestasValidas = propuestaRepository.findPropuestasBy(idEstudiante, tipo);

        // Si existen propuestas con estas condiciones, el estudiante no puede enviar una nueva propuesta
        return propuestasValidas.isEmpty();
    }

    @Override
    public Boolean puedeEnviarPropuestasTipoCategoria(Integer idEstudiante, String tipo, String categoria) {
        List<Propuesta> propuestasValidas = propuestaRepository.findPropuestasByCompleta(idEstudiante, tipo, categoria);

        // Si existen propuestas con estas condiciones, el estudiante no puede enviar una nueva propuesta
        return propuestasValidas.isEmpty();
    }

    @Override
    public Boolean puedeEnviarPropuestasMultimodal(Integer idEstudiante1,Integer idEstudiante2,Integer idEstudiante3, String tipo) {
        List<Propuesta> propuestasValidas1 = propuestaRepository.findPropuestasByCompleta(idEstudiante1, tipo, "multimodal");
        List<Propuesta> propuestasValidas2 = propuestaRepository.findPropuestasByCompleta(idEstudiante2, tipo, "multimodal");
        List<Propuesta> propuestasValidas3 = propuestaRepository.findPropuestasByCompleta(idEstudiante3, tipo, "multimodal");

        if (propuestasValidas1.isEmpty() && propuestasValidas2.isEmpty() && propuestasValidas3.isEmpty()){
            return true;
        }

        if (propuestasValidas1.size() > 1 || propuestasValidas2.size() > 1 || propuestasValidas3.size() > 1) {
            return false;
        }

        Propuesta propuestaExistente = null;

        if (propuestasValidas1.size() == 1) {
            propuestaExistente = propuestasValidas1.get(0);
        } else if (propuestasValidas2.size() == 1) {
            propuestaExistente = propuestasValidas2.get(0);
        } else if (propuestasValidas3.size() == 1) {
            propuestaExistente = propuestasValidas3.get(0);
        }

        if (propuestaExistente != null) {
            // Obtener los estudiantes de la propuesta existente
            Integer e1 = propuestaExistente.getEstudiante1().getId();
            Integer e2 = propuestaExistente.getEstudiante2().getId();
            Integer e3 = propuestaExistente.getEstudiante3().getId();

            // Verificar si los tres estudiantes actuales coinciden con la propuesta registrada (en cualquier orden)
            List<Integer> estudiantesRegistrados = Arrays.asList(e1, e2, e3);
            List<Integer> estudiantesIngresados = Arrays.asList(idEstudiante1, idEstudiante2, idEstudiante3);

            if (new HashSet<>(estudiantesRegistrados).equals(new HashSet<>(estudiantesIngresados))) {
                return true;  // Se permite enviar porque es la misma propuesta
            }
        }

        return false; // No se puede enviar una nueva propuesta
    }

    @Override
    public Boolean puedeEnviarPropuestasUnimodal(Integer idEstudiante1, Integer idEstudiante2, String tipo) {
        List<Propuesta> propuestasValidas1 = propuestaRepository.findPropuestasByCompleta(idEstudiante1, tipo, "unimodal");
        List<Propuesta> propuestasValidas2 = propuestaRepository.findPropuestasByCompleta(idEstudiante2, tipo, "unimodal");
        if (propuestasValidas1.isEmpty() && propuestasValidas2.isEmpty()){
            return true;
        }

        if (propuestasValidas1.size() > 1 || propuestasValidas2.size() > 1) {
            return false;
        }

        Propuesta propuestaExistente = null;

        if (propuestasValidas1.size() == 1) {
            propuestaExistente = propuestasValidas1.get(0);
        } else if (propuestasValidas2.size() == 1) {
            propuestaExistente = propuestasValidas2.get(0);
        }

        if (propuestaExistente != null) {
            // Obtener los estudiantes de la propuesta existente
            Integer e1 = propuestaExistente.getEstudiante1().getId();
            Integer e2 = propuestaExistente.getEstudiante2().getId();
            Integer e3 = propuestaExistente.getEstudiante3().getId();

            // Verificar si los tres estudiantes actuales coinciden con la propuesta registrada (en cualquier orden)
            List<Integer> estudiantesRegistrados = Arrays.asList(e1, e2, e3);
            List<Integer> estudiantesIngresados = Arrays.asList(idEstudiante1, idEstudiante2);

            if (new HashSet<>(estudiantesRegistrados).equals(new HashSet<>(estudiantesIngresados))) {
                return true;  // Se permite enviar porque es la misma propuesta
            }
        }

        return false; // No se puede enviar una nueva propuesta
    }

    @Override
    public Boolean diferentesCarreras(Integer idCarreraEstudiante1, Integer idCarreraEstudiante2, Integer idCarreraEstudiante3) {

        Set<Integer> carrerasUnicas = new HashSet<>(Arrays.asList(idCarreraEstudiante1, idCarreraEstudiante2, idCarreraEstudiante3));

        // Solo es válido si hay exactamente 2 carreras distintas (1 diferente y 2 iguales)
        return carrerasUnicas.size() == 2;
    }

    @Override
    @Transactional
    public Boolean validarPropuesta(Integer idPropuesta, Boolean estadoValidacion, String obsercvaciones) {
        if (estadoValidacion == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La respuesta de validación no puede ser nula.");
        }

        // Obtener la propuesta por ID
        Propuesta propuestaExistente = this.propuestaRepository.buscarPorId(idPropuesta);


        VistaCarrera carrera = this.vistasEntidadesService.buscarCarreraPorNombreCarrera(propuestaExistente.getCarrera());


        if (carrera == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrera no existe");

        }

        if (propuestaExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró la propuesta con el ID: " + idPropuesta);
        }

        //Integer idDirector = this.carreraService.buscarCarreraPorNombre(propuestaExistente.getCarrera()).getIdDirector();


        // Actualizar el estado de validación
        propuestaExistente.setEstadoValidacion(estadoValidacion ? EstadoValidacion.VALIDADO : EstadoValidacion.NO_VALIDADO);
        propuestaExistente.setObservaciones(obsercvaciones);

        Boolean seGuardo= false;
        seGuardo= this.propuestaRepository.update(propuestaExistente);

        if (propuestaExistente.getEstadoValidacion().equals(EstadoValidacion.NO_VALIDADO)){



            Integer idPrimerEstudiante = propuestaExistente.getEstudiante1().getUsuario().getId();

            UsuarioDTO primerEstudianteDTO =  this.usuarioService.buscarPorId(idPrimerEstudiante);

            String primerEstudiante =
                    primerEstudianteDTO.getPrimerApellido()+ " "+ primerEstudianteDTO.getPrimerNombre();

            UsuarioDTO segundoEstudianteDTO=null;
            String segundoEstudiante = null;
            String segundoCorreo = null;
            if (propuestaExistente.getEstudiante2()!=null) {
                segundoEstudianteDTO=   this.usuarioService.buscarPorId(propuestaExistente.getEstudiante2().getUsuario().getId());


                segundoEstudiante =
                        segundoEstudianteDTO.getPrimerApellido()+ " "+ segundoEstudianteDTO.getPrimerNombre();

                segundoCorreo = segundoEstudianteDTO.getCorreo();

            }


            UsuarioDTO tercerEstudianteDTO = null;
            String tercerEstudiante = null;
            String tercerCorreo = null;
            if (propuestaExistente.getEstudiante3()!=null) {

                tercerEstudianteDTO =
                        this.usuarioService.buscarPorId(propuestaExistente.getEstudiante3().getUsuario().getId());

                tercerEstudiante =
                        tercerEstudianteDTO.getPrimerApellido()+ " "+ tercerEstudianteDTO.getPrimerNombre();

                tercerCorreo = tercerEstudianteDTO.getCorreo();
            }


            List<String> posiblesNombres = new ArrayList<String>();

            List<String> ccEmails = new ArrayList<>();
            Stream.of(segundoCorreo, tercerCorreo)
                    .filter(Objects::nonNull) // Filtra solo los que no son null
                    .forEach(ccEmails::add);


            posiblesNombres.add(primerEstudiante);
            posiblesNombres.add(segundoEstudiante);
            posiblesNombres.add(tercerEstudiante);

            String nombres = this.validaciones.obtenerNombresEstudiantes(posiblesNombres);

            System.out.println(nombres);
            System.out.println(ccEmails);


            seGuardo= this.propuestaRepository.update(propuestaExistente);
            try {

                if(!seGuardo){

                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar la validacion.");
                }


                this.correoRestClient.notificacionNegacionTema(
                        primerEstudianteDTO.getCorreo(),
                        ccEmails,
                        nombres,
                        propuestaExistente.getTema()
                        ,carrera.getCorreoDireccion()
                        , propuestaExistente.getObservaciones()
                );


            } catch (Exception e) {

                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo.");
            }

        }

        // Guardar los cambios
        return seGuardo;

    }

    @Override
    public Boolean asignarRevisor(Integer idPropuesta, Integer idDocente1, Integer idDocente2,
                                  MultipartFile rubrica,
                                  MultipartFile archivo,
                                  MultipartFile oficio) {
        // Validar entrada
        if (idPropuesta == null || idPropuesta < 1 ||
                idDocente1 == null || idDocente1 < 1 ||
                idDocente2 == null || idDocente2 <1 ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los parámetros no pueden ser nulos o vacíos.");
        }

        this.validaciones.validarArchivo(archivo, "archivo propuesta", List.of("application/pdf"));
        this.validaciones.validarArchivo(oficio, "oficio de desiganción", List.of("application/pdf"));

        if (rubrica==null || rubrica.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha seleccionado ningún archivo de la propuesta presentada.");
        }


        if (idDocente1.equals(idDocente2)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los docentes no pueden ser iguales.");

        }

        // Obtener la propuesta por ID
        //Propuesta propuestaExistente = this.propuestaRepository.buscarPorId(idPropuesta);

        List<VistaPropuesta> vistaPropuestaExistente = this.vistasEntidadesService.buscarPropuestaPorIdPropuesta(idPropuesta);
        if (vistaPropuestaExistente.get(0) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró una propuesta con el ID: " + idPropuesta);
        }

        if (vistaPropuestaExistente.get(0).getEstadoValidacion().equals(EstadoValidacion.NO_VALIDADO) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La propuesta con el ID: " + idPropuesta + " no está validada.");
        }else if (vistaPropuestaExistente.get(0).getEstadoValidacion().equals(EstadoValidacion.NO_REVISADO)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La propuesta con el ID: " + idPropuesta + " no está revisada.");

        }

        VistaDocente docenteExistente1 = this.vistasEntidadesService.buscarDocentePorIdDocente(idDocente1);

        if (docenteExistente1 == null) {
            // Si el docente es null (aunque Feign debería lanzarlo como excepción), lanzar una excepción HTTP Not Found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un docente1 con el ID: " + idDocente1);
        }

        VistaDocente docenteExistente2 = this.vistasEntidadesService.buscarDocentePorIdDocente(idDocente2);

        if (docenteExistente2 == null) {
            // Si el docente es null (aunque Feign debería lanzarlo como excepción), lanzar una excepción HTTP Not Found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un docente2 con el ID: " + idDocente2);
        }


        Revision revision = this.revisionRepository.findByIdPropuesta(vistaPropuestaExistente.get(0).getId()).getFirst();


        if (revision == null) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró revision para la propuesta: " + idPropuesta);
        }

        List<Integer> docentesRegistrados = Arrays.asList(revision.getRevisor1().getId(), revision.getRevisor2().getId());
        List<Integer> docentesIngresados = Arrays.asList(idDocente1, idDocente2);


        // Asignar el docente según el tipo de revisor
        boolean cambioRealizado;

        if (new HashSet<>(docentesRegistrados).equals(new HashSet<>(docentesIngresados))) {
           cambioRealizado = false;
        }else {

            revision.setRevisor1(
                    this.converter.toEntity(this.docenteService.buscarPorIdUsuario(docenteExistente1.getIdUsuario())));

            revision.setRevisor2(
                    this.converter.toEntity(this.docenteService.buscarPorIdUsuario(docenteExistente2.getIdUsuario())));

           cambioRealizado= true;
        }


        // Si hubo cambios, actualizar la base de datos
        if (!cambioRealizado) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se realizaron cambios, ya estaba asignado.");
        }

        Boolean revisionGuardada= this.revisionRepository.update(revision);
        List<String> toEmails = new ArrayList<>();
        toEmails.add(docenteExistente1.getCorreo());
        toEmails.add(docenteExistente2.getCorreo());

        List<String> ccEmails =  new ArrayList<>();

        String nombresRevisores = docenteExistente1.getApellidos()+" "+ docenteExistente1.getNombres()+
                " y "
                + docenteExistente2.getApellidos()+" "+ docenteExistente2.getNombres();


        VistaEstudiante primer =null;
        VistaEstudiante segundo =null;
        VistaEstudiante tercero =null;

        String nombresPrimerEstudiante =null;
        String nombresSegundoEstudiante =null;
        String nombresTercerEstudiante =null;

        if (vistaPropuestaExistente.getFirst().getCategoria().equals("multimodal")){

            primer =this.vistasEntidadesService.buscarEstudiantePorIdEstudiante(vistaPropuestaExistente.getFirst().getPrimerEstuId());
            segundo =this.vistasEntidadesService.buscarEstudiantePorIdEstudiante(vistaPropuestaExistente.getFirst().getSegundoEstuId());
            tercero =this.vistasEntidadesService.buscarEstudiantePorIdEstudiante(vistaPropuestaExistente.getFirst().getTercerEstuId());

            ccEmails.add(primer.getCorreo());
            ccEmails.add(segundo.getCorreo());
            ccEmails.add(tercero.getCorreo());

            nombresPrimerEstudiante =primer.getApellidos() +" "+primer.getNombres();
            nombresSegundoEstudiante =segundo.getApellidos() +" "+segundo.getNombres();
            nombresTercerEstudiante =tercero.getApellidos() +" "+tercero.getNombres();


        } else if (vistaPropuestaExistente.getFirst().getCategoria().equals("unimodal")) {
            primer =this.vistasEntidadesService.buscarEstudiantePorIdEstudiante(vistaPropuestaExistente.getFirst().getPrimerEstuId());

            ccEmails.add(primer.getCorreo());
            nombresPrimerEstudiante =primer.getApellidos() +" "+primer.getNombres();

            if (vistaPropuestaExistente.getFirst().getSegundoEstuId()!=null){
                segundo =this.vistasEntidadesService.buscarEstudiantePorIdEstudiante(vistaPropuestaExistente.getFirst().getSegundoEstuId());
                ccEmails.add(segundo.getCorreo());  }
                nombresSegundoEstudiante =segundo.getApellidos() +" "+segundo.getNombres();
        }

        List<String> posiblesNombres = new ArrayList<String>();
        posiblesNombres.add(nombresPrimerEstudiante);
        posiblesNombres.add(nombresSegundoEstudiante);
        posiblesNombres.add(nombresTercerEstudiante);

        String nombresEstudiantes = this.validaciones.obtenerNombresEstudiantes(posiblesNombres);
        String correoDireccion = this.vistasEntidadesService.buscarCarreraPorNombreCarrera(vistaPropuestaExistente.getFirst().getCarrera()).getCorreoDireccion();
        String fechaEntrega = this.validaciones.sumarDiasLaborables(LocalDate.now(), 10);
        ccEmails.add(correoDireccion);
        try{
            if (!revisionGuardada) {

                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al asiganr los revisores.");

            }

            //this.motorRestClient.iniciarProceso(guardada.getId());
            logger.info("Se conectó correctamente con el motor de procesos para asignacion revisores. ID propuesta: {}",
                    vistaPropuestaExistente.getFirst().getId());

            this.correoRestClient.asignacionrtevisores(toEmails,
                                                        ccEmails,
                                                        nombresRevisores,
                                                        nombresEstudiantes,
                                                        "fsadf",
                                                        vistaPropuestaExistente.getFirst().getTema(),
                                                        correoDireccion,
                                                        fechaEntrega,
                                                        rubrica,
                                                        archivo,
                                                        oficio);

            logger.info("Correo enviado exitosamente a {} con copia {}", toEmails, ccEmails);

        }catch (Exception e){
            logger.error("Error en el proceso: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el motor de procesos o correo.");
        }
        return revisionGuardada;
    }


}
