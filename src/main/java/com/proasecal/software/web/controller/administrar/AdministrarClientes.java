package com.proasecal.software.web.controller.administrar;

import com.proasecal.software.web.entity.administrar.Clientes;
import com.proasecal.software.web.entity.parametricas.*;
import com.proasecal.software.web.service.administrar.ClienteService;
import com.proasecal.software.web.service.parametricas.CiudadService;
import com.proasecal.software.web.service.parametricas.DepartamentoService;
import com.proasecal.software.web.service.parametricas.IdTipoPaisService;
import com.proasecal.software.web.service.parametricas.PaisService;
import com.proasecal.software.web.service.seguridad.FiltrosSeccionesService;
import com.proasecal.software.web.service.seguridad.SeccionesService;
import com.proasecal.software.web.util.Notify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value= "/clientes")
public class AdministrarClientes {
    private final Logger LOG = LoggerFactory.getLogger(AdministrarClientes.class);

    @Autowired
    PaisService paisService;
    @Autowired
    IdTipoPaisService idTipoPaisService;
    @Autowired
    DepartamentoService departamentoService;
    @Autowired
    CiudadService ciudadService;
    @Autowired
    ClienteService clienteService;
    @Autowired
    FiltrosSeccionesService filtrosSeccionesService;
    @Autowired
    SeccionesService seccionesService;


