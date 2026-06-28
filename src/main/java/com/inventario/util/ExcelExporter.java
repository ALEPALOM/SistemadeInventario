package com.inventario.util;

import com.inventario.model.Producto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileOutputStream;
import java.util.List;

public class ExcelExporter {
    private static final Logger logger = LoggerFactory.getLogger(ExcelExporter.class);

    public static void exportar(List<Producto> productos, String rutaArchivo) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Inventario");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Nombre");
            headerRow.createCell(2).setCellValue("Cantidad");
            headerRow.createCell(3).setCellValue("Precio");

            int rowNum = 1;
            for (Producto p : productos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getNombre());
                row.createCell(2).setCellValue(p.getCantidad());
                row.createCell(3).setCellValue(p.getPrecio());
            }

            try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
                workbook.write(fileOut);
                logger.info("Inventario exportado exitosamente a Excel.");
            }
        } catch (Exception e) {
            logger.error("Error al exportar a Excel", e);
        }
    }
}