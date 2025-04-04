package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.clients.CorreoRestClient;
import com.tesis.backend_tesis.clients.MotorRestClient;
import com.tesis.backend_tesis.repository.IPropuestaRepository;
import com.tesis.backend_tesis.repository.IRevisionRepository;
import com.tesis.backend_tesis.repository.modelo.*;
import com.tesis.backend_tesis.service.dto.DocenteDTO;
import com.tesis.backend_tesis.service.dto.EstudianteDTO;
import com.tesis.backend_tesis.service.dto.utils.Converter;
import com.tesis.backend_tesis.utilitarios.Validaciones;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
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
        if (archivo==null || archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha seleccionado ningún archivo.");
        }

        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se permiten archivos en formato PDF.");
        }

        // 2. Obtener el estudiante principal (obligatorio)

        if (!esCorreoValido(primerCorreo) || primerCorreo ==null ||primerCorreo.isEmpty()   ) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo del estudiante principal es obligatrorio y con dominio @uce.edu.ec.");
        }

        EstudianteDTO estudiantePrimero= null;
        VistaEstudiante vistaEstudiantePrimero = null;


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
            nombresSegundoEstudiante = vistaEstudianteSegundo.getApellidos() + " "+ vistaEstudianteSegundo.getApellidos();
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
            nombresTercerEstudiante = vistaEstudianteTercero.getApellidos() + " "+ vistaEstudianteTercero.getApellidos();
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


                if (categoria.equals("Unimodal")) {

                    if (estudiantePrimero != null && !puedeEnviarPropuestasTipoCategoria(estudiantePrimero.getId(), "Proyecto de Investigación", "Unimodal")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "El primer estudiante tiene propuestas vigentes en Proyecto de Investigación categoria Unimodal.");

                    }

                    if (estudianteSegundo != null && !puedeEnviarPropuestasTipoCategoria(estudianteSegundo.getId(), "Proyecto de Investigación", "Unimodal")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "El segundo estudiante tiene propuestas vigentes en Proyecto de Investigación categoria Unimodal.");

                    }

                    if (estudianteTercero != null && !puedeEnviarPropuestasTipoCategoria(estudianteTercero.getId(), "Proyecto de Investigación", "Unimodal")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "El tercer estudiante tiene propuestas vigentes en Proyecto de Investigación categoria Unimodal.");

                    }


                } else if (categoria.equals("multimodal")) {


                    if (estudiantePrimero != null || estudianteSegundo != null || estudianteTercero != null) {

                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Se necesitan 3 estudiantes.");
                    }

                    if (!diferentesCarreras(estudiantePrimero.getId(), estudianteSegundo.getId(), estudianteTercero.getId())) {

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

                if (categoria.equals("Unimodal")) {

                    if (estudiantePrimero != null && !puedeEnviarPropuestasTipoCategoria(estudiantePrimero.getId(), "Proyecto de Integración", "Unimodal")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "El primer estudiante tiene propuestas vigentes en Proyecto de Integración categoria Unimodal.");

                    }

                    if (estudianteSegundo != null && !puedeEnviarPropuestasTipoCategoria(estudianteSegundo.getId(), "Proyecto de Integración", "Unimodal")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "El segundo estudiante tiene propuestas vigentes en Proyecto de Integración categoria Unimodal.");

                    }

                    if (estudianteTercero != null && !puedeEnviarPropuestasTipoCategoria(estudianteTercero.getId(), "Proyecto de Integración", "Unimodal")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "El tercer estudiante tiene propuestas vigentes en Proyecto de Integración categoria Unimodal.");

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

        this.revisionRepository.insert(revision);


        Revision revision2 = Revision.builder()
                .archivoSubidoEstudiantes(ar)
                .propuesta(guardada)
                .numeroRevision(2)
                .build();

        this.revisionRepository.insert(revision2);


        List<String> ccEmails = new ArrayList<>();
        Stream.of(segundoCorreo, tercerCorreo)
                .filter(Objects::nonNull) // Filtra solo los que no son null
                .forEach(ccEmails::add);



        String nombresPrimerEstudiante =
                propuesta.getEstudiante1().getUsuario().getPrimerApellido()+ " "+ propuesta.getEstudiante1().getUsuario().getPrimerNombre();



        List<String> posiblesNombres = new ArrayList<String>();
        posiblesNombres.add(nombresPrimerEstudiante);
        posiblesNombres.add(nombresSegundoEstudiante);
        posiblesNombres.add(nombresTercerEstudiante);

        String nombres = this.validaciones.obtenerNombresEstudiantes(posiblesNombres);

        try {
            this.correoRestClient.enviareachivo(primerCorreo, ccEmails, nombres, tema, "fing.direccion.computacion@uce.edu.ec", archivo);
            logger.info("Correo enviado exitosamente a {}", primerCorreo);
        } catch (Exception e) {
            logger.error("Error al enviar correo: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo.");
        }

        this.motorRestClient.iniciarProceso(guardada.getId());

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
    public Boolean diferentesCarreras(Integer idCarreraEstudiante1, Integer idCarreraEstudiante2, Integer idCarreraEstudiante3) {

        Set<Integer> carrerasUnicas = new HashSet<>(Arrays.asList(idCarreraEstudiante1, idCarreraEstudiante2, idCarreraEstudiante3));

        // Solo es válido si hay exactamente 2 carreras distintas (1 diferente y 2 iguales)
        return carrerasUnicas.size() == 2;
    }


}
