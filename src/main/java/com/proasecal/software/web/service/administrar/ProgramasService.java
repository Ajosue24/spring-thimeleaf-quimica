package com.proasecal.software.web.service.administrar;

import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.repository.administrar.ProgramasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProgramasService {
    @Autowired
    ProgramasRepository programasRepository;

    public void save(Programas programas){
        programasRepository.save(programas);
    }

    public Programas getProgramas(Long id){
        return programasRepository.findById(id).get();
    }

    public List<Programas> list(){
        return (List<Programas>) programasRepository.findAll();
    }

    public List<String> getList(){
        return programasRepository.getColumnsTable();
    }

    private List<Programas> programasList;
    
    public List<Programas> searchName(String nombre) {
    	return this.programasRepository.findByNombreProgramaContainingIgnoreCase(nombre);
    }

	public Optional<Programas> find(Long id){
		return this.programasRepository.findById(id);
	}

    public Page<Programas> ListPaginated(String nameFind,String findColumn,String sort,String sortColumn, Pageable pageable){

        if(findColumn.equalsIgnoreCase("estado")){
            nameFind =  nameFind.equalsIgnoreCase("activo")?
                    "true":"false";
        }

        programasList= filtro(nameFind,findColumn,sortColumn,sort);
        //INICIO
         int pageSize = pageable.getPageSize();
         int currentPage = pageable.getPageNumber();
         int startItem = currentPage * pageSize;
         List<Programas> list;
         if (programasList.size() < startItem) {
         list = Collections.emptyList();
         } else {
         int toIndex = Math.min(startItem + pageSize, programasList.size());
         list = programasList.subList(startItem, toIndex); }
        Page<Programas> programasPage = new PageImpl<>(list, pageable, programasList.size());
        //FIN
        return programasPage;
    }

    @PersistenceContext
    private EntityManager em;
    public List<Programas> filtro(String nameFind,String findColumn,String sortColumn,String sort) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Programas> consulta = criteriaBuilder.createQuery(Programas.class);

        Root<Programas> programas = consulta.from(Programas.class);

        Predicate filtro = null;

        if(!findColumn.isEmpty()&&!nameFind.isEmpty()&&(!findColumn.equalsIgnoreCase("_")&&(!nameFind.equalsIgnoreCase("_")))){

            //Caso unico para este modelo
            if(findColumn.equalsIgnoreCase("tipoProgramaId")){
               return programasRepository.findProgramasByTipoProgramaId_NombreTipoProgramaContainingIgnoreCase(nameFind,new Sort(Sort.Direction.valueOf(sort),sortColumn.equalsIgnoreCase("tipoProgramaId")?"tipoProgramaId.nombreTipoPrograma":sortColumn));
            }else{
                filtro = criteriaBuilder.like(criteriaBuilder.upper(programas.get(findColumn).as(String.class)), "%" + nameFind.toUpperCase() + "%");
            }

            if(sort.equalsIgnoreCase("DESC")){
                consulta.select(programas).where(filtro).orderBy(criteriaBuilder.asc(programas.get(sortColumn)));
            }else{
                consulta.select(programas).where(filtro).orderBy(criteriaBuilder.desc(programas.get(sortColumn)));
            }
        }else{
            //Caso unico para este modelo
            if(sortColumn.equalsIgnoreCase("tipoProgramaId")){
               return programasRepository.findProgramasByTipoProgramaId_NombreTipoProgramaContainingIgnoreCase("_",new Sort(Sort.Direction.valueOf(sort),sortColumn.equalsIgnoreCase("tipoProgramaId")?"tipoProgramaId.nombreTipoPrograma":sortColumn));
            }
            if(sort.equalsIgnoreCase("DESC")){
                consulta.select(programas).orderBy(criteriaBuilder.asc(programas.get(sortColumn)));
            }else{
                consulta.select(programas).orderBy(criteriaBuilder.desc(programas.get(sortColumn)));
            }
        }

        List<Programas> lista = em.createQuery(consulta).getResultList();

        return lista;

    }

    public Programas findById(long id){
        return programasRepository.findById(id).get();
    }
    
    public Programas FindByName(String nombrePrograma) {
    	return programasRepository.findProgramasByNombreProgramaEqualsIgnoreCase(nombrePrograma);
    }
}
