package com.proasecal.software.web.controller.administrar;

import com.proasecal.software.web.entity.administrar.Clientes;
import com.proasecal.software.web.entity.DAO.LaboratoriosCliente;
import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.parametricas.*;
import com.proasecal.software.web.service.administrar.ClienteService;
import com.proasecal.software.web.service.administrar.LaboratorioService;
import com.proasecal.software.web.service.parametricas.CiudadService;
import com.proasecal.software.web.service.parametricas.DepartamentoService;
import com.proasecal.software.web.service.parametricas.IdTipoPaisService;
import com.proasecal.software.web.service.parametricas.PaisService;

import com.proasecal.software.web.service.seguridad.FiltrosSeccionesService;
import com.proasecal.software.web.service.seguridad.SeccionesService;
import com.proasecal.software.web.service.seguridad.UsuarioService;
import com.proasecal.software.web.util.Notify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@RequestMapping(value = "/laboratorios")
public class LaboratoriosController {
    @Autowired
    PaisService paisService;
    @Autowired
    IdTipoPaisService idTipoPaisService;
    @Autowired
    DepartamentoService departamentoService;
    @Autowired
    LaboratorioService laboratorioService;
    @Autowired
    CiudadService ciudadService;
    @Autowired
    ClienteService clienteService;
    @Autowired
    SeccionesService seccionesService;
    @Autowired
    FiltrosSeccionesService filtrosSeccionesService;
    @Autowired
    UsuarioService usuarioService;

