package com.proasecal.software.web.service.administrar;

import com.proasecal.software.web.entity.DAO.LaboratoriosCliente;
import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.parametricas.TipoDocumentoPais;
import com.proasecal.software.web.entity.seguridad.*;
import com.proasecal.software.web.repository.administrar.LaboratorioRepository;
import com.proasecal.software.web.repository.seguridad.RolRepository;
import com.proasecal.software.web.repository.seguridad.UsuarioRepository;
import com.proasecal.software.web.repository.seguridad.UsuariosLabSedesRespository;
import com.proasecal.software.web.service.seguridad.AuditoriaWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.Table;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class LaboratorioService {

    @Autowired
    LaboratorioRepository laboratorioRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    RolRepository rolRepository;
    @Autowired
    UsuariosLabSedesRespository usuariosLabSedesRespository;
    @Autowired
    AuditoriaWebService auditoriaWebService;
    private String tableName;

    @PostConstruct
    private void init() {
        Class<?> c = Laboratorios.class;
        Table table = c.getAnnotation(Table.class);
        this.tableName = table.name();
    }

    public void saveLaboratorios(LaboratoriosCliente datos) throws Exception {
        Set<Roles> rol = new HashSet<>();

        rol.add(rolRepository.findByNombreRolIgnoreCase("Laboratorio"));

        Laboratorios laboratorios = new Laboratorios(
                datos.getIdPais(),
                datos.getIdDepartamentos(),
                datos.getIdCiudad(),
                datos.getClienteId(),
                datos.getIdTipoPais(),
                datos.getDireccion(),
                datos.getCorreo(),
                datos.getCorreoAlternativo(),
                datos.getTelefono(),
                datos.getTelefonoAlternativo(),
                null,
                datos.getUsuarioDirector(),
                datos.getUsuarioCalidad(),
                datos.getNumeroIdentificacion(),
                datos.getRazonSocial(),
                datos.getNombreComercial(),
                true
        );

        AuditoriaWeb auditoriaWeb = new AuditoriaWeb(tableName, laboratorios.getIdLaboratoriosSedes(), laboratorios.getIdLaboratoriosSedes() != 0 ? EnumAccionAuditoria.ACTUALIZAR.getNombreAccion() : EnumAccionAuditoria.CREAR.getNombreAccion());
        Laboratorios lab = laboratorioRepository.save(laboratorios);

        auditoriaWeb.setIdElementoTabla(laboratorios.getIdLaboratoriosSedes());
        auditoriaWebService.save(auditoriaWeb);

        Usuarios usuario = new Usuarios(
                datos.getUsuario(),
                datos.getPassword(),
                "",
                "",
                true,
                null,
                null,
                false,
                "",
                rol
        );

        Usuarios user = usuarioRepository.save(usuario);

        UsuariosLabSedes usuariosLabSedes = new UsuariosLabSedes(
                user,
                lab,
                null
        );

        usuariosLabSedesRespository.save(usuariosLabSedes);
    }

    public Laboratorios obtenerLaboratorioeByID(long idLaboratorio) {
        return laboratorioRepository.findById(idLaboratorio).get();
    }

    public Page<Laboratorios> ListPaginated(String cliente, String nombreComercial, String razonSocial, String pais, String tipoId, String id, Pageable pageable) {
        if (cliente.compareTo("null") == 0) {
            cliente = "_";
        }

        if (pais.compareTo("null") == 0) {
            pais = "_";
        }

        if (tipoId.compareTo("null") == 0) {
            tipoId = "_";
        }

        return laboratorioRepository.filtro
                (cliente, nombreComercial, razonSocial, pais, tipoId, id, pageable);
    }

    public boolean existNit(long id, String identificacion, TipoDocumentoPais documentoPais) {
        if (!this.validateEdit(id)) {
            return false;
        }

        List<Laboratorios> numeroIdentificacionList = this.laboratorioRepository.findByNumeroIdentificacionAndIdTipoPais(identificacion, documentoPais);

        if (numeroIdentificacionList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean validateEdit(long laboratorio) {
        try {
            obtenerLaboratorioeByID(laboratorio);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public List<Laboratorios> identificacionAndRazonSocialLike(String laboratorio) {
        return this.laboratorioRepository.findByNumeroIdentificacionContainingIgnoreCaseOrRazonSocialContainingIgnoreCase(laboratorio, laboratorio);
    }

    public List<Laboratorios> list() {
        return (List<Laboratorios>) this.laboratorioRepository.findAll();
    }

    public boolean borrarXId(Long laboratorioId) {
        try {
            Laboratorios lab = laboratorioRepository.findById(laboratorioId).get();
            UsuariosLabSedes usuariosLabSedes = usuariosLabSedesRespository.findByIdLaboratoriosSedes(lab).get();
            usuariosLabSedesRespository.delete(usuariosLabSedes);
            laboratorioRepository.delete(lab);
            auditoriaWebService.save(new AuditoriaWeb(tableName, laboratorioId, EnumAccionAuditoria.ELIMINAR.getNombreAccion()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void editLaboratorios(Laboratorios laboratorios) {

        laboratorioRepository.save(laboratorios);
        AuditoriaWeb auditoriaWeb = new AuditoriaWeb(tableName, laboratorios.getIdLaboratoriosSedes(), laboratorios.getIdLaboratoriosSedes() != 0 ? EnumAccionAuditoria.ACTUALIZAR.getNombreAccion() : EnumAccionAuditoria.CREAR.getNombreAccion());
        auditoriaWebService.save(auditoriaWeb);
    }

    public List<Laboratorios> searchName(String nombre) {
        return this.laboratorioRepository.findByRazonSocialContainingIgnoreCase(nombre);
    }

    public List<Laboratorios> obtLabxMuestra(String muestra) {
        return laboratorioRepository.obtMuestrasxAno(muestra);
    }

}