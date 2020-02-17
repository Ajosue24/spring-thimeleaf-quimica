package com.proasecal.software.web.controller.administrar;

import com.proasecal.software.web.entity.administrar.Laboratorios;
import com.proasecal.software.web.entity.administrar.Sedes;
import com.proasecal.software.web.entity.parametricas.Ciudad;
import com.proasecal.software.web.entity.parametricas.Departamentos;
import com.proasecal.software.web.entity.parametricas.Pager;
import com.proasecal.software.web.entity.parametricas.Pais;
import com.proasecal.software.web.entity.seguridad.Usuarios;
import com.proasecal.software.web.entity.seguridad.UsuariosLabSedes;
import com.proasecal.software.web.service.administrar.LaboratorioService;
import com.proasecal.software.web.service.administrar.SedesService;
import com.proasecal.software.web.service.parametricas.CiudadService;
import com.proasecal.software.web.service.parametricas.DepartamentoService;
import com.proasecal.software.web.service.parametricas.PaisService;
import com.proasecal.software.web.service.seguridad.RolService;
import com.proasecal.software.web.service.seguridad.UsuarioService;
import com.proasecal.software.web.service.seguridad.UsuariosLabSedesService;
import com.proasecal.software.web.util.Notify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
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

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/sedes")
@PropertySource("classpath:static/properties/msg.properties")
@Transactional
public class SedesController {

    @Autowired
    SedesService sedesService;

    @Autowired
    UsuariosLabSedesService usuariosLabSedesService;

    @Autowired
    DepartamentoService departamentoService;
    @Autowired
    CiudadService ciudadService;

    @Autowired
    PaisService paisService;

    @Autowired
    LaboratorioService laboratorioService;

    @Autowired
    RolService rolService;

    @Autowired
    UsuarioService usuarioService;


