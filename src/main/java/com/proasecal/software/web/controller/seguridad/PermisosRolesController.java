package com.proasecal.software.web.controller.seguridad;


import com.proasecal.software.web.entity.seguridad.Modulos;
import com.proasecal.software.web.entity.seguridad.Permisos;
import com.proasecal.software.web.entity.seguridad.Roles;
import com.proasecal.software.web.entity.seguridad.Secciones;
import com.proasecal.software.web.service.seguridad.ModuloService;
import com.proasecal.software.web.service.seguridad.PermisoService;
import com.proasecal.software.web.service.seguridad.RolService;
import com.proasecal.software.web.service.seguridad.SeccionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/pemisosRoles")
public class PermisosRolesController {

    @Autowired
    RolService rolService;
    @Autowired
    ModuloService moduloService;
    @Autowired
    PermisoService permisoService;
    @Autowired
    SeccionesService seccionesService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView main() {
        ModelAndView modelAndView = new ModelAndView("web/security/permisos_roles");
        List<String> lista = Arrays.asList("LOCO", "otra cosa", "otro rol");
        Permisos permisosObj = new Permisos();
        List<Roles> listaRoles = rolService.obtenerListaRoles();
        List<Modulos> listaModulos = moduloService.obtenerModulos();
        List<Secciones> listaSecciones = seccionesService.list();
        modelAndView.addObject("permisosRolesForm", new Permisos());
        modelAndView.addObject("listaModulos", new ArrayList<>());
        modelAndView.addObject("modulosForm", new Modulos());
        modelAndView.addObject("listaSecciones", listaSecciones);
       listaRoles.removeIf(t->t.getNombreRol().equalsIgnoreCase("ADMIN"));
        modelAndView.addObject("listaRoles", listaRoles);
        return modelAndView;
    }

    /**
     * Atributo que busca si Los modulos se encuentran habilitados para dicho Rol
     * 0 = roles // pos 1 = modulo
     */
    @RequestMapping(value = "/enableModule", method = RequestMethod.GET)
    public String enableModule(@RequestParam("modulos") List<Long> paramsLong, Model model) {
        Modulos modulos = moduloService.buscarModuloPorId(paramsLong.get(1));
        Roles rolSeleccionado = rolService.obtenerRol(paramsLong.get(0));
        modulos.getRolesList().add(rolSeleccionado);
        moduloService.guardarModulo(modulos);
        List<Permisos> permisosxModulo = permisoService.obtenerPermisosPorModulos(modulos);
        model.addAttribute("listaPermisos", permisosxModulo);
        List<Boolean> permisosRol = new ArrayList<>();
        //Guardar Todos los permisos nuevo 22-02-2019
        for (Permisos permisos : permisosxModulo) {
            permisos.getListRoles().add(rolSeleccionado);
            permisoService.guardarPermiso(permisos);
        }
        //Fin

        //front no usa stream
        for (Permisos r : permisosxModulo) {
            if (r.getListRoles().stream()
                    .anyMatch(t -> t.getIdRoles() == paramsLong.get(0))) {
                permisosRol.add(true);
            } else {
                permisosRol.add(false);
            }
        }
        model.addAttribute("permisosRol", permisosRol);
        return "web/security/permisos_roles :: permisosRolesTable";
    }

    /**
     * Remover De la tabla
     * pos 0 = roles // pos 1 = modulo
     */
    @RequestMapping(value = "/disableModule", method = RequestMethod.GET)
    public String disable(@RequestParam("modulos") List<Long> paramsLong, Model model) {
        Modulos modulos = moduloService.buscarModuloPorId(paramsLong.get(1));
        modulos.getRolesList().removeIf(t -> t.getIdRoles() == paramsLong.get(0));
        Roles roles = rolService.obtenerRol(paramsLong.get(0));
        List<Permisos> permisos = permisoService.obtenerPermisosXRolyModulo(roles,modulos);
        for (Permisos p : permisos) {
            p.getListRoles().remove(roles);
            permisoService.guardarPermiso(p);
        }
        moduloService.guardarModulo(modulos);
        return "web/security/permisos_roles :: permisosRolesTable";
    }

