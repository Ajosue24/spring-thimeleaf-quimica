package com.proasecal.software.web.service.administrar;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.proasecal.software.web.entity.seguridad.AuditoriaWeb;
import com.proasecal.software.web.entity.seguridad.EnumAccionAuditoria;
import com.proasecal.software.web.service.seguridad.AuditoriaWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.proasecal.software.web.entity.administrar.Equipos;
import com.proasecal.software.web.repository.administrar.EquipoRepository;

@Service
@Transactional
public class EquipoService {

    @Autowired
    EquipoRepository equipoRepository;

    @PersistenceContext
    private EntityManager em;
    @Autowired
    AuditoriaWebService auditoriaWebService;
    private String tableName;

    @PostConstruct
    private void init() {
        Class<?> c = Equipos.class;
        Table table = c.getAnnotation(Table.class);
        this.tableName = table.name();
    }

    public void save(Equipos equipoForm) {
        AuditoriaWeb auditoriaWeb = new AuditoriaWeb(tableName, equipoForm.getEquipoId(), equipoForm.getEquipoId() != null ? EnumAccionAuditoria.ACTUALIZAR.getNombreAccion() : EnumAccionAuditoria.CREAR.getNombreAccion());
        this.equipoRepository.save(equipoForm);
        auditoriaWeb.setIdElementoTabla(equipoForm.getEquipoId());
        auditoriaWebService.save(auditoriaWeb);
    }

    public List<Equipos> findAll(Boolean estado) {
        return this.equipoRepository.findAllByEstado(true);
    }

    public Optional<Equipos> find(Long id) {
        return this.equipoRepository.findById(id);
    }

    public Page<Equipos> ListPaginated(String programaFront, String nombreFront, String grupoFront, String codProsecalFront, Boolean estadoFront, Pageable pageable) {

        Page<Equipos> equiposPage = equipoRepository.findByIdPrograma_NombreProgramaContainingIgnoreCaseAndNombreEquipoContainingIgnoreCaseAndGrupoContainingIgnoreCaseAndCodProasecalContainingIgnoreCaseAndEstado(programaFront, nombreFront, grupoFront, codProsecalFront, estadoFront, pageable);
        return equiposPage;
    }

    public Boolean exist(Equipos equipos) {

        if (equipos.getEquipoId() == null) {
            Optional<Equipos> optional = this.equipoRepository.findByCodProasecalAndIdPrograma(equipos.getCodProasecal(), equipos.getIdPrograma());
            if (optional.isPresent()) {
                return true;
            } else {
                return false;
            }
        } else {
            Optional<Equipos> optional = this.equipoRepository.findByCodProasecalAndIdProgramaAndEquipoIdIsNot(equipos.getCodProasecal(), equipos.getIdPrograma(), equipos.getEquipoId());
            if (optional.isPresent()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public Boolean borrarXId(long id) {
        try {
            Equipos e = new Equipos();
            e.setEquipoId(id);
            equipoRepository.delete(e);
            auditoriaWebService.save(new AuditoriaWeb(tableName, id, EnumAccionAuditoria.ELIMINAR.getNombreAccion()));
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public List<Equipos> listHabilitado(Long inscripcionMuestras) {
        return equipoRepository.findByIdPrograma_ProgramaIdAndEstadoTrue(inscripcionMuestras);
    }

}
