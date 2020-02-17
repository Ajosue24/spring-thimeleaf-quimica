package com.proasecal.software.web.repository.inscripcion;

import java.util.List;
import java.util.Optional;

import com.proasecal.software.web.entity.inscripcion.InscripcionProgramas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proasecal.software.web.entity.inscripcion.PeriodosVigencia;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;

@Repository
public interface PeriodosVigenciaRepository extends CrudRepository<PeriodosVigencia,Long> {

	
	@Query("SELECT pv FROM PeriodosVigencia pv "
			+ ""
			+ "inner join InscripcionProgramas i on pv.inscripProgramaId=i.inscripProgramaId "
			+ "inner join InscripcionMuestras im on i.inscripProgramaId=im.inscripProgramaId "
			+ "inner join Muestras mu on im.idMuestras=mu.muestraId "	
			+ "inner join Programas pro on i.programaId=pro.programaId "			
			+ "inner join Sedes s on i.idSedes=s.idSedes "
			+ "inner join Laboratorios l on s.idLaboratoriosSedes=l.idLaboratoriosSedes "
			+ "inner join UsuariosLabSedes us on i.idUsuarioLabSedes=us.idUsuarioLabSedes "	
			+ "WHERE  CONCAT(im.idMuestras,'') LIKE CONCAT('%',:muestra,'%') and (mu.fechaInicial between pv.fechaInicio and pv.fechaFin)"
			+ "and CONCAT(l.idLaboratoriosSedes,'') LIKE CONCAT('%',:laboratorio,'%') "
			+ "AND CONCAT(s.idSedes,'') LIKE CONCAT('%',:sede,'%')  "
			+ "AND CONCAT(us.idUsuarioLabSedes,'') LIKE CONCAT('%',:usuario,'%') "
			+ "and CONCAT(pro.programaId,'') LIKE CONCAT('%',:programa,'%') "
			+ "and CONCAT(lower(pv.modalidad),'') LIKE CONCAT('%',lower(:modalidad),'%') "
			+ "and CONCAT(pv.enMora,'') LIKE CONCAT('%', :enmora,'%') "
			+ "and CONCAT(pv.muestraPatrocinada,'') LIKE CONCAT('%', :patrocinada,'%')  "
			+ "and CONCAT(pv.clientePatrocinador,'') LIKE CONCAT('%',:clientePatrocinador,'%') "
			+ "and CONCAT(pv.fechaInicio,'') LIKE CONCAT('%',:fechaInicio,'%') "
			+ "and CONCAT(pv.fechaFin,'') LIKE CONCAT('%',:fechaFin,'%') ")
	Page<PeriodosVigencia> filtroMuestraCliente(@Param("muestra") String muestra,@Param("laboratorio") String laboratorio,@Param("sede") String sede,@Param("usuario") String usuario,@Param("programa") String programa,@Param("modalidad") String modalidad,@Param("enmora") String enmora,@Param("patrocinada") String patrocinada,@Param("clientePatrocinador") String clientePatrocinador,@Param("fechaInicio") String fechaInicio,@Param("fechaFin") String fechaFin, Pageable pageable);


