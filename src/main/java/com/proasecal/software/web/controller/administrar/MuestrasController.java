package com.proasecal.software.web.controller.administrar;

import com.proasecal.software.web.entity.DAO.MuestrasInscritas;
import com.proasecal.software.web.entity.administrar.TiposMuestras;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.entity.administrar.Programas;
import com.proasecal.software.web.entity.inscripcion.PeriodosVigencia;
import com.proasecal.software.web.entity.parametricas.Pager;
import com.proasecal.software.web.service.administrar.MuestraService;
import com.proasecal.software.web.service.administrar.ProgramasService;
import com.proasecal.software.web.service.inscripcion.InscripcionMuestrasService;
import com.proasecal.software.web.service.inscripcion.InscripcionProgramasService;
import com.proasecal.software.web.service.inscripcion.PeriodosVigenciaService;
import com.proasecal.software.web.service.seguridad.FiltrosSeccionesService;
import com.proasecal.software.web.service.seguridad.SeccionesService;
import com.proasecal.software.web.util.Notify;
import com.proasecal.software.web.service.administrar.TipoMuestraService;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/muestras")
public class MuestrasController {

    @Autowired
    MuestraService muestraService;
    @Autowired
    TipoMuestraService tipoMuestraService;
    @Autowired
    SeccionesService seccionesService;
    @Autowired
    FiltrosSeccionesService filtrosSeccionesService;
    @Autowired
    ProgramasService programaService;
    @Autowired
    InscripcionMuestrasService inscripcionMuestrasService;
    @Autowired
    PeriodosVigenciaService periodosVigenciaService;

    @PersistenceContext
    private EntityManager em;

