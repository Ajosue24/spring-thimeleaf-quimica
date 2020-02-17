package com.proasecal.software.controlexterno.controller.simulacion;

import com.proasecal.software.controlexterno.entity.ResultadosDetallados;
import com.proasecal.software.controlexterno.service.ResultadosDetalladosService;
import com.proasecal.software.controlexterno.service.ResultadosService;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.administrar.Muestras;
import com.proasecal.software.web.service.administrar.MensurandoService;
import com.proasecal.software.web.service.administrar.MuestraService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ResultadosReportados extends AbstractXlsView {

    Muestras muestra;
    Mensurandos mensurando;

    @Autowired
    MuestraService muestraService;
    @Autowired
    MensurandoService mensurandoService;
    @Autowired
    ResultadosService resultadosService;
    @Autowired
    ResultadosDetalladosService resultadosDetalladosService;

    public ResultadosReportados(Muestras muestra, Mensurandos mensurando) {
        this.muestra = muestra;
        this.mensurando = mensurando;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        String[] titulos = {"Usuario", "Mensurando", "Valor informado", "Método", "Reactivo", "Equipo"};
        // Trae la lista
        List<ResultadosDetallados> resultadosDetalladosList = (List<ResultadosDetallados>) map.get("resultadosDetallados");

        String archivoName = "Resultados " + muestra.getNumeroMuestra() + "-" + mensurando.getNombreMensurando().trim() + ".xls";
        httpServletResponse.setHeader("Content-Disposition", "attachement; filename=\"" + archivoName + "\"");
        Sheet sheet = workbook.createSheet("Resultados");
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < titulos.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(titulos[i]);
        }

        int rowNum = 1;

        for (ResultadosDetallados resultadosDetallados : resultadosDetalladosList) {
            int col = 0;
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(resultadosDetallados.getIdResultados().getIdUsuarios().getNombreUsuario());
            row.createCell(1).setCellValue(resultadosDetallados.getMensurandosId().getNombreMensurando());
            row.createCell(2).setCellValue(resultadosDetallados.getValorReportado());
            row.createCell(3).setCellValue(resultadosDetallados.getMetodoId().getNombreMetodo());
            row.createCell(4).setCellValue(resultadosDetallados.getReactivoId().getNombreReactivo());
            row.createCell(5).setCellValue(resultadosDetallados.getEquipoId().getNombreEquipo());

            while (col <= 5) {
                sheet.autoSizeColumn(col);
                col++;
            }
        }
    }
}
