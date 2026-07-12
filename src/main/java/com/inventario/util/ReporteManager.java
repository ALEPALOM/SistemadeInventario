package com.inventario.util;

import com.inventario.dao.EquipoDAO;
import com.inventario.model.Equipo;
import org.apache.poi.xssf.usermodel.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.io.*;
import java.util.List;

public class ReporteManager {
    private EquipoDAO dao = new EquipoDAO();

    public void exportarExcel(File archivo) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Inventario");
            String[] cols = {"ID", "Serie", "Tipo", "Marca", "Modelo", "Estado", "Ubicación"};
            var row = sheet.createRow(0);
            for(int i=0; i<cols.length; i++) row.createCell(i).setCellValue(cols[i]);
            
            int r = 1;
            for (Equipo e : dao.listarEquipos()) {
                var f = sheet.createRow(r++);
                f.createCell(0).setCellValue(e.getIdEquipo());
                f.createCell(1).setCellValue(e.getNumeroSerie());
                f.createCell(2).setCellValue(e.getIdTipo());
                f.createCell(3).setCellValue(e.getMarca());
                f.createCell(4).setCellValue(e.getModelo());
                f.createCell(5).setCellValue(e.getIdEstado());
                f.createCell(6).setCellValue(e.getIdUbicacion());
            }
            try (FileOutputStream out = new FileOutputStream(archivo)) { workbook.write(out); }
        }
    }

    public void exportarPDF(File archivo) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(new FileOutputStream(archivo));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        document.add(new Paragraph("Reporte de Inventario - Colegio Claretiano"));
        Table table = new Table(7);
        table.addHeaderCell("ID"); table.addHeaderCell("Serie"); table.addHeaderCell("Tipo");
        table.addHeaderCell("Marca"); table.addHeaderCell("Modelo"); table.addHeaderCell("Estado"); table.addHeaderCell("Ubicación");
        
        for (Equipo e : dao.listarEquipos()) {
            table.addCell(String.valueOf(e.getIdEquipo()));
            table.addCell(e.getNumeroSerie());
            table.addCell(String.valueOf(e.getIdTipo()));
            table.addCell(e.getMarca());
            table.addCell(e.getModelo());
            table.addCell(String.valueOf(e.getIdEstado()));
            table.addCell(String.valueOf(e.getIdUbicacion()));
        }
        document.add(table);
        document.close();
    }
}