    // atributos para la paginacion
    private static int currentPage = 1;
    private static int pagSize = 20;
    private static String sortColumn = "muestraId";
    private static String sortO = "ASC";
    private static String programaFront = "_";
    private static String tipoMuestraFront = "_";
    private static String numeroFront = "_";
    private static String fechaFront = "_";
    private static final int[] PAGE_SIZES = {5, 10, 20, 50};
    private static final int BUTTONS_TO_SHOW = 3;


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam("programaFront") Optional<String> programaFront,
                             @RequestParam("tipoMuestraFront") Optional<String> tipoMuestraFront,
                             @RequestParam("numeroFront") Optional<String> numeroFront,
                             @RequestParam("fechaFront") Optional<String> fechaFront,
                             @RequestParam("nameColumn") Optional<String> nameColumn,
                             @RequestParam("sortBy") Optional<String> sortBy,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size,
                             @RequestParam("save") Optional<String> save,
                             @RequestParam("delete") Optional<Boolean> delete) {

        sortColumn = nameColumn.isPresent() ? nameColumn.get() : "idPrograma.nombrePrograma";
        sortO = sortBy.isPresent() ? sortBy.get() : "ASC";
        currentPage = page.isPresent() ? page.get() : 1;
        pagSize = size.isPresent() ? size.get() : 20;
        this.programaFront = programaFront.isPresent() ? programaFront.get() : "";
        this.tipoMuestraFront = tipoMuestraFront.isPresent() ? tipoMuestraFront.get() : "";
        this.numeroFront = numeroFront.isPresent() ? numeroFront.get() : "";
        this.fechaFront = fechaFront.isPresent() ? fechaFront.get() : "";


        int evalPageSize = size.orElse(pagSize);
        int evalPage = (page.orElse(0) < 1) ? currentPage : page.get() - 1;
        Page<Muestras> muestras =
                muestraService.ListPaginated(
                        programaFront.orElse(this.programaFront),
                        tipoMuestraFront.orElse(this.tipoMuestraFront),
                        numeroFront.orElse(this.numeroFront),
                        fechaFront.orElse(this.fechaFront),
                        PageRequest.of(
                                page.orElse(currentPage) - 1, pagSize, new Sort(Sort.Direction.valueOf(sortO), sortColumn, "muestraId")
                        ));

        Pager pager = new Pager(muestras.getTotalPages(), muestras.getNumber(), BUTTONS_TO_SHOW);

        ModelAndView modelAndView = new ModelAndView("web/administrar/muestras/list");

        modelAndView.addObject("filtrosList", filtrosSeccionesService.getListFiltros(seccionesService.findByName("muestras")));
        modelAndView.addObject("muestras", muestras);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("nameColumn", nameColumn.orElse(sortColumn));  
        modelAndView.addObject("sort", sortBy.orElse(sortO));
        modelAndView.addObject("programaFront", programaFront.orElse("").equalsIgnoreCase("_") ? "" : programaFront.orElse(""));
        modelAndView.addObject("tipoMuestraFront", tipoMuestraFront.orElse("").equalsIgnoreCase("_") ? "" : tipoMuestraFront.orElse(""));
        modelAndView.addObject("numeroFront", numeroFront.orElse("").equalsIgnoreCase("_") ? "" : numeroFront.orElse(""));
        modelAndView.addObject("fechaFront", fechaFront.orElse("").equalsIgnoreCase("_") ? "" : fechaFront.orElse(""));
        modelAndView.addObject("programasList", programaService.list());
        modelAndView.addObject("tipoMuestraList", tipoMuestraService.list());
		

        if (save.isPresent()) {
            if (save.get().equalsIgnoreCase("create")) {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Muestra creada correctamente"));
            } else {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Muestra modificada correctamente"));
            }
        }
        //Si Borra un metodo
        if (delete.isPresent()) {
            if (delete.get()) {
                modelAndView.addObject("notify", Notify.SUCCESS("!OK", "Muestra eliminada correctamente"));
            } else {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo eliminar la Muestra"));
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/create")
    public ModelAndView form() {
        ModelAndView modelAndView = new ModelAndView("web/administrar/muestras/form");
        modelAndView.addObject("muestrasForm", new Muestras());
        modelAndView.addObject("programas", programaService.list());
        return modelAndView;
    }

    @GetMapping(value = "/edit/{id}")
    public ModelAndView edit(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("web/administrar/muestras/form");
        Optional<Muestras> muestras = this.muestraService.find(id);

        if (!muestras.isPresent()) {
            modelAndView.addObject("muestrasForm", new Muestras());
            modelAndView.addObject("notify", Notify.ERROR("ERROR", "La muestra a consultar no existe"));
            return modelAndView;
        }

        List<TiposMuestras> models = tipoMuestraService.obtenerTipoMuestraxPrograma(muestras.get().getIdTipoMuestra().getIdPrograma());
        modelAndView.addObject("models3", models);
        modelAndView.addObject("programas", programaService.list());
        modelAndView.addObject("muestrasForm", muestras.get());
        return modelAndView;
    }

    @PostMapping(value = "/save")
    public ModelAndView save(@Valid @ModelAttribute("muestrasForm") Muestras muestrasForm, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                             ModelAndView model) {
        ModelAndView modelAndView = new ModelAndView("web/administrar/muestras/form");

        if (this.muestraService.exist(muestrasForm)) {
            bindingResult.rejectValue("numeroMuestra", "error", "La combinación Numero Muestra y Programa debe ser única.");
        }

        if (this.muestraService.validDate(muestrasForm)) {
            bindingResult.rejectValue("fechaInicial", "error", "La fecha inicial no puede ser superior a la fecha final.");
        }
        if (bindingResult.hasErrors()) {
            model.setViewName("web/administrar/muestras/form");
            model.addObject("programas", programaService.list());
            model.addObject("muestrasForm", muestrasForm);
            List<TiposMuestras> models = tipoMuestraService.obtenerTipoMuestraxPrograma(muestrasForm.getIdPrograma());
            model.addObject("models3", models);
            return model;
        }

        String ir = "create";
        if (muestrasForm.getMuestraId() != null && muestrasForm.getMuestraId() != 0) {
            ir = "update";
        }

        try {
            this.muestraService.save(muestrasForm);
        } catch (Exception e) {
            modelAndView.addObject("programas", programaService.list());
            List<TiposMuestras> models = tipoMuestraService.obtenerTipoMuestraxPrograma(muestrasForm.getIdPrograma());
            modelAndView.addObject("models3", models);
            if (ir.equalsIgnoreCase("create")) {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo crear la Muestra"));
            } else {
                modelAndView.addObject("notify", Notify.ERROR("ERROR", "No se pudo modificar la Muestra"));
            }

            return modelAndView;
        }

        redirectAttributes.addAttribute("save", ir);
        return new ModelAndView("redirect:/muestras/list");
    }


    @RequestMapping(value = "/delete")
    public ModelAndView form(@RequestParam("muestraId") String muestraId, ModelAndView model, RedirectAttributes
            redirectAttributes) {
    	
    	try {
            	muestraService.borrarXId(Long.parseLong(muestraId));
                //Si Borra un metodo
                redirectAttributes.addAttribute("delete", true);
                return new ModelAndView("redirect:/muestras/list");

		} catch (Exception e) {
		       redirectAttributes.addAttribute("delete", false);
               return new ModelAndView("redirect:/muestras/list");
		}
    }

    @RequestMapping("/obtTipoMuestra")
    public String obtenerTipoMuestraxPrograma(@RequestParam("idPrograma") String idPrograma, Model model) {
        Programas programa = new Programas();
        programa.setProgramaId(Long.valueOf(idPrograma).longValue());
        List<TiposMuestras> models = tipoMuestraService.obtenerTipoMuestraxPrograma(programa);
        model.addAttribute("models3", models);
        return "web/administrar/muestras/form :: models3";
    }

    @GetMapping(value = "/fechas")
    public String muestrasVigentes(@RequestParam("fechaInicial") String fechaInicial,
                                   @RequestParam("fechaFinal") String fechaFinal,
                                   @RequestParam("idPrograma") String idPrograma,
                                   //@RequestParam("idUsuario") String idUsuario,
                                   Model model) throws Exception {

        List<MuestrasInscritas> muestrasInscritasList = new ArrayList<>();
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(fechaInicial);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(fechaFinal);
        Optional<PeriodosVigencia> periodosVigencia = periodosVigenciaService.find(Long.valueOf(idPrograma));
        Programas programas = periodosVigencia.get().getInscripProgramaId().getProgramaId();
        List<Muestras> muestrasLists = muestraService.muestrasParaInscribir(date1, date2, programas);

        for (int i = 0; i < muestrasLists.size(); i++) {
            MuestrasInscritas muestrasInscritas = new MuestrasInscritas();
            muestrasInscritas.setIdMuestra(muestrasLists.get(i).getMuestraId());
            muestrasInscritas.setNombreMuestra(muestrasLists.get(i).getNumeroMuestra());

            if (inscripcionMuestrasService.findMuestraInscrita(muestrasLists.get(i), periodosVigencia.get().getInscripProgramaId(),
                    periodosVigencia.get().getInscripProgramaId().getIdUsuarioLabSedes()).isPresent()) {
                muestrasInscritas.setEstado(true);
            } else{
                muestrasInscritas.setEstado(false);
            }
            muestrasInscritasList.add(i, muestrasInscritas);
        }

        model.addAttribute("muestras", muestrasInscritasList);
        return "web/inscripcion/periodosvigencia/list :: tablaMuestras";
    }
}