    private static int currentPage = 1;
    private static int pagSize = 5;
    private static String sortColumn = "idPais.nombrePais";
    private static String sortO = "ASC";
    private static String cliente;
    private static String nombreComercial;
    private static String razonSocial;
    private static String pais;
    private static String tipoId;
    private static String id;
    private static final int[] PAGE_SIZES = {5, 10, 20, 50};
    private static final int BUTTONS_TO_SHOW = 3;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView index(@RequestParam("cliente") Optional<String> cliente,
                              @RequestParam("nombreComercial") Optional<String> nombreComercial,
                              @RequestParam("razonSocial") Optional<String> razonSocial,
                              @RequestParam("pais") Optional<String> pais,
                              @RequestParam("tipoId") Optional<String> tipoId,
                              @RequestParam("id") Optional<String> id,
                              @RequestParam("nameColumn") Optional<String> nameColumn,
                              @RequestParam("sortBy") Optional<String> sortBy,
                              @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size,
                              @RequestParam("save") Optional<Boolean> save,
                              @RequestParam("edit") Optional<Boolean> edit,
                              @RequestParam("delete") Optional<Boolean> delete) {

        sortColumn = nameColumn.isPresent() ? nameColumn.get() : "idPais.nombrePais";
        sortO = sortBy.isPresent() ? sortBy.get() : "ASC";
        currentPage = page.isPresent() ? page.get() : 1;
        pagSize = size.isPresent() ? size.get() : 20;
        int evalPageSize = size.orElse(pagSize);
        this.cliente = cliente.isPresent() ? cliente.get() : "";
        this.nombreComercial = nombreComercial.isPresent() ? nombreComercial.get() : "";
        this.razonSocial = razonSocial.isPresent() ? razonSocial.get() : "";
        this.pais = pais.isPresent() ? pais.get() : "";
        this.tipoId = tipoId.isPresent() ? tipoId.get() : "";
        this.id = id.isPresent() ? id.get() : "";

        Page<Laboratorios> laboratoriosSedesList =
                this.laboratorioService.ListPaginated(this.cliente,
                        this.nombreComercial,
                        this.razonSocial,
                        this.pais,
                        this.tipoId,
                        this.id,
                        PageRequest.of(
                                page.orElse(currentPage) - 1, pagSize, new Sort(Sort.Direction.valueOf(sortO), sortColumn, "idLaboratoriosSedes")));


        Pager pager = new Pager(laboratoriosSedesList.getTotalPages(), laboratoriosSedesList.getNumber(), BUTTONS_TO_SHOW);
        List<Clientes> clientesList = clienteService.getAllClientes();

        ModelAndView modelAndView = new ModelAndView("web/administrar/laboratorios/list");
        modelAndView.addObject("listaLaboratorios", laboratoriosSedesList);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("sort", sortBy.orElse(sortO));
        modelAndView.addObject("nameColumn", nameColumn.orElse(sortColumn));
        modelAndView.addObject("cliente", cliente.orElse("").equalsIgnoreCase("_") ? "" : cliente.orElse(""));
        modelAndView.addObject("nombreComercial", nombreComercial.orElse("").equalsIgnoreCase("_") ? "" : nombreComercial.orElse(""));
        modelAndView.addObject("razonSocial", razonSocial.orElse("").equalsIgnoreCase("_") ? "" : razonSocial.orElse(""));
        modelAndView.addObject("pais", pais.orElse("").equalsIgnoreCase("_") ? "" : pais.orElse(""));
        modelAndView.addObject("tipoId", tipoId.orElse("").equalsIgnoreCase("_") ? "" : tipoId.orElse(""));
        modelAndView.addObject("id", id.orElse("").equalsIgnoreCase("_") ? "" : id.orElse(""));
        modelAndView.addObject("paisList", paisService.obtenerPaises());
        modelAndView.addObject("clientesList", clientesList);
        modelAndView.addObject("tipoDocList", idTipoPaisService.obtenerIdxPais());


        if (save.isPresent()) {
            if (save.get()) {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Laboratorio creado correctamente."));
            } else {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo crear el Laboratorio."));
            }
        }

        if (edit.isPresent()) {
            if (edit.get()) {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Laboratorio modificado correctamente."));
            } else {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo modificar el Laboratorio."));
            }
        }

        if (delete.isPresent()) {
            if (delete.get()) {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Laboratorio eliminado correctamente"));
            } else {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo eliminar el Laboratorio."));
            }
        }

        return modelAndView;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView form() {
        ModelAndView modelAndView = new ModelAndView("web/administrar/laboratorios/form");
        modelAndView.addObject("laboratorioForm", new LaboratoriosCliente());
        List<Clientes> clientesList = clienteService.getAllClientes();
        modelAndView.addObject("clientesList", clientesList);
        modelAndView.addObject("paisList", paisService.obtenerPaises());
        return modelAndView;
    }


    @RequestMapping(value = "/form/{idLaboratorio}", method = RequestMethod.GET)
    public ModelAndView editarLaboratorio(@PathVariable long idLaboratorio) {

        try {
            ModelAndView modelAndView = new ModelAndView("web/administrar/laboratorios/formEdit");
            Laboratorios laboratoriosSedes = laboratorioService.obtenerLaboratorioeByID(idLaboratorio);

            modelAndView.addObject("laboratorioForm", laboratoriosSedes);
            modelAndView.addObject("models", idTipoPaisService.obtenerIdSegunPais(laboratoriosSedes.getIdPais()));
            modelAndView.addObject("models2", departamentoService.obtenerDepartamentoXPais(laboratoriosSedes.getIdPais()));
            modelAndView.addObject("models3", ciudadService.obtenerCiudadxDepartamento(laboratoriosSedes.getIdDepartamentos()));
            modelAndView.addObject("models4", paisService.obtenerPaises());
            //            modelAndView.addObject("models4", paisService.obtenerPaisXId(laboratoriosSedes.getIdPais().getIdPais()).get());
            modelAndView.addObject("models5", laboratoriosSedes);
            List<Clientes> clientesList = clienteService.getAllClientes();
            modelAndView.addObject("clientesList", clientesList);
            modelAndView.addObject("paisList", paisService.obtenerPaises());
            return modelAndView;

        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("web/administrar/laboratorios/list");
            modelAndView.addObject("notify", Notify.ERROR("ERROR", "El laboratorio consultado no existe."));
            return modelAndView;
        }
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@Valid @ModelAttribute("laboratorioForm") LaboratoriosCliente formulario, BindingResult bindingResult, RedirectAttributes redirectAttributes, ModelAndView model) {

        ModelAndView modelAndView = new ModelAndView("web/administrar/laboratorios/form");

        if (this.clienteService.existClient(formulario.getClienteId().getClienteId(), formulario.getIdLaboratoriosSedes())) {
            bindingResult.rejectValue("clienteId", "error", "El cliente no existe.");
        }

        if (this.laboratorioService.existNit(formulario.getIdLaboratoriosSedes(), formulario.getNumeroIdentificacion(), formulario.getIdTipoPais())) {
            bindingResult.rejectValue("numeroIdentificacion", "error", "Este Laboratorio ya existe. Verifique el Tipo de identificación comercial y el Número identificación del Cliente");
        }

        if (this.usuarioService.validarUsuarioExistente(formulario.getUsuario())) {
            bindingResult.rejectValue("usuario", "error", "El nombre de usuario ya esta en uso.");
        }

        if (formulario.getPassword().length() < 4) {
            bindingResult.rejectValue("password", "error", "El tamaño minimo de la contraseña es de cuatro caracteres.");
        }

        if (bindingResult.hasErrors()) {
            List<Departamentos> departamentos = departamentoService.obtenerDepartamentoXPais(formulario.getIdPais());
            List<Ciudad> ciudades = ciudadService.obtenerCiudadxDepartamento(formulario.getIdDepartamentos());
            List<TipoDocumentoPais> tipoDocumentoPais = idTipoPaisService.obtenerIdSegunPais(formulario.getIdPais());
            List<Pais> paises = paisService.obtenerPaises();
            model.setViewName("web/administrar/laboratorios/form");
            model.addObject("models", tipoDocumentoPais);
            model.addObject("models2", departamentos);
            model.addObject("models3", ciudades);
            model.addObject("models4", paises);
            model.addObject("models5", formulario.getClienteId());
            List<Clientes> clientesList = clienteService.getAllClientes();
            model.addObject("clientesList", clientesList);
            model.addObject("paisList", paisService.obtenerPaises());
            model.addObject("laboratorioForm", formulario);
            return model;
        }

        formulario.setPassword(new BCryptPasswordEncoder().encode(formulario.getPassword()));

        try {
            laboratorioService.saveLaboratorios(formulario);
        } catch (Exception e) {
            List<Departamentos> departamentos = departamentoService.obtenerDepartamentoXPais(formulario.getIdPais());
            List<Ciudad> ciudades = ciudadService.obtenerCiudadxDepartamento(formulario.getIdDepartamentos());
            List<TipoDocumentoPais> tipoDocumentoPais = idTipoPaisService.obtenerIdSegunPais(formulario.getIdPais());
            List<Pais> paises = paisService.obtenerPaises();
            model.setViewName("web/administrar/laboratorios/form");
            model.addObject("models", tipoDocumentoPais);
            model.addObject("models2", departamentos);
            model.addObject("models3", ciudades);
            model.addObject("models4", paises);
            model.addObject("models5", formulario.getClienteId());
            List<Clientes> clientesList = clienteService.getAllClientes();
            model.addObject("clientesList", clientesList);
            model.addObject("paisList", paisService.obtenerPaises());
            modelAndView.addObject("notify", Notify.ERROR("ERROR", "Ocurrio un error al procesar la solicitud."));
            return modelAndView;
        }

        redirectAttributes.addAttribute("save", true);
        return new ModelAndView("redirect:/laboratorios/list");
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView edit(@Valid @ModelAttribute("laboratorioForm") Laboratorios formulario, BindingResult bindingResult, RedirectAttributes redirectAttributes, ModelAndView model) {

        ModelAndView modelAndView = new ModelAndView("/web/administrar/laboratorios/form");

        if (this.clienteService.existClient(formulario.getClienteId().getClienteId(), formulario.getIdLaboratoriosSedes())) {
            bindingResult.rejectValue("clienteId", "error", "El cliente no existe.");
        }

        if (this.laboratorioService.existNit(formulario.getIdLaboratoriosSedes(), formulario.getNumeroIdentificacion(), formulario.getIdTipoPais())) {
            bindingResult.rejectValue("numeroIdentificacion", "error", "Este Laboratorio ya existe. Verifique el Tipo de identificación comercial y el Número identificación del Cliente");
        }

        if (bindingResult.hasErrors()) {
            List<Departamentos> departamentos = departamentoService.obtenerDepartamentoXPais(formulario.getIdPais());
            List<Ciudad> ciudades = ciudadService.obtenerCiudadxDepartamento(formulario.getIdDepartamentos());
            List<TipoDocumentoPais> tipoDocumentoPais = idTipoPaisService.obtenerIdSegunPais(formulario.getIdPais());

            model.setViewName("web/administrar/laboratorios/formEdit");
            model.addObject("models", tipoDocumentoPais);
            model.addObject("models2", departamentos);
            model.addObject("models3", ciudades);
            model.addObject("laboratorioForm", formulario);
            return model;
        }

        try {
            formulario.setEstadoLabSedes(true);
            laboratorioService.editLaboratorios(formulario);
        } catch (Exception e) {
            modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo modificar el Laboratorio."));
            return modelAndView;
        }

        redirectAttributes.addAttribute("edit", true);
        return new ModelAndView("redirect:/laboratorios/list");
    }


    @RequestMapping("/idTipoPais")
    public String obtenerIDPais(@RequestParam("idPais.nombrePais") String nombrePais, Model model) {
        Pais pais = new Pais();
        pais.setIdPais(Long.valueOf(nombrePais).longValue());
        List<TipoDocumentoPais> models = idTipoPaisService.obtenerIdSegunPais(pais);
        model.addAttribute("models", models);
        return "web/administrar/laboratorios/form :: models"; //se dejan nombres standart para ser reusados
    }

    @RequestMapping("/obtDepartamentos")
    public String obtenerDeparSegunPais(@RequestParam("idPais.nombrePais") String nombrePais, Model model) {
        Pais pais = new Pais();
        pais.setIdPais(Long.valueOf(nombrePais).longValue());
        List<Departamentos> models = departamentoService.obtenerDepartamentoXPais(pais);
        model.addAttribute("models2", models);
        return "web/administrar/laboratorios/form :: models2";
    }

    @RequestMapping("/obtCiudades")
    public String obtenerCiudadesxDepartamentos(@RequestParam("idDepartamento") String idDepartamento, Model model) {
        if (idDepartamento.compareTo("") == 0) {
            return null;
        } else {
            Departamentos departamentos = new Departamentos();
            departamentos.setIdDepartamentos(Long.valueOf(idDepartamento).longValue());
            List<Ciudad> models = ciudadService.obtenerCiudadxDepartamento(departamentos);
            model.addAttribute("models3", models);
            return "web/administrar/laboratorios/form :: models3";
        }
    }

    @RequestMapping(value = "/autoCompFields", method = RequestMethod.GET)
    public String autoCompFields(@RequestParam("id") Long id, Model model) {
        Clientes datos = clienteService.obtenerClienteByID(id);

        List<Pais> paises = paisService.obtenerPaises();
        List<Departamentos> departamentos = departamentoService.obtenerDepartamentoXPais(datos.getIdPais());
        List<Ciudad> ciudades = ciudadService.obtenerCiudadxDepartamento(datos.getIdDepartamentos());
        List<TipoDocumentoPais> tipoDocumentoPais = idTipoPaisService.obtenerIdSegunPais(datos.getIdPais());
        List<Clientes> clientesList = clienteService.getAllClientes();

        LaboratoriosCliente laboratorios = new LaboratoriosCliente();
        laboratorios.setClienteId(datos);
        laboratorios.setIdPais(datos.getIdPais());
        laboratorios.setIdDepartamentos(datos.getIdDepartamentos());
        laboratorios.setIdCiudad(datos.getIdCiudad());
        laboratorios.setIdPais(datos.getIdPais());
        laboratorios.setIdTipoPais(datos.getIdTipoPais());
        laboratorios.setRazonSocial(datos.getRazonSocial());
        laboratorios.setNombreComercial(datos.getNombreComercial());
        laboratorios.setTelefono(datos.getTelefonoCliente());
        laboratorios.setTelefonoAlternativo(datos.getTelefonoAlternativo());
        laboratorios.setCorreo(datos.getCorreoCliente());
        laboratorios.setCorreoAlternativo(datos.getCorreoAlternativo());
        laboratorios.setDireccion(datos.getDireccionCliente());
        laboratorios.setUsuarioCalidad(datos.getUsuarioCalidad());
        laboratorios.setUsuarioDirector(datos.getUsuarioDirector());

        model.addAttribute("clientesList", clientesList);
        model.addAttribute("paisList", paises);
        model.addAttribute("laboratorioForm", laboratorios);
        model.addAttribute("models", tipoDocumentoPais);
        model.addAttribute("models2", departamentos);
        model.addAttribute("models3", ciudades);
        model.addAttribute("models4", paises);
        model.addAttribute("models5", datos);
        return "web/administrar/laboratorios/form :: labFormFragment";
    }

    @RequestMapping(value = "/delete")
    public ModelAndView form(@RequestParam("laboratorioId") String laboratorioId, ModelAndView model, RedirectAttributes
            redirectAttributes) {

        if (laboratorioService.borrarXId(Long.valueOf(laboratorioId))) {
            redirectAttributes.addAttribute("delete", true);
            return new ModelAndView("redirect:/laboratorios/list");
        } else {
            redirectAttributes.addAttribute("delete", false);
            return new ModelAndView("redirect:/laboratorios/list");
        }
    }
}