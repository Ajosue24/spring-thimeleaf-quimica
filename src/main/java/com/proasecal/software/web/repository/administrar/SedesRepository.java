package com.proasecal.software.web.repository.administrar;

import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.administrar.Sedes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SedesRepository extends CrudRepository<Sedes,Long> {

    @Query("SELECT DISTINCT u FROM Sedes u join UsuariosLabSedes a on u.idSedes = a.idSedes where CONCAT(lower(a.usuarioId.nombreUsuario),'') LIKE CONCAT('%',lower(:usuario),'%')"
            +"AND CONCAT(lower(u.idPais.nombrePais),'') LIKE CONCAT('%',lower(:pais),'%')"
            +"AND CONCAT(lower(unaccent(u.nombreSede)),'') LIKE CONCAT('%',lower(unaccent(:nombreSede)),'%') "
            +"AND (CONCAT(lower(u.idLaboratoriosSedes.numeroIdentificacion),'') LIKE CONCAT('%',lower(:laboratorioId),'%')"
            +"OR CONCAT(lower(u.idLaboratoriosSedes.razonSocial),'') LIKE CONCAT('%',lower(:laboratorioRz),'%'))"
            +"AND CONCAT(u.fechaCreacion,'') LIKE CONCAT('%',:fechaCreacion,'%')" +"AND CONCAT(u.imprimirResultados,'') LIKE CONCAT('%',:impResultados,'%')")
    Page<Sedes> filtroUsuario(@Param("usuario") String usuario,@Param("pais") String pais,@Param("nombreSede") String nombreSede,@Param("laboratorioId") String laboratorioId,@Param("laboratorioRz") String laboratorioRz,@Param("fechaCreacion") String fechaCreacion,@Param("impResultados") String impResultados,Pageable pageable);


    @Query("select u from Sedes u where lower(u.idPais.nombrePais) like lower(concat('%', :pais,'%'))"
            +"AND CONCAT(lower(unaccent(u.nombreSede)),'') LIKE CONCAT('%',lower(unaccent(:nombreSede)),'%') "
            +"AND (CONCAT(lower(u.idLaboratoriosSedes.numeroIdentificacion),'') LIKE CONCAT('%',lower(:laboratorioId),'%')"
            +"OR CONCAT(lower(u.idLaboratoriosSedes.razonSocial),'') LIKE CONCAT('%',lower(:laboratorioRz),'%'))"
            +"AND CONCAT(u.fechaCreacion,'') LIKE CONCAT('%',:fechaCreacion,'%')" +"AND CONCAT(u.imprimirResultados,'') LIKE CONCAT('%',:impResultados,'%')")
    Page<Sedes> filtro(@Param("pais") String pais,@Param("nombreSede") String nombreSede,@Param("laboratorioId") String laboratorioId,@Param("laboratorioRz") String laboratorioRz,@Param("fechaCreacion") String fechaCreacion,@Param("impResultados") String impResultados,Pageable pageable);


    List<Sedes> findByIdLaboratoriosSedes(Laboratorios idLaboratoriosSedes);
    
       @Query("select distinct s from  Sedes s "
    		+ "inner join UsuariosLabSedes uls on s.idSedes=uls.idSedes "
    		+ "inner join InscripcionMuestras im on uls.idUsuarioLabSedes=im.idUsuarios "
    		+ "inner join Muestras m on im.idMuestras=m.muestraId "
            +" where CONCAT(m.muestraId,'')  LIKE CONCAT('%',:muestra,'%') ")
    List<Sedes> obtSedexMuestra(@Param("muestra") String muestra);
}