	@Query("SELECT pv FROM PeriodosVigencia pv "
			+ ""
			+ "inner join InscripcionProgramas i on pv.inscripProgramaId=i.inscripProgramaId "
			+ "inner join InscripcionMuestras im on i.inscripProgramaId=im.inscripProgramaId "
			+ "inner join Muestras mu on im.idMuestras=mu.muestraId "	
			+ "inner join Programas pro on i.programaId=pro.programaId "			
			+ "inner join Sedes s on i.idSedes=s.idSedes "
			+ "inner join Laboratorios l on s.idLaboratoriosSedes=l.idLaboratoriosSedes "
			+ "inner join UsuariosLabSedes us on i.idUsuarioLabSedes=us.idUsuarioLabSedes "	
			+ "WHERE  CONCAT(im.idMuestras,'') LIKE CONCAT('%',:muestra,'%') and (mu.fechaInicial between pv.fechaInicio and pv.fechaFin)"
			+ "and CONCAT(l.idLaboratoriosSedes,'') LIKE CONCAT('%',:laboratorio,'%') "
			+ "AND CONCAT(s.idSedes,'') LIKE CONCAT('%',:sede,'%')  "
			+ "AND CONCAT(us.idUsuarioLabSedes,'') LIKE CONCAT('%',:usuario,'%') "
			+ "and CONCAT(pro.programaId,'') LIKE CONCAT('%',:programa,'%') "
			+ "and CONCAT(lower(pv.modalidad),'') LIKE CONCAT('%',lower(:modalidad),'%') "
			+ "and CONCAT(pv.enMora,'') LIKE CONCAT('%', :enmora,'%') "
			+ "and CONCAT(pv.muestraPatrocinada,'') LIKE CONCAT('%', :patrocinada,'%')  "
			+ "and CONCAT(pv.fechaInicio,'') LIKE CONCAT('%',:fechaInicio,'%') "
			+ "and CONCAT(pv.fechaFin,'') LIKE CONCAT('%',:fechaFin,'%') ")
	Page<PeriodosVigencia> filtroMuestra(@Param("muestra") String muestra,@Param("laboratorio") String laboratorio,@Param("sede") String sede,@Param("usuario") String usuario,@Param("programa") String programa,@Param("modalidad") String modalidad,@Param("enmora") String enmora,@Param("patrocinada") String patrocinada/*,@Param("clientePatrocinador") String clientePatrocinador*/,@Param("fechaInicio") String fechaInicio,@Param("fechaFin") String fechaFin, Pageable pageable);
			
			
	@Query("SELECT pv FROM PeriodosVigencia pv "
			+ "inner join InscripcionProgramas i on pv.inscripProgramaId=i.inscripProgramaId "
			+ "inner join Programas pro on i.programaId=pro.programaId "			
			+ "inner join Sedes s on i.idSedes=s.idSedes "
			+ "inner join Laboratorios l on s.idLaboratoriosSedes=l.idLaboratoriosSedes "
			+ "inner join UsuariosLabSedes us on i.idUsuarioLabSedes=us.idUsuarioLabSedes "	
			+ "WHERE  CONCAT(l.idLaboratoriosSedes,'') LIKE CONCAT('%',:laboratorio,'%') "
			+ "AND CONCAT(s.idSedes,'') LIKE CONCAT('%',:sede,'%')  "
			+ "AND CONCAT(us.idUsuarioLabSedes,'') LIKE CONCAT('%',:usuario,'%') "
			+ "and CONCAT(pro.programaId,'') LIKE CONCAT('%',:programa,'%') "
			+ "and CONCAT(lower(pv.modalidad),'') LIKE CONCAT('%',lower(:modalidad),'%') "
			+ "and CONCAT(pv.enMora,'') LIKE CONCAT('%', :enmora,'%') "
			+ "and CONCAT(pv.muestraPatrocinada,'') LIKE CONCAT('%', :patrocinada,'%')  "
			+ "and CONCAT(pv.clientePatrocinador,'') LIKE CONCAT('%',:clientePatrocinador,'%') "
			+ "and CONCAT(pv.fechaInicio,'') LIKE CONCAT('%',:fechaInicio,'%') "
			+ "and CONCAT(pv.fechaFin,'') LIKE CONCAT('%',:fechaFin,'%') ")
	Page<PeriodosVigencia> filtroCliente(@Param("laboratorio") String laboratorio,@Param("sede") String sede,@Param("usuario") String usuario,@Param("programa") String programa,@Param("modalidad") String modalidad,@Param("enmora") String enmora,@Param("patrocinada") String patrocinada,@Param("clientePatrocinador") String clientePatrocinador,@Param("fechaInicio") String fechaInicio,@Param("fechaFin") String fechaFin, Pageable pageable);

	
	@Query("SELECT pv FROM PeriodosVigencia pv "
			+ "inner join InscripcionProgramas i on pv.inscripProgramaId=i.inscripProgramaId "
			+ "inner join Programas pro on i.programaId=pro.programaId "
			+ "inner join Sedes s on i.idSedes=s.idSedes "
			+ "inner join Laboratorios l on s.idLaboratoriosSedes=l.idLaboratoriosSedes "
			+ "inner join UsuariosLabSedes us on i.idUsuarioLabSedes=us.idUsuarioLabSedes "	
			+ "WHERE  CONCAT(l.idLaboratoriosSedes,'') LIKE CONCAT('%',:laboratorio,'%') "
			+ "AND CONCAT(s.idSedes,'') LIKE CONCAT('%',:sede,'%')  "
			+ "AND CONCAT(us.idUsuarioLabSedes,'') LIKE CONCAT('%',:usuario,'%') "
			+ "and CONCAT(pro.programaId,'') LIKE CONCAT('%',:programa,'%') "
			+ "and CONCAT(lower(pv.modalidad),'') LIKE CONCAT('%',lower(:modalidad),'%') "
			+ "and CONCAT(pv.enMora,'') LIKE CONCAT('%', :enmora,'%') "
			+ "and CONCAT(pv.muestraPatrocinada,'') LIKE CONCAT('%', :patrocinada,'%')  "
			+ "and CONCAT(pv.fechaInicio,'') LIKE CONCAT('%',:fechaInicio,'%') "
			+ "and CONCAT(pv.fechaFin,'') LIKE CONCAT('%',:fechaFin,'%') ")
	Page<PeriodosVigencia> filtro(@Param("laboratorio") String laboratorio,@Param("sede") String sede,@Param("usuario") String usuario,@Param("programa") String programa,@Param("modalidad") String modalidad,@Param("enmora") String enmora,@Param("patrocinada") String patrocinada,@Param("fechaInicio") String fechaInicio,@Param("fechaFin") String fechaFin, Pageable pageable);

	
	List<PeriodosVigencia> findByInscripProgramaIdAndInscripProgramaId_IdUsuarioLabSedesAndPeriodosvigenciaIdIsNot(InscripcionProgramas inscripcionProgramas,UsuariosLabSedes usuariosLabSedes,Long Id );
	
	List<PeriodosVigencia> findByInscripProgramaIdAndInscripProgramaId_IdUsuarioLabSedes(InscripcionProgramas inscripcionProgramas,UsuariosLabSedes usuariosLabSedes);	
	
	List<PeriodosVigencia> findByInscripProgramaId(InscripcionProgramas inscripcionProgramas );

	@EntityGraph(value = "nombre", type = EntityGraph.EntityGraphType.LOAD)
	Optional<PeriodosVigencia> findById(Long id);


	@Query("SELECT distinct pv FROM PeriodosVigencia pv where   CONCAT(pv.clientePatrocinador,'') LIKE CONCAT('%',:cliente,'%') ")
	Optional<PeriodosVigencia> buscarxcliente(@Param("cliente") String cliente);

}
