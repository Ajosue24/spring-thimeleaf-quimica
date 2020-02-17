package com.proasecal.software.web.controller.administrar;

import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.inscripcion.InscripcionMuestras;
import com.proasecal.software.web.entity.inscripcion.InscripcionProgramas;
import com.proasecal.software.web.entity.inscripcion.PeriodosVigencia;
import com.proasecal.software.web.entity.parametricas.RetornoSalida;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import com.proasecal.software.web.service.administrar.MuestraService;
import com.proasecal.software.web.service.administrar.ProgramasService;
import com.proasecal.software.web.service.inscripcion.InscripcionMuestrasService;
import com.proasecal.software.web.service.inscripcion.InscripcionProgramasService;
import com.proasecal.software.web.service.seguridad.UsuarioService;
import com.proasecal.software.web.service.seguridad.UsuariosLabSedesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping(value="/inscripcionMasivaMuestras")
public class InscripcionMasivaMuestrasController {
    List<RetornoSalida> models = new ArrayList<RetornoSalida>();
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    MuestraService muestraService;
    @Autowired
    InscripcionProgramasService inscripcionProgramasService;
    @Autowired
    UsuariosLabSedesService usuariosLabSedesService;
    @Autowired
    InscripcionMuestrasService inscripcionMuestrasService;
	@Autowired
    ProgramasService programasService;

    @RequestMapping(value="/create")
    public ModelAndView form(@RequestParam("save") Optional<String> save) {
        models.clear();
        ModelAndView modelAndView = new ModelAndView("web/administrar/inscripcionMasivaMuestras/form");
        modelAndView.addObject("files");
        return modelAndView;
    }
    @RequestMapping(value="/list")
    public ModelAndView list( @RequestParam("save") Optional<String> save){
        ModelAndView modelAndView = new ModelAndView("web/administrar/inscripcionMasivaMuestras/form");
        modelAndView.addObject("files");
        return modelAndView;
    }

