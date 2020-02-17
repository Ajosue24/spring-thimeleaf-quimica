package com.proasecal.software.web.service.administrar;

import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.seguridad.AuditoriaWeb;
import com.proasecal.software.web.entity.seguridad.EnumAccionAuditoria;
import com.proasecal.software.web.repository.administrar.MuestraRepository;
import com.proasecal.software.web.service.seguridad.AuditoriaWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
@Transactional(rollbackFor = Exception.class)
public class MuestraService {

    @Autowired
    private MuestraRepository muestraRepository;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    AuditoriaWebService auditoriaWebService;
    private String tableName;

    @PostConstruct
    private void init() {
        Class<?> c = Muestras.class;
        Table table = c.getAnnotation(Table.class);
        this.tableName = table.name();
    }

    public void save(Muestras muestras) throws Exception {
        AuditoriaWeb auditoriaWeb = new AuditoriaWeb(tableName, muestras.getMuestraId(), muestras.getMuestraId() != null ? EnumAccionAuditoria.ACTUALIZAR.getNombreAccion() : EnumAccionAuditoria.CREAR.getNombreAccion());
        this.muestraRepository.save(muestras);
        auditoriaWeb.setIdElementoTabla(muestras.getMuestraId());
        auditoriaWebService.save(auditoriaWeb);
    }


    public Optional<Muestras> find(Long id) {
        Optional<Muestras> muestra = this.muestraRepository.findById(id);
        return muestra;
    }

    public List<Muestras> list() {
        return (List<Muestras>) this.muestraRepository.findAll();
    }

    public List<Muestras> findByFechaInicialBetween(Date fechaInicial, Date fechaFinal) throws ParseException {
        return muestraRepository.findByFechaInicialBetween(fechaInicial, fechaFinal);//AndFechaFinalAfter
    }

    public List<Muestras> muestrasParaInscribir(Date fechaInicial, Date fechaFinal, Programas programas) throws ParseException {
        Date date3 = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date3);
        c.add(Calendar.DATE, -1);  // number of days to add
        c.getTime();

        return muestraRepository.findByFechaInicialBetweenAndFechaFinalAfterAndIdPrograma(fechaInicial, fechaFinal, c.getTime(), programas);
    }


    public Boolean exist(Muestras muestras) {

        if (muestras.getMuestraId() == null) {
            Optional<Muestras> optional = this.muestraRepository.findByNumeroMuestraAndIdPrograma(muestras.getNumeroMuestra(), muestras.getIdPrograma());
            if (optional.isPresent()) {
                return true;
            } else {
                return false;
            }
        } else {
            Optional<Muestras> optional = this.muestraRepository.findByNumeroMuestraAndIdProgramaAndMuestraIdIsNot(muestras.getNumeroMuestra(), muestras.getIdPrograma(), muestras.getMuestraId());
            if (optional.isPresent()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean validDate(Muestras muestras) {
        if (muestras.getFechaInicial().compareTo(muestras.getFechaFinal()) <= 0) {
            return false;
        } else {
            return true;
        }
    }

    public Page<Muestras> ListPaginated(String programaFront, String tipoMuestraFront, String numeroFront, String fechaFront, Pageable pageable) {
        Page<Muestras> lista;
        if (numeroFront != null && (!numeroFront.isEmpty() && !numeroFront.equalsIgnoreCase("_"))) {
            lista = muestraRepository.filtro(programaFront, tipoMuestraFront, numeroFront, fechaFront, pageable);
            return lista;
        }

        lista = muestraRepository.filtro2(programaFront, tipoMuestraFront/*, numeroFront*/, fechaFront, pageable);
        return lista;
    }

    public List<Muestras> filtro(String nameFind, String findColumn, String sortColumn, String sort) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Muestras> consulta = criteriaBuilder.createQuery(Muestras.class);
        Root<Muestras> muestras = consulta.from(Muestras.class);
        Predicate filtro = null;

        if (!findColumn.isEmpty() && !nameFind.isEmpty() && (!findColumn.equalsIgnoreCase("_") && (!nameFind.equalsIgnoreCase("_")))) {

            filtro = criteriaBuilder.like(criteriaBuilder.upper(muestras.get(findColumn).as(String.class)), "%" + nameFind.toUpperCase() + "%");

            if (sort.equalsIgnoreCase("DESC")) {
                consulta.select(muestras).where(filtro).orderBy(criteriaBuilder.asc(muestras.get(sortColumn)));
            } else {
                consulta.select(muestras).where(filtro).orderBy(criteriaBuilder.desc(muestras.get(sortColumn)));
            }
        } else {

            if (sort.equalsIgnoreCase("DESC")) {
                consulta.select(muestras).orderBy(criteriaBuilder.asc(muestras.get(sortColumn)));
            } else {
                consulta.select(muestras).orderBy(criteriaBuilder.desc(muestras.get(sortColumn)));
            }
        }

        List<Muestras> lista = em.createQuery(consulta).getResultList();
        return lista;
    }


    public Boolean borrarXId(long id) {
        try {
            Muestras m = new Muestras();
            m.setMuestraId(id);
            muestraRepository.delete(m);
            auditoriaWebService.save(new AuditoriaWeb(tableName, id, EnumAccionAuditoria.ELIMINAR.getNombreAccion()));
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    public Muestras findByNumMuestra(Integer numMuestra, Long idPrograma) {
        Optional<Muestras> muestras = muestraRepository.findBynumeroMuestraAndIdPrograma_ProgramaId(numMuestra, idPrograma);
        if (muestras.isPresent()) {

            return muestras.get();
        }
        return null;
    }

    public List<Muestras> obtMuestrasxAno(Integer ano) {
        String programa = "PRUEBA DE EFICIENCIA EN QUIMICA CLINICA";
        return muestraRepository.obtMuestrasxAno(programa, ano);

    }

    public Muestras findMuestrasForm(Long muestraId) {
        Optional<Muestras> muestra = find(muestraId);
        return muestra.get();
    }
}