    //
    /**
     * Habilita Permiso Para Url
     * 0 = idURL / 1 = Rol
     */
    @RequestMapping(value = "/enableUrl", method = RequestMethod.GET)
    public String enableUrl(@RequestParam("url") List<Long> paramsLong, Model model) {
        Permisos permisos = permisoService.obtenerPermiso(paramsLong.get(0));
        permisos.getListRoles().add(rolService.obtenerRol(paramsLong.get(1)));
        permisoService.guardarPermiso(permisos);
        return "web/security/permisos_roles :: content";
    }

    /**
     * Eliminar permiso por url
     * 0 = id / 1 = rol
     */
    @RequestMapping(value = "/disableUrl", method = RequestMethod.GET)
    public String disableUrl(@RequestParam("url") List<Long> paramsLong, Model model) {
        Permisos permisos = permisoService.obtenerPermiso(paramsLong.get(0));
        permisos.getListRoles().removeIf(t -> t.getIdRoles() == paramsLong.get(1));
        permisoService.guardarPermiso(permisos);
        return "web/security/permisos_roles :: content";
    }


    /**
     * 0 = rol / 1 = modulo
     */
    @RequestMapping(value = "/validateModuleStatus", method = RequestMethod.GET)
    public String validateModuleStatus(@RequestParam("rolxmodulo") List<Long> rolxmodulo, Model model) {
        Modulos modulosXRol = moduloService.buscarModuloPorId(rolxmodulo.get(1));
        Boolean cEstadoModulo = modulosXRol.getRolesList().stream().anyMatch(t -> t.getIdRoles() == rolxmodulo.get(0));
        List<Modulos> modulos = moduloService.obtenerModulosXSeccion(modulosXRol.getIdSecciones().getIdSecciones());
        Boolean cEstadoSeccion = modulos.stream().anyMatch(t -> t.getPermisosList().stream().anyMatch(x -> x.getListRoles().stream().anyMatch(l -> l.getIdRoles() == rolxmodulo.get(0))));
        model.addAttribute("cEstadoModulo", cEstadoModulo);
        model.addAttribute("cEstadoSeccion", cEstadoSeccion);
        return "web/security/permisos_roles :: jsBck";
    }


    @RequestMapping(value = "/onlyLoad2Form", method = RequestMethod.GET)
    public String onlyLoad2Form(@RequestParam("rolxmodulo") List<Long> rolxmodulo, @RequestParam("secciones") Optional<String> secciones, Model model) {
        if (rolxmodulo.get(2) == 1) {
            Modulos modulos = moduloService.buscarModuloPorId(rolxmodulo.get(1));
            List<Permisos> permisosxModulo = new ArrayList<>();
            permisosxModulo = permisoService.obtenerPermisosPorModulos(modulos);
            model.addAttribute("listaPermisos", permisosxModulo);
            List<Boolean> permisosRol = new ArrayList<>();
            //front no usa stream
            for (Permisos r : permisosxModulo) {
                if (r.getListRoles().stream()
                        .anyMatch(t -> t.getIdRoles() == rolxmodulo.get(0))) {
                    permisosRol.add(true);
                } else {
                    permisosRol.add(false);
                }
            }
            model.addAttribute("permisosRol", permisosRol);
        } else if (secciones.isPresent() && !secciones.get().isEmpty()) {
            List<Modulos> modulosList = moduloService.obtenerModulosXSeccion(Long.parseLong(secciones.get()));
            List<Permisos> permisosxModulo = new ArrayList<>();
            for (Modulos modulos : modulosList) {
                permisosxModulo.addAll(permisoService.obtenerPermisosPorModulos(modulos));
            }
            model.addAttribute("listaPermisos", permisosxModulo);
            List<Boolean> permisosRol = new ArrayList<>();
            //front no usa stream
            for (Permisos r : permisosxModulo) {
                if (r.getListRoles().stream()
                        .anyMatch(t -> t.getIdRoles() == rolxmodulo.get(0))) {
                    permisosRol.add(true);
                } else {
                    permisosRol.add(false);
                }
            }
            model.addAttribute("permisosRol", permisosRol);
        }
        return "web/security/permisos_roles :: permisosRolesTable";
    }