    private static int currentPage = 1;
    private static int pagSize = 5;
    private static String sortColumn = "idPais.nombrePais";
    private static String sortO = "ASC";
    private static String paisFront  ="_";
    private static String idTipoPais ="_";
    private static String razonSocial="_";
    private static String numeroid   ="_";
    private static String nombreComercial = "_";
    private static String estado    ="";
    private static final int[] PAGE_SIZES = {5, 10,20,50};
    private static final int BUTTONS_TO_SHOW = 3;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam("paisFront") Optional<String> paisFront,
                             @RequestParam("idTipoPais") Optional<String> idTipoPais,
                             @RequestParam("razonSocial")Optional<String> razonSocial,
                             @RequestParam("numeroid")Optional<String> numeroid,
                             @RequestParam("nombreComercial")Optional<String> nombreComercial,
                             @RequestParam("estado")Optional<String> estado,
                             @RequestParam("nameColumn") Optional<String> nameColumn,
                             @RequestParam("sortBy") Optional<String> sortBy,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size,
                             @RequestParam("save") Optional<String> save,
                             @RequestParam("delete") Optional<Boolean> delete) {
        LOG.info("inicio metodo list clientes");
        sortColumn = nameColumn.isPresent() ? nameColumn.get() : "idPais.nombrePais";
        sortO = sortBy.isPresent() ? sortBy.get() : "ASC";
        currentPage = page.isPresent() ? page.get() : 1;
        pagSize = size.isPresent() ? size.get() : 20;
        this.paisFront      =paisFront.isPresent()?paisFront.get():"";
        this.idTipoPais     =idTipoPais.isPresent()?idTipoPais.get():"";
        this.razonSocial    =razonSocial.isPresent()?razonSocial.get():"";
        this.numeroid       =numeroid.isPresent()?numeroid.get():"";
        this.nombreComercial= nombreComercial.isPresent()?nombreComercial.get():"";
        this.estado= estado.isPresent()?estado.get():"";


        int evalPageSize = size.orElse(pagSize);
        int evalPage = (page.orElse(0) < 1) ? currentPage : page.get() - 1;
        LOG.debug("Consultar Lista clientes");
        Page<Clientes> clientesList =
                clienteService.ListPaginated(
                        paisFront.orElse(this.paisFront),
                        idTipoPais.orElse(this.idTipoPais),
                        razonSocial.orElse(this.razonSocial),
                        numeroid.orElse(this.numeroid),
                        nombreComercial.orElse(this.nombreComercial),
                        estado.orElse(this.nombreComercial),
                        PageRequest.of(
                        page.orElse(currentPage) - 1, pagSize,new Sort(Sort.Direction.valueOf(sortO),sortColumn, "clienteId")
                        ));
        LOG.debug("fin consulta lista clientes");

        Pager pager = new Pager(clientesList.getTotalPages(), clientesList.getNumber(), BUTTONS_TO_SHOW);

        ModelAndView modelAndView = new ModelAndView("web/administrar/clientes/list");

        modelAndView.addObject("filtrosList", filtrosSeccionesService.getListFiltros(seccionesService.findByName("clientes")));
        modelAndView.addObject("listaClientes", clientesList);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("sort", sortBy.orElse(sortO));
        modelAndView.addObject("nameColumn", nameColumn.orElse(sortColumn));        
        modelAndView.addObject("clienteForm",new Clientes());
        modelAndView.addObject("paisFront", paisFront.orElse("").equalsIgnoreCase("_") ? "" : paisFront.orElse(""));
        modelAndView.addObject("idTipoPais", idTipoPais.orElse("").equalsIgnoreCase("_") ? "" : idTipoPais.orElse(""));
        modelAndView.addObject("razonSocial", razonSocial.orElse("").equalsIgnoreCase("_") ? "" : razonSocial.orElse(""));
        modelAndView.addObject("numeroid", numeroid.orElse("").equalsIgnoreCase("_") ? "" : numeroid.orElse(""));
        modelAndView.addObject("nombreComercial", nombreComercial.orElse("").equalsIgnoreCase("_") ? "" : nombreComercial.orElse(""));
        modelAndView.addObject("estado", estado.orElse("").equalsIgnoreCase("_") ? "" : estado.orElse(""));
		modelAndView.addObject("paisList",paisService.obtenerPaises());
		modelAndView.addObject("tipoDocList",idTipoPaisService.obtenerIdxPais()); 

        if(save.isPresent()) {
        	if(save.get().equalsIgnoreCase("create")) {
        		modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Cliente creado correctamente"));
        	}
        	else {
        		modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Cliente modificado correctamente"));
        	}
        }
        //Si Borra un cliente
        if (delete.isPresent()){
            if(delete.get()){
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Cliente eliminado correctamente"));
            }else{
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo eliminar el Cliente. Revise que no tenga laboratorios asociados o muestras patrocinadas"));
            }
        }
        LOG.info("Fin Listar clientes");
        return modelAndView;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView salvarActualizarCliente(@Valid @ModelAttribute("clienteForm") Clientes clienteForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, ModelAndView model) {
        model.setViewName("web/administrar/clientes/form");
        model.addObject("rolesForm", clienteForm);
        clienteForm.setNumeroIdentificacionCliente(clienteForm.getNumeroIdentificacionCliente().trim());


        Clientes clientesExc = clienteService.obtenerPorDocumento(clienteForm.getNumeroIdentificacionCliente());


        if (clientesExc != null && clientesExc.getClienteId() != clienteForm.getClienteId()) {
            bindingResult.rejectValue("numeroIdentificacionCliente", "error", "Este Cliente ya existe. Verifique el Tipo de identificación comercial y el Número identificación del Cliente.");
        }

        if (bindingResult.hasErrors()) {
            List<Departamentos> departamentos = departamentoService.obtenerDepartamentoXPais(clienteForm.getIdPais());
            List<Ciudad> ciudades = ciudadService.obtenerCiudadxDepartamento(clienteForm.getIdDepartamentos());
            List<TipoDocumentoPais> tipoDocumentoPais = idTipoPaisService.obtenerIdSegunPais(clienteForm.getIdPais());
            model.addObject("models", tipoDocumentoPais);
            model.addObject("models2",departamentos);
            model.addObject("models3", ciudades);
            model.addObject("${clienteForm}", clienteForm);
            model.addObject("paisList",paisService.obtenerPaises()); 
            model.addObject("notify", Notify.ERROR("!ERROR", "Ocurrio un error al guardar/editar cliente"));
            return model;
        }

		String ir="create";
		if(clienteForm.getClienteId()!=0) {
			ir="update";			
		}
			        
        try {
            clienteService.guardarActualizarCliente(clienteForm);
        } catch (Exception e) {
            model.addObject("paisList",paisService.obtenerPaises());
            List<Departamentos> departamentos = departamentoService.obtenerDepartamentoXPais(clienteForm.getIdPais());
            List<Ciudad> ciudades = ciudadService.obtenerCiudadxDepartamento(clienteForm.getIdDepartamentos());
            List<TipoDocumentoPais> tipoDocumentoPais = idTipoPaisService.obtenerIdSegunPais(clienteForm.getIdPais());
            model.addObject("models", tipoDocumentoPais);
            model.addObject("models2",departamentos);
            model.addObject("models3", ciudades);
        	if(ir.equalsIgnoreCase("create")) {
        		model.addObject("notify", Notify.ERROR("ERROR", "No se pudo crear el Cliente"));
        	}
        	else {
        		model.addObject("notify", Notify.ERROR("ERROR", "No se pudo modificar el Cliente"));
        	}			
			        	
            return model;
        }
           
		redirectAttributes.addAttribute("save",ir);
		return new ModelAndView("redirect:/clientes/list");

    }

    @RequestMapping(value = "/editarCliente/{nroIdCliente}", method = RequestMethod.GET)
    public ModelAndView editarCliente(@PathVariable long nroIdCliente) {
        Clientes cliente = clienteService.obtenerClienteByID(nroIdCliente);
        ModelAndView modelAndView = clientesCreate().addObject("clienteForm", cliente);
        modelAndView.addObject("paisList",paisService.obtenerPaises()); 
        modelAndView.addObject("models", idTipoPaisService.obtenerIdSegunPais(cliente.getIdPais()));
        modelAndView.addObject("models2", departamentoService.obtenerDepartamentoXPais(cliente.getIdPais()));
        modelAndView.addObject("models3", ciudadService.obtenerCiudadxDepartamento(cliente.getIdDepartamentos()));

        return modelAndView;
    }

    @RequestMapping("/buscarObtenerCliente")
    public String buscarObtenerCliente(@RequestParam("numeroIdentificacionCliente") String numeroIdentificacionCliente, Model model) {
        Clientes clientesForm = clienteService.obtenerPorDocumento(numeroIdentificacionCliente);

        if (clientesForm != null) {
            model.addAttribute("clienteForm", clientesForm);
            model.addAttribute("models", idTipoPaisService.obtenerIdSegunPais(clientesForm.getIdPais()));
            model.addAttribute("models2", departamentoService.obtenerDepartamentoXPais(clientesForm.getIdPais()));
        } else {
            clientesForm = new Clientes();
            clientesForm.setNumeroIdentificacionCliente(numeroIdentificacionCliente);
            model.addAttribute("clienteForm", clientesForm);
        }
        return "web/administrar/clientes/form :: #clienteForm"; //se dejan nombres standart para ser reusados
    }


    @RequestMapping("/idTipoPais")
    public String obtenerIDPais(@RequestParam("idPais.nombrePais") String nombrePais, Model model) {
        Pais pais = new Pais();
        pais.setIdPais(Long.valueOf(nombrePais).longValue());
        List<TipoDocumentoPais> models = idTipoPaisService.obtenerIdSegunPais(pais);
        model.addAttribute("models", models);
        return "web/administrar/clientes/form :: models"; //se dejan nombres standart para ser reusados
    }

    @RequestMapping("/listIdTipoPais")
    public String obtenerIDPaisList(@RequestParam("idPais.nombrePais") String nombrePais, Model model) {
        Pais pais = new Pais();
        pais.setIdPais(Long.valueOf(nombrePais).longValue());
        List<TipoDocumentoPais> models = idTipoPaisService.obtenerIdSegunPais(pais);
        model.addAttribute("models", models);
        return "web/administrar/clientes/list :: models";
    }

    @RequestMapping("/obtDepartamentos")
    public String obtenerDeparSegunPais(@RequestParam("idPais.nombrePais") String nombrePais, Model model) {
        Pais pais = new Pais();
        pais.setIdPais(Long.valueOf(nombrePais).longValue());
        List<Departamentos> models = departamentoService.obtenerDepartamentoXPais(pais);
        model.addAttribute("models2", models);
        return "web/administrar/clientes/form :: models2";
    }


    @RequestMapping("/obtCiudades")
    public String obtenerCiudadesxDepartamentos(@RequestParam("idDepartamento") String idDepartamento, Model model) {
        if(idDepartamento!=null&&!idDepartamento.isEmpty()){
        Departamentos departamentos = new Departamentos();
        departamentos.setIdDepartamentos(Long.valueOf(idDepartamento).longValue());
        List<Ciudad> models = ciudadService.obtenerCiudadxDepartamento(departamentos);
        model.addAttribute("models3", models);
        }
        return "web/administrar/clientes/form :: models3";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView clientesCreate() {
        ModelAndView modelAndView = new ModelAndView("web/administrar/clientes/form");
        Clientes clientesForm = new Clientes();
        modelAndView.addObject("clienteForm", clientesForm);
        modelAndView.addObject("paisList",paisService.obtenerPaises()); 
        return modelAndView;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam("clienteId") String clienteId, ModelAndView model, RedirectAttributes
            redirectAttributes){
        LOG.info("Se prepara para borrar"+clienteId);
        if(clienteService.borrarXId(Long.parseLong(clienteId))){
            LOG.info("Borrado Exitosamente");
            //Si Borra un cliente
            redirectAttributes.addAttribute("delete",true );
            return new ModelAndView("redirect:/clientes/list");

        }else{
            LOG.info("Error al Borrar");
            redirectAttributes.addAttribute("delete",false);
            return new ModelAndView("redirect:/clientes/list");
        }
    }

}
