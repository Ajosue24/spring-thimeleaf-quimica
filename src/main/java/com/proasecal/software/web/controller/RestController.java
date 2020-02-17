package com.proasecal.software.web.controller;

import com.proasecal.software.web.entity.DAO.PeriodosVigenciaModal;
import com.proasecal.software.web.entity.administrar.Clientes;
import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.administrar.TiposMuestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionProgramas;
import com.proasecal.software.web.entity.inscripcion.PeriodosVigencia;
import com.proasecal.software.web.entity.parametricas.Pais;
import com.proasecal.software.web.entity.parametricas.TipoDocumentoPais;
import com.proasecal.software.web.service.administrar.*;
import com.proasecal.software.web.service.inscripcion.InscripcionProgramasService;
import com.proasecal.software.web.service.inscripcion.PeriodosVigenciaService;
import com.proasecal.software.web.service.parametricas.CiudadService;
import com.proasecal.software.web.service.parametricas.DepartamentoService;
import com.proasecal.software.web.service.parametricas.IdTipoPaisService;
import com.proasecal.software.web.service.parametricas.PaisService;
import com.proasecal.software.web.service.seguridad.UsuarioService;
import com.proasecal.software.web.service.seguridad.UsuariosLabSedesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/rest")
public class RestController {

    @Autowired
    PaisService paisService;
    @Autowired
    ClienteService clienteService;
    @Autowired
    ProgramasService programasService;
    @Autowired
    TipoMuestraService tipoMuestraService;
    @Autowired
    IdTipoPaisService idTipoPaisService;
    @Autowired
    LaboratorioService laboratorioService;
    @Autowired
    DepartamentoService departamentoService;
    @Autowired
    CiudadService ciudadService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    UsuariosLabSedesService usuariosLabSedesService;
    @Autowired
    MuestraService muestraService;
    @Autowired
    PeriodosVigenciaService periodosVigenciaService;
    @Autowired
    InscripcionProgramasService inscripcionProgramasService;

    @RequestMapping(value = "autoCompPais/{desPais}", method = RequestMethod.GET, produces = {
            MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Pais>> getPais(@PathVariable("desPais") String desPais) {
        List<Pais> listaFiltrada = paisService.filtrarPaisLike(desPais);
        try {
            return new ResponseEntity<List<Pais>>(listaFiltrada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<List<Pais>>(listaFiltrada, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "autoCompClientes/", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Clientes>> getClientes(@RequestParam("term") String desCliente) {
        List<Clientes> listaFiltrada = clienteService.filtrarClientesLike(desCliente);
        try {
            return new ResponseEntity<List<Clientes>>(listaFiltrada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<List<Clientes>>(listaFiltrada, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "autoCompClientes2/{desCliente}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Clientes>> getClientes2(@PathVariable("desCliente") String desCliente) {
        List<Clientes> listaFiltrada = clienteService.filtrarClientesLike(desCliente);
        try {
            return new ResponseEntity<List<Clientes>>(listaFiltrada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<List<Clientes>>(listaFiltrada, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "autoCompLaboratorio/", method = RequestMethod.GET, produces = {
            MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Laboratorios>> getLaboratorios(@RequestParam("term") String desLaboratorio) {
        List<Laboratorios> listaFiltrada = laboratorioService.identificacionAndRazonSocialLike(desLaboratorio);
        try {
            return new ResponseEntity<List<Laboratorios>>(listaFiltrada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<List<Laboratorios>>(listaFiltrada, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/programas/{nombre}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Programas>> programas(@PathVariable String nombre) {

        List<Programas> programas = this.programasService.searchName(nombre);
        try {
            return new ResponseEntity<>(programas, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<List<Programas>>(programas, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/tipo/{nombre}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<TiposMuestras>> tiposMuestras(@PathVariable String nombre) {

        List<TiposMuestras> tiposMuestras = this.tipoMuestraService.searchName(nombre);

        try {
            return new ResponseEntity<List<TiposMuestras>>(tiposMuestras, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<List<TiposMuestras>>(tiposMuestras, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/usuario/{nombre}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public boolean usuarios(@PathVariable String nombre) {
        return usuarioService.validarUsuarioExistente(nombre);
    }


    @GetMapping(value = "/periodosVigencia/{id}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public PeriodosVigenciaModal periodosVigenciaXid(@PathVariable String id){
        return periodosVigenciaService.findModal(Long.valueOf(id));
    }

    @GetMapping(value = "/detMuestra/{muestra}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public Muestras detMuestra(@PathVariable String muestra){
        return muestraService.findMuestrasForm(Long.valueOf(muestra));
    }


    @GetMapping(value = "autoPais/", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Pais>> getPPais(@RequestParam("term") String desPais) {
        List<Pais> listaFiltrada = paisService.filtrarPaisLike(desPais);
        try {
            return new ResponseEntity<List<Pais>>(listaFiltrada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<List<Pais>>(listaFiltrada, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "tipoId/", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<TipoDocumentoPais>> getTipoId(@RequestParam("term") String tipoId) {
        List<TipoDocumentoPais> listaFiltrada = idTipoPaisService.filtrarIdLike(tipoId);
        try {
            return new ResponseEntity<List<TipoDocumentoPais>>(listaFiltrada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<List<TipoDocumentoPais>>(listaFiltrada, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value= "/inscripcionPrograma/{id}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public PeriodosVigenciaModal ultimoPeriodoVigencia (@PathVariable String id){
        InscripcionProgramas inscripcionProgramas = inscripcionProgramasService.findById(Long.valueOf(id)).get();

        Set<PeriodosVigencia> periodosVigenciaSet = inscripcionProgramas.getPeriodosVigenciaList();

        List<PeriodosVigencia> periodosVigenciaList = new ArrayList<>();
        periodosVigenciaList.addAll(periodosVigenciaSet);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


        PeriodosVigencia periodosVigencia= periodosVigenciaService.find(maxId(periodosVigenciaList)).get();
        PeriodosVigenciaModal periodosVigenciaModal= new PeriodosVigenciaModal(
                periodosVigencia.getPeriodosvigenciaId(),
                periodosVigencia.getInscripProgramaId(),
                periodosVigencia.getPerVigenciaRegistrados(),
                periodosVigencia.getModalidad(),
                sdf.format(periodosVigencia.getFechaInicio()),
                sdf.format(periodosVigencia.getFechaFin())
        );

        return periodosVigenciaModal;
    }

    private long maxId(List<PeriodosVigencia> periodosVigencia) {
        long id=0;

        for (PeriodosVigencia periodo: periodosVigencia){
            if(id<periodo.getPeriodosvigenciaId()){
                id=periodo.getPeriodosvigenciaId();
            }
        }
        return id;
    }
}