    @RequestMapping(value = "/loadListSecciones", method = RequestMethod.GET)
    public String loadListSecciones(@RequestParam("listRoles") Optional<String> rol, @RequestParam("listSecciones") Optional<String> seccion, Model model) {
        List<Modulos> listModulos = moduloService.obtenerModulosXSeccion(Long.parseLong(seccion.get()));
        model.addAttribute("listaModulos", listModulos);
        return "web/security/permisos_roles :: modulos";
    }


    @RequestMapping(value = "/validateSeccionStatus", method = RequestMethod.GET)
    public String validateSeccionStatus(@RequestParam("listRoles") Optional<String> rol, @RequestParam("listSecciones") Optional<String> seccion, Model model) {
        if (seccion.isPresent() && !seccion.get().isEmpty()) {
            List<Modulos> modulos = moduloService.obtenerModulosXSeccion(Long.parseLong(seccion.get()));
            Boolean cEstadoSeccion = modulos.stream().anyMatch(
                    t -> t.getPermisosList().stream().anyMatch(x -> x.getListRoles().stream().anyMatch(l -> l.getIdRoles() == Long.parseLong(rol.get()))));
            model.addAttribute("cEstadoModulo", false);
            model.addAttribute("cEstadoSeccion", cEstadoSeccion);
        } else {
            return "redirect:web/security/permisos_roles";
        }

        return "web/security/permisos_roles :: jsBck";
    }


    @RequestMapping(value = "/disableSecciones", method = RequestMethod.GET)
    public String disableSeccion(@RequestParam("seccion") Optional<Long> seccionId, @RequestParam("listRoles") Optional<Long> roles, Model model) {
        if (seccionId.isPresent() & roles.isPresent()) {


            List<Modulos> modulosList = moduloService.obtenerModulosXSeccion(seccionId.get());
            for (Modulos mod : modulosList) {
                mod.getRolesList().removeIf(t -> t.getIdRoles() == roles.get());
                mod.getPermisosList().stream().forEach(x -> x.getListRoles().removeIf(t -> t.getIdRoles() == roles.get()));
                moduloService.guardarModulo(mod);
            }
        }
        return "web/security/permisos_roles :: permisosRolesTable";
    }


    @RequestMapping(value = "/enableSecciones", method = RequestMethod.GET)
    public String enableSecciones(@RequestParam("seccion") Optional<Long> seccionId, @RequestParam("listRoles") Optional<Long> roles, Model model) {
        if (seccionId.isPresent() & roles.isPresent()) {
            List<Modulos> modulosList = moduloService.obtenerModulosXSeccion(seccionId.get());
            Roles rol = rolService.obtenerRol(roles.get());
            for (Modulos mod : modulosList) {
                mod.getRolesList().add(rol);
                mod.getPermisosList().stream().forEach(x -> x.getListRoles().add(rol));
                moduloService.guardarModulo(mod);
            }
            List<Permisos> permisosxModulo = new ArrayList<>();
            for (Modulos modulos : modulosList) {
                permisosxModulo.addAll(permisoService.obtenerPermisosPorModulos(modulos));
            }
            model.addAttribute("listaPermisos", permisosxModulo);
            List<Boolean> permisosRol = new ArrayList<>();
            //front no usa stream
            for (Permisos r : permisosxModulo) {
                if (r.getListRoles().stream()
                        .anyMatch(t -> t.getIdRoles() == roles.get())) {
                    permisosRol.add(true);
                } else {
                    permisosRol.add(false);
                }
            }
            model.addAttribute("permisosRol", permisosRol);
        }
        return "web/security/permisos_roles :: permisosRolesTable";
    }
}
