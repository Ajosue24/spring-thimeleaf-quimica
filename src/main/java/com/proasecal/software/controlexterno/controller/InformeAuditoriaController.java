package com.proasecal.software.controlexterno.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import com.proasecal.software.web.entity.seguridad.AuditoriaControlExterno;
import com.proasecal.software.web.service.seguridad.AuditoriaControlExternoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.proasecal.software.controlexterno.entity.Resultados;
import com.proasecal.software.controlexterno.entity.ResultadosDetallados;
import com.proasecal.software.controlexterno.service.ResultadosDetalladosService;
import com.proasecal.software.controlexterno.service.ResultadosService;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Muestras;

import com.proasecal.software.web.service.administrar.MensurandoService;
import com.proasecal.software.web.service.administrar.MuestraService;

import java.util.Date;


@Controller
@RequestMapping(value= "/controlexternoinforme")
@PropertySource("classpath:static/properties/msg.properties")
public class InformeAuditoriaController {

    @Autowired
    AuditoriaControlExternoService auditoriaControlExternoService;
    @Autowired
    MuestraService muestraService;
    @Autowired
    MensurandoService mensurandoService;
    @Autowired
    ResultadosService resultadosService;
    @Autowired
    ResultadosDetalladosService resultadosDetalladosService;    
    @RequestMapping(value = "/auditoria", method = RequestMethod.GET)
    public ModelAndView auditoria() {       
        ModelAndView modelAndView = new ModelAndView("controlexterno/informeAuditoria/auditoria");
        return modelAndView;
    }
    
    
    @RequestMapping("/downloadAuditoria")
    @ResponseBody
    public void downloadFile(@RequestParam("desde") String desde,
			 @RequestParam("hasta") String hasta, HttpServletResponse response) throws IOException {
        //File file = new File("C:\\tmp\\ya\\plantilla_inscripcion_masiva_muestras.csv");
        List<String> lines = Arrays.asList ("Fecha hora;Programa;Muestra;Accion;Justificacion;Responsable;Id usuario;Nombre de usuario;Usuario resultado;Valor asignado;Mensurando;Filtro;Version");
        Date desde1 = null;
        Date hasta1 = null;
        try {       	
        	desde1 =  new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(desde);  
        	hasta1 =  new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(hasta);        	
	    } catch (Exception e) {
	        return;
	    }

        List<AuditoriaControlExterno> modelsListAuditoria	= auditoriaControlExternoService.informeAuditoria(desde1,hasta1);//Primeros datos de auditoria


        modelsListAuditoria.stream().forEach(ia ->{
	    	  String fec=String.valueOf(ia.getFecha()).replace("-", "/");
	    	  String fechaInforme=fec.substring(8, 11).concat(fec.substring(4, 7)).concat('/'+fec.substring(2, 4)).concat(fec.substring(10, 16));
	    	  String mensurandoMetodo=";";
	    	  String versi="";
	    	  if (ia.getNumeroVersion()!=null){
	    	      versi=String.valueOf(ia.getNumeroVersion());
              }

	    	  if (ia.getIdMensurandos()!=null){
	    	      mensurandoMetodo=ia.getIdMensurandos().getNombreMensurando()+" ("+ ia.getIdMensurandos().getIdUnidadesMedidas().getUnidad()+");Método-> "+ia.getFiltro().getMetodoId()+" - "+ia.getFiltro().getNombreMetodo();
              }

	    	  lines.set(0, lines.get(0)+("\n"+fechaInforme+";"+ia.getPrograma()+";"+ia.getIdMuestras().getNumeroMuestra()+";"+ia.getAccion()+";"+ia.getJustificacion()+";"
                      +ia.getResponsable()+";"+ia.getIdUsuario().getIdUsuario()+";"+ia.getIdUsuario().getNombreUsuario()+";"+ia.getUsuarioResultado()+";"+ia.getValorAsignado()+";"
                      +mensurandoMetodo+";"
                      +versi));
	      });
       
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH_mm_ss", Locale.ENGLISH);
        Path file = Paths.get("auditoria_"+formmat1.format(ldt)+".csv");
        Files.write(file, lines, Charset.forName("UTF-8"));
       
       if(!file.toFile().exists()){
            String errorMessage = "Lo sentimos. En este momento el archivo no se encuentra disponible";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }
        String mimeType= URLConnection.guessContentTypeFromName(file.toFile().getName());
        if(mimeType==null){
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);

        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.toFile().getName() +"\""));

        response.setContentLength((int)file.toFile().length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(file.toFile()));

        FileCopyUtils.copy(inputStream, response.getOutputStream());
        
        if(file.toFile().exists()){
        	file.toFile().delete();
        }
    }
    
    @RequestMapping(value = "/descargarResultados")
    @ResponseBody
    public void descargarResultados(@RequestParam("muestra") String idMuestra,
            @RequestParam("mensurando") String idMensurando, HttpServletResponse response) throws IOException {  

        Muestras muestras = muestraService.find(Long.valueOf(idMuestra)).get();
        Mensurandos mensurandos = mensurandoService.getMensurandos(Long.valueOf(idMensurando));

        List<Resultados> resultadosList = resultadosService.resultadosPorMuestra(muestras);
        List<ResultadosDetallados> resultadosDetalladosList = resultadosDetalladosService.obtenerListaPorResultadosyMensurando(resultadosList, mensurandos);

        List<String> lines = Arrays.asList ("Usuario;Mensurando;Valor informado;Método;Reactivo;Equipo");
        
        for (ResultadosDetallados resultadosDetallados : resultadosDetalladosList) {

        	lines.set(0, lines.get(0)+("\n"+resultadosDetallados.getIdResultados().getIdInscripcionMuestras().getIdUsuarios().getUsuarioId().getNombreUsuario()+";"+resultadosDetallados.getMensurandosId().getNombreMensurando()+" ("+resultadosDetallados.getMensurandosId().getIdUnidadesMedidas().getUnidad()+");"+resultadosDetallados.getValorReportado()+";"+resultadosDetallados.getMetodoId().getCodProasecal() +"-"+resultadosDetallados.getMetodoId().getNombreMetodo()+";"+resultadosDetallados.getReactivoId().getCodProasecal() +"-"+resultadosDetallados.getReactivoId().getNombreReactivo()+";"+resultadosDetallados.getEquipoId().getCodProasecal() +"-"+resultadosDetallados.getEquipoId().getNombreEquipo()));
        }
     
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH_mm_ss", Locale.ENGLISH);
        Path file = Paths.get(muestras.getNumeroMuestra()+"_"+formmat1.format(ldt)+".csv");
        Files.write(file, lines, Charset.forName("UTF-8"));
        //Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        if(!file.toFile().exists()){
            String errorMessage = "Lo sentimos. En este momento el archivo no se encuentra disponible";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }

        String mimeType= URLConnection.guessContentTypeFromName(file.toFile().getName());
        if(mimeType==null){
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);

        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.toFile().getName() +"\""));

        response.setContentLength((int)file.toFile().length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(file.toFile()));

        FileCopyUtils.copy(inputStream, response.getOutputStream());

        if(file.toFile().exists()){
            file.toFile().delete();
        }
    }
}