    @RequestMapping("/downloadFile")
    @ResponseBody
    public void downloadFile(@RequestParam("save") Optional<String> save, HttpServletResponse response) throws IOException {
        List<String> lines = Arrays.asList("programa;usuario;enero;febrero;marzo;abril;mayo;junio;julio;agosto;septiembre;octubre;noviembre;diciembre");
        Path file = Paths.get("plantilla_inscripcion_masiva_muestras.csv");
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

    @RequestMapping(value="/save")
    @ResponseStatus(value= HttpStatus.OK)
    public String save(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws Exception {
        boolean errorDatos = false;
        models.clear();
        ArrayList<String> retornoSalida = new ArrayList<>();
        ArrayList<String> retornoSalidaFinal = new ArrayList<>();
        File fileWork = convert(file);
        Date date = new Date();
        String name = file.getOriginalFilename();
        String[] valNombre  = file.getOriginalFilename().split("\\.") ;
        if(!valNombre[valNombre.length -1].equals("csv")){
            retornoSalida.add("El formato del archivo no es csv" );
            errorDatos = true;
            retornoSalidaFinal.addAll(retornoSalida);
        }
        BufferedReader muestraInscribir = new BufferedReader(new FileReader(fileWork));
        String carga;
        int i = 0;
        int j = 0;
        int k = 0;
        Usuarios usuarios = new Usuarios();
        UsuariosLabSedes usulabSede = new UsuariosLabSedes();
        List<InscripcionProgramas> inscripPrograma = new ArrayList<InscripcionProgramas>();
        Date fecFinPerV = new Date();
        Date fecDia = new Date();
        if (!errorDatos){

            while  ((carga = muestraInscribir.readLine()) != null){
                k = 0;
                String[] cargaPaso =null;
                int intIndex = carga.indexOf(",");
                if(intIndex == - 1){
                    cargaPaso = carga.split(";", -1);
                }else{
                    cargaPaso = carga.split(",", -1);
                }


                if( i > 0 ) {
                    j = 0 ;
                    if (cargaPaso[0].equals("")){
                        retornoSalida.add(i+":Programa no existe.");	
                        errorDatos = true;
                    }

                    if (!errorDatos){
	                    try {
	                        Integer.parseInt(cargaPaso[0]);	 
	                     } catch (NumberFormatException excepcion) {
	                         retornoSalida.add(i+":Programa  " + cargaPaso[0] + " no existe.");	
	                    	 errorDatos = true;
	                     }  
                    }       
      
                    if (!errorDatos&&cargaPaso[0].equals("")){
                        retornoSalida.add(i+":Usuario no existe.");
                        errorDatos = true;
                    }                    
                    if (!errorDatos){                    
	                    try {
	                        Integer.parseInt(cargaPaso[1]);	 
	                     } catch (NumberFormatException excepcion) {
	                         retornoSalida.add(i+":Usuario  " + cargaPaso[1] + "  no existe.");
	                    	 errorDatos = true;
	                     }  
                    }
                    if (!errorDatos){
	                    usuarios = usuarioService.findByCodProasecal(cargaPaso[1]);
	                    if (usuarios == null) {
	                        retornoSalida.add(i+":Usuario " + cargaPaso[1] + "  no existe.");
	                        errorDatos = true;	
	                    }   
                    }
                    if (!errorDatos){
                        usulabSede= usuariosLabSedesService.buscarXUsuarioID(usuarios);
                        if (usulabSede == null) {
                            retornoSalida.add(i+":Usuario  " + cargaPaso[1] + " no existe.");
                            errorDatos = true;    
                        }
                    }
                    if (!errorDatos){
                    	
                		Optional<Programas> programas = this.programasService.find(Long.valueOf(cargaPaso[0]));              		
                		if( !programas.isPresent() ) {               			
	                        retornoSalida.add(i+":Programa  " + cargaPaso[0] + "  no existe.");	
                            errorDatos = true;	                   			
                		}	                   	
                		else {
	                        inscripPrograma = inscripcionProgramasService
	                                .listaXprogramasYUsuarios(programas.get(),usulabSede);
	                        if (inscripPrograma.size() <= 0 ){
		                        retornoSalida.add(i+":El usuario  " + cargaPaso[1] + " no tiene Inscrito el Programa "+ cargaPaso[0]+".");
	                            errorDatos = true;
	     
	                        }
                		}
            		}                           
                  
                    if (!errorDatos){
                        for(String valDatos : cargaPaso) {
                            if(!valDatos.equals("")){
                            	if (j>1) {
                                  //Inicia validacion de muestras
                            		try {
                                        Integer.parseInt(valDatos);	 
                                     } catch (NumberFormatException excepcion) {
             	                        retornoSalida.add(i+":Muestra  " + cargaPaso[j] + "  no existe.");	
                                    	 errorDatos = true;
                                     }  		
                            		
                            	    if (!errorDatos){
	                                    Long idMuestra = Long.valueOf(valDatos);
	                                    Integer numMuestra = Integer.valueOf(valDatos);
	                                    Long idPrograma = Long.valueOf(cargaPaso[0]);
	                                    Muestras muestras = muestraService.findByNumMuestra(numMuestra,idPrograma);
	                                    if (muestras ==  null){
	                                        retornoSalida.add(i+":Problema al inscribir la Muestra " + cargaPaso[j] +
	                                                " al Usuario " + cargaPaso[1] + " Muestra " + cargaPaso[j] + " no existe.");
	                                        errorDatos = true;
	                                    }
	                                    if (!errorDatos){
	                                        inscripPrograma = inscripcionProgramasService
	                                                .listaXprogramasYUsuarios(muestras.getIdPrograma(),usulabSede);
	                                        if (inscripPrograma.size() <= 0 ){
	                                            retornoSalida.add(i+":Problema al inscribir la Muestra " + cargaPaso[j] +
	                                                    " al Usuario " + cargaPaso[1] + " el Usuario " + cargaPaso[1] + " no tiene Inscrito el Programa.");
	                                            errorDatos = true;
	                                        }
	                                    }
	                                    if (!errorDatos){	                         
	                                        Date fecFinMuet = muestras.getFechaFinal();
	                                        if (fecDia.after(fecFinMuet) ){
	                                            retornoSalida.add(i+":Problema al inscribir la Muestra " + cargaPaso[j] +
		                                                " al Usuario " + cargaPaso[1] + " Muestra ya procesada.");
	                                            errorDatos = true;
	                                        }
	                                    }                                
	                                    
	                                    
	                                    if (!errorDatos){
	                                        Set<PeriodosVigencia> perVigen = inscripPrograma.get(inscripPrograma.size()-1).getPeriodosVigenciaList();
	                                        List<PeriodosVigencia> perVigenList = new ArrayList<PeriodosVigencia>();
	                                        if(perVigen.size()==0) {
	                                        	retornoSalida.add(i+":Usuario sin periodo de vigencia válido para la muestra " + cargaPaso[j]);
	                                            errorDatos = true;
	                                        }	
	                                        else {
		                                        perVigenList.addAll(perVigen);
		                                        //Collections.sort(perVigenList, PeriodosVigencia.datePerComparator);
		                                        int contPer=0;
		                                        Date fecIniMuet = muestras.getFechaInicial();
		                                        for(PeriodosVigencia periodosVigencia :perVigenList) {
		                                        	if(fecIniMuet.after(periodosVigencia.getFechaInicio())&&fecIniMuet.before(periodosVigencia.getFechaFin())) 
		                                        		contPer++;
		                                        	if(fecIniMuet.compareTo(periodosVigencia.getFechaInicio())==0||fecIniMuet.compareTo(periodosVigencia.getFechaFin())==0) 
		                                        		contPer++;
		                                        }
		                                        if(contPer==0) {
		                                            retornoSalida.add(i+":Problema al inscribir la Muestra " + cargaPaso[j] +
		                                                    " al Usuario " + cargaPaso[1] + " Usuario sin periodo de vigencia válido para la Muestra.");
		                                            errorDatos = true;
		                                        }
	                                        }
	                                    }

	                                    if (!errorDatos){
	                                        InscripcionProgramas inscrito = inscripPrograma.get(inscripPrograma.size()-1);
	                                        boolean valMuestraInscrita = inscripcionMuestrasService.inscrito(muestras,inscrito,usulabSede);
	                                        if (valMuestraInscrita ){
	                                            retornoSalida.add(i+":Problema al inscribir la Muestra " + cargaPaso[j] +
	                                                " al Usuario " + cargaPaso[1] + " Muestra ya inscrita.");
	                                            errorDatos = true;
	                                        }
	                                    }
	                                    if ( !errorDatos){
	                                        InscripcionMuestras inscripcionMuestras = new InscripcionMuestras(inscripPrograma.get(inscripPrograma.size()-1),
	                                                muestras,usulabSede,fecDia);
	                                        try {
	                                            inscripcionMuestrasService.save(inscripcionMuestras);
	                                            retornoSalida.add(i+":Muestra " + cargaPaso[j] + " inscrita correctamente al Usuario " + cargaPaso[1]);
	                                        }
	                                        catch (Exception e){
	                                            retornoSalida.add(i+":Problema al inscribir la Muestra " + cargaPaso[j] +
	                                                    " al Usuario " + cargaPaso[1] + " Muestra ya inscrita.");
	                                        }
	                                    }
		                                
		                                j++;
		                                errorDatos = false;
	                            	}
                            	    else {
		                                j++;
		                                errorDatos = false;
                            	    }
                            	    //Fin validacioin de muestras
                            	}
                            	else {
                                    j++;
                                }
                            }else {
                                k++;
                                j++;
                            }
                        }
                    }
                }else{
                    String[] headers = cargaPaso;
                }

                if ( k>= 12 && i > 0 && !errorDatos){
                    retornoSalida.clear();
                    retornoSalida.add(i+":Sin muestras para inscribir");
                }
                retornoSalidaFinal.addAll(retornoSalida);
                retornoSalida.clear();
                i++;
                errorDatos=false;
            }
        }
        if ( k==0){
            retornoSalida.clear();
            retornoSalida.add(i+":Sin muestras para inscribir");
            retornoSalidaFinal.addAll(retornoSalida);
        }
        for (String salida :retornoSalidaFinal){
            RetornoSalida returnsal = new RetornoSalida() ;
            //int intIndex = salida.indexOf("Problema");
            if (salida.indexOf("correctamente") >0 ){
                returnsal.setSalidaRespuesta(salida);
                returnsal.setValError("0");
                models.add(returnsal);                
            }else {    
                returnsal.setSalidaRespuesta(salida);
                returnsal.setValError("1");
                models.add(returnsal);    
            }
        }

        return "web/administrar/inscripcionMasivaMuestras/form";
    }

    @RequestMapping("/respuesta")
    @ResponseBody
    public List<RetornoSalida> obtenerSedes( Model model ){
        model.addAttribute("models2", models);
        return models;
    }

    public File convert(MultipartFile file) throws Exception{
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