    private static int currentPage = 1;
    private static int pagSize = 5;
    private static String sortColumn = "clienteId";
    private static String sortO = "DESC";
    private static String pais = "_";
    private static String laboratorio = "_";
    private static String nombreSede = "_";
    private static String usuario = "_";
    private static String fechaCreacion = "_";
    private static String impResultados = "false";
    private static final int[] PAGE_SIZES = {5, 10, 20, 50};
    private static final int BUTTONS_TO_SHOW = 3;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam("pais") Optional<String> pais,
                             @RequestParam("laboratorioFront") Optional<String> laboratorioFront,
                             @RequestParam("nombreSede") Optional<String> nombreSede,
                             @RequestParam("usuario") Optional<String> usuario,
                             @RequestParam("fechaCreacion") Optional<String> fechaCreacion,
                             @RequestParam("impResultados") Optional<String> impResultados,
                             @RequestParam("nameColumn") Optional<String> nameColumn,
                             @RequestParam("sortBy") Optional<String> sortBy,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size,
                             @RequestParam("delete") Optional<String> delete,
                             @RequestParam("save") Optional<String> save) {
        ModelAndView modelAndView = new ModelAndView("web/administrar/sedes/list");

        sortColumn = nameColumn.isPresent() ? nameColumn.get() : "nombreSede";
        sortO = sortBy.isPresent() ? sortBy.get() : "ASC";
        currentPage = page.isPresent() ? page.get() : 1;
        pagSize = size.isPresent() ? size.get() : 20;
        int evalPageSize = size.orElse(pagSize);
        this.pais = pais.isPresent() ? pais.get() : "_";
        this.laboratorio = laboratorioFront.isPresent() ? laboratorioFront.get() : "_";
        this.nombreSede = nombreSede.isPresent() ? nombreSede.get() : "_";
        this.usuario = usuario.isPresent() ? usuario.get() : "_";
        this.fechaCreacion = fechaCreacion.isPresent() ? fechaCreacion.get() : "_";
        this.impResultados = impResultados.isPresent() ? impResultados.get() : "_";


        Page<Sedes> sedesList = sedesService.filtro(usuario.orElse(this.usuario),
                pais.orElse(this.pais),
                nombreSede.orElse(this.nombreSede),
                validarLaboratorioString(laboratorioFront)[0],
                validarLaboratorioString(laboratorioFront)[1],
                fechaCreacion.orElse(this.fechaCreacion),
                impResultados.orElse(this.impResultados),
                PageRequest.of(
                        page.orElse(currentPage) - 1, pagSize, new Sort(Sort.Direction.valueOf(sortO), sortColumn, "idSedes")
                ));

        Pager pager = new Pager(sedesList.getTotalPages(), sedesList.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("pais", pais.orElse("").equalsIgnoreCase("_") ? "" : pais.orElse(""));
        modelAndView.addObject("laboratorioFront", laboratorioFront.orElse("").equalsIgnoreCase("_") ? "" : laboratorioFront.orElse(""));
        modelAndView.addObject("nombreSede", nombreSede.orElse("").equalsIgnoreCase("_") ? "" : nombreSede.orElse(""));
        modelAndView.addObject("usuario", usuario.orElse("").equalsIgnoreCase("_") ? "" : usuario.orElse(""));
        modelAndView.addObject("fechaCreacion", fechaCreacion.orElse("").equalsIgnoreCase("_") ? "" : fechaCreacion.orElse(""));
        modelAndView.addObject("impResultados", impResultados.orElse("false"));
        modelAndView.addObject("sedesList", sedesList);
        modelAndView.addObject("paisList", paisService.obtenerPaises());
        List<Laboratorios> laboratoriosList = laboratorioService.list();
        modelAndView.addObject("laboratoriosList", laboratoriosList);

        List<Boolean> mostrarBotonEliminar = new ArrayList<>();
        for (Sedes sedesT : sedesList.getContent()) {
            if (sedesT.getSedesUsuariosList() != null && sedesT.getSedesUsuariosList().size() > 0) {
                mostrarBotonEliminar.add(false);
            } else {
                mostrarBotonEliminar.add(true);
            }
        }

        if (save.isPresent()) {
            if (save.get().equalsIgnoreCase("exito")) {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Sede creada correctamente"));
            } else if (save.get().equalsIgnoreCase("editar")) {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Sede modificada correctamente"));
            } else if (save.get().equalsIgnoreCase("editarError")) {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo modificar la Sede"));
            } else {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "Ocurrio un error al procesar la solicitud"));
            }
        }
        if (delete.isPresent()) {
            if (delete.get().equalsIgnoreCase("exito")) {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Sede eliminada Exitosamente"));
            } else {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo eliminar la Sede. Revise que no tenga usuarios con resultados reportados"));
            }
        }

        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("sort", sortBy.orElse(sortO));
        modelAndView.addObject("nameColumn", nameColumn.orElse(sortColumn));
        modelAndView.addObject("mostrarBotonEliminar", mostrarBotonEliminar);

        return modelAndView;
    }


    private String[] validarLaboratorioString(Optional<String> laboratorio) {
        //logica para separar el id de lab con razon social
        String[] parts = new String[2];
        if (laboratorio.orElse(this.laboratorio).contains(" l ")) {
            parts = laboratorio.orElse(this.laboratorio).split(" l ");
            if (parts.length != 2) {
                String[] parts2 = new String[2];
                parts2[0] = "";
                parts2[1] = "";
                return parts2;
            }
            return parts;
        } else {
            parts[0] = laboratorio.orElse(this.laboratorio);
            parts[1] = laboratorio.orElse(this.laboratorio);
            return parts;
        }
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView sedesCreate() {
        ModelAndView modelAndView = new ModelAndView("web/administrar/sedes/form");
        Sedes sedesForm = new Sedes();
        modelAndView.addObject("sedesForm", sedesForm);
        List<Laboratorios> laboratoriosList = laboratorioService.list();
        modelAndView.addObject("laboratoriosList", laboratoriosList);
        List<UsuariosLabSedes> usuariosSedeList = new ArrayList<>();
        modelAndView.addObject("usuarioSedesForm", new UsuariosLabSedes());
        modelAndView.addObject("usuariosSedeList", usuariosSedeList);
        modelAndView.addObject("paisList", paisService.obtenerPaises());
        return modelAndView;
    }


    @RequestMapping(value = "/usuarios", method = RequestMethod.POST)
    public ModelAndView sedesSaveUsuario(@ModelAttribute("usuarioSedesForm") UsuariosLabSedes usuarioSedesForm, BindingResult bindingResult, ModelAndView model
            , @RequestParam(value = "sedesId", required = false) String sedesId, RedirectAttributes redirectAttributes) {
        model.setViewName("web/administrar/sedes/form");
        model.addObject("usuarioSedesForm", usuarioSedesForm);
        Boolean editarPassword = false;
        Boolean isNuevo = false;
        //caso para edicion
        if (usuarioSedesForm.getUsuarioId().getNombreUsuario() == null) {
            try {
                usuarioSedesForm.getUsuarioId().setNombreUsuario(usuariosLabSedesService.buscarXUsuario(usuarioSedesForm.getUsuarioId().getIdUsuario()).getUsuarioId().getNombreUsuario());
            } catch (Exception e) {
                redirectAttributes.addAttribute("usuario", "error");
                return new ModelAndView("redirect:/sedes/update/" + sedesId);
            }

        }
        if (usuarioSedesForm.getUsuarioId().getNombreUsuario().matches(".*[a-zA-Z].*")) {
            bindingResult.rejectValue("usuarioId.nombreUsuario", "error", "solo Numeros");
            redirectAttributes.addAttribute("usuario", "error");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("usuario", "error");
            return new ModelAndView("redirect:/sedes/update/" + sedesId);
        }

        //nuevo
        if (usuarioSedesForm.getUsuarioId().getIdUsuario() == 0) {
            Usuarios usuarioExistente = usuarioService.findByCodProasecal(usuarioSedesForm.getUsuarioId().getNombreUsuario());
            if (usuarioExistente != null) {
                redirectAttributes.addAttribute("usuario", "error");
                return new ModelAndView("redirect:/sedes/update/" + sedesId);
            }
            if (usuarioSedesForm.getUsuarioId().getPassword().length() >= 4) {
                usuarioSedesForm.getUsuarioId().setPassword(new BCryptPasswordEncoder().encode(usuarioSedesForm.getUsuarioId().getPassword()));
            } else {
                redirectAttributes.addAttribute("usuario", "error");
                return new ModelAndView("redirect:/sedes/update/" + sedesId);
            }
            isNuevo = true;
        } else {
            //Editando
            //validamos que no se encuentre el usuario relacionado a una sede
            if (usuariosLabSedesService.buscarXUsuario(usuarioSedesForm.getUsuarioId().getIdUsuario()).getUsuarioId().getIdUsuario() != 0) {
                editarPassword = true;
            }
            if (usuarioSedesForm.getUsuarioId().getPassword().length() >= 4) {
                usuarioSedesForm.getUsuarioId().setPassword(new BCryptPasswordEncoder().encode(usuarioSedesForm.getUsuarioId().getPassword()));
            } else {
                usuarioSedesForm.getUsuarioId().setPassword(usuarioService.get(usuarioSedesForm.getUsuarioId().getIdUsuario()).getPassword());
            }
        }
        //buscamos rol participante
        try {
            usuarioSedesForm.getUsuarioId().setRolesList(new HashSet<>());
            usuarioSedesForm.getUsuarioId().getRolesList().add(rolService.obtenerPorNombreRol("participante"));
            usuarioSedesForm.getUsuarioId().setCodProasecal(Integer.parseInt(usuarioSedesForm.getUsuarioId().getNombreUsuario()));
            usuarioSedesForm.setIdSedes(new Sedes());
            usuarioSedesForm.setIdSedes(sedesService.get(Long.parseLong(sedesId)));
            usuarioSedesForm.setIdSedes(usuarioSedesForm.getIdSedes());
            usuarioSedesForm.setIdLaboratoriosSedes(new Laboratorios());
            usuarioSedesForm.setIdLaboratoriosSedes(usuarioSedesForm.getIdSedes().getIdLaboratoriosSedes());
            //primero se debe de crear al usuario
            usuarioService.save(usuarioSedesForm.getUsuarioId());
            if (!editarPassword) {
                usuariosLabSedesService.save(usuarioSedesForm);
            }
            if (isNuevo) {
                redirectAttributes.addAttribute("usuario", "exito");
            } else {
                redirectAttributes.addAttribute("usuario", "usuarioEditar");
            }

        } catch (Exception e) {
            redirectAttributes.addAttribute("usuario", "error");
            return new ModelAndView("redirect:/sedes/update/" + sedesId);
        }


        return new ModelAndView("redirect:/sedes/update/" + sedesId);
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView salvarActualizarCliente(@Valid @ModelAttribute("sedesForm") Sedes sedesForm, BindingResult bindingResult, ModelAndView model, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("web/administrar/sedes/form");
        if (bindingResult.hasErrors()) {
            model.setViewName("web/administrar/sedes/form");
            model.addObject("sedesForm", sedesForm);
            List<Laboratorios> laboratoriosList = laboratorioService.list();
            model.addObject("laboratoriosList", laboratoriosList);
            List<UsuariosLabSedes> usuariosSedeList = new ArrayList<>();
            model.addObject("usuarioSedesForm", new UsuariosLabSedes());
            model.addObject("usuariosSedeList", usuariosSedeList);
            model.addObject("paisList", paisService.obtenerPaises());
            return model;
        }
        try {
            Boolean isNewSedes = sedesForm.getIdSedes() >= 1;
            if (sedesService.save(sedesForm)) {
                if (isNewSedes) {
                    redirectAttributes.addAttribute("save", "editar");
                } else {
                    redirectAttributes.addAttribute("save", "exito");
                }

            } else {
                if (sedesForm.getIdSedes() >= 1) {
                    redirectAttributes.addAttribute("save", "editarError");
                } else {
                    redirectAttributes.addAttribute("save", "error");
                }

            }

        } catch (Exception e) {
            if (sedesForm.getIdSedes() >= 1) {
                redirectAttributes.addAttribute("save", "editarError");
            } else {
                redirectAttributes.addAttribute("save", "error");
            }
        }

        return new ModelAndView("redirect:/sedes/list");
    }

    //editar
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public ModelAndView editSedes(@PathVariable long id,
                                  @RequestParam("usuario") Optional<String> usuario,
                                  @RequestParam("delete") Optional<String> delete) {
        ModelAndView modelAndView = new ModelAndView("web/administrar/sedes/form");
        if (usuario.isPresent()) {
            if (usuario.get().equalsIgnoreCase("error")) {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "Ocurrio un error al crear el usuario"));
            } else if (usuario.get().equalsIgnoreCase("usuarioEditar")) {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Usuario modificado correctamente"));
            } else {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Usuario creado correctamente"));
            }
        }
        if (delete.isPresent()) {
            if (delete.get().equalsIgnoreCase("error")) {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "Ocurrio un error al eliminar usuario"));
            } else if (delete.get().equalsIgnoreCase("exito")) {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Usuario Eliminado correctamente"));
            }
        }

        Sedes sedesForm = sedesService.get(id);
        modelAndView.addObject("sedesForm", sedesForm);
        List<Laboratorios> laboratoriosList = laboratorioService.list();
        modelAndView.addObject("laboratoriosList", laboratoriosList);
        List<Departamentos> departamentosList = departamentoService.obtenerDepartamentoXPais(sedesForm.getIdPais());
        List<Ciudad> ciudadList = ciudadService.obtenerCiudadxDepartamento(sedesForm.getIdDepartamentos());
        modelAndView.addObject("models2", departamentosList);
        modelAndView.addObject("models3", ciudadList);
        List<UsuariosLabSedes> usuariosSedeList = usuariosLabSedesService.obtenerUsuariosPorSedes(id);
        modelAndView.addObject("usuarioSedesForm", new UsuariosLabSedes());
        modelAndView.addObject("usuariosSedeList", usuariosSedeList);
        modelAndView.addObject("paisList", paisService.obtenerPaises());
        return modelAndView;
    }

    @RequestMapping(value = "/autoCompleteSedes", method = RequestMethod.GET)
    public String autoCompleteSedes(@RequestParam("idLaboratorio") String idLaboratorio, Model model) {
        Sedes sedesForm = new Sedes();
        Laboratorios laboratorio = laboratorioService.obtenerLaboratorioeByID(Long.parseLong(idLaboratorio));
        sedesForm.setIdLaboratoriosSedes(laboratorio);
        sedesForm.setNombreSede(laboratorio.getRazonSocial());
        sedesForm.setIdPais(laboratorio.getIdPais());
        sedesForm.setIdDepartamentos(laboratorio.getIdDepartamentos());
        sedesForm.setIdCiudad(laboratorio.getIdCiudad());
        sedesForm.setDireccion(laboratorio.getDireccion());
        sedesForm.setCorreo(laboratorio.getCorreo());
        sedesForm.setCorreoAlternativo(laboratorio.getCorreoAlternativo());
        sedesForm.setTelefono(laboratorio.getTelefono());
        sedesForm.setTelefonoAlternativo(laboratorio.getTelefonoAlternativo());
        sedesForm.setUsuarioCalidad(laboratorio.getUsuarioCalidad());
        sedesForm.setUsuarioDirector(laboratorio.getUsuarioDirector());
        List<Departamentos> departamentosList = departamentoService.obtenerDepartamentoXPais(sedesForm.getIdPais());
        List<Ciudad> ciudadList = ciudadService.obtenerCiudadxDepartamento(sedesForm.getIdDepartamentos());
        model.addAttribute("models2", departamentosList);
        model.addAttribute("models3", ciudadList);
        model.addAttribute("sedesForm", sedesForm);
        List<Laboratorios> laboratoriosList = laboratorioService.list();
        model.addAttribute("laboratoriosList", laboratoriosList);
        model.addAttribute("paisList", paisService.obtenerPaises());

        return "web/administrar/sedes/form :: sedesFragment";
    }

    @RequestMapping("/obtDepartamentos")
    public String obtenerDeparSegunPais(@RequestParam("idPais.nombrePais") String nombrePais, Model model) {
        Pais pais = new Pais();
        pais.setIdPais(Long.valueOf(nombrePais).longValue());
        List<Departamentos> models = departamentoService.obtenerDepartamentoXPais(pais);
        model.addAttribute("models2", models);
        return "web/administrar/sedes/form :: models2";
    }

    @RequestMapping("/obtCiudades")
    public String obtenerCiudadesxDepartamentos(@RequestParam("idDepartamento") String idDepartamento, Model model) {
        Departamentos departamentos = new Departamentos();
        departamentos.setIdDepartamentos(Long.valueOf(idDepartamento).longValue());
        List<Ciudad> models = ciudadService.obtenerCiudadxDepartamento(departamentos);
        model.addAttribute("models3", models);
        return "web/administrar/sedes/form :: models3";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam("clienteId") String clienteId, ModelAndView model, RedirectAttributes
            redirectAttributes) {

        if (sedesService.borrarXId(Long.parseLong(clienteId))) {
            redirectAttributes.addAttribute("delete", "exito");
            return new ModelAndView("redirect:/sedes/list");

        } else {
            redirectAttributes.addAttribute("delete", "");
            return new ModelAndView("redirect:/sedes/list");
        }

    }

    @RequestMapping(value = "/deleteUsuario", method = RequestMethod.GET)
    public ModelAndView deleteUsuario(@RequestParam("usuarioId") String usuarioId, @RequestParam(value = "sedesId", required = false) String sedesId, ModelAndView model, RedirectAttributes
            redirectAttributes) {
        try {
            if (usuarioService.deleteById(Long.parseLong(usuarioId))) {
                redirectAttributes.addAttribute("delete", "exito");
                return new ModelAndView("redirect:/sedes/update/" + sedesId);

            } else {
                redirectAttributes.addAttribute("delete", "error");
                return new ModelAndView("redirect:/sedes/update/" + sedesId);
            }

        } catch (Exception e) {
            redirectAttributes.addAttribute("delete", "error");
            return new ModelAndView("redirect:/sedes/update/" + sedesId);
        }
    }
}